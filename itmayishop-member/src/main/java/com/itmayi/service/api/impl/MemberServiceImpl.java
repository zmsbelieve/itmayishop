package com.itmayi.service.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.itmayi.api.service.MemberService;
import com.itmayi.base.BaseApiService;
import com.itmayi.base.ResponseBase;
import com.itmayi.constants.Constants;
import com.itmayi.dao.UserDao;
import com.itmayi.entity.UserEntity;
import com.itmayi.mq.RegisterMailboxProducer;
import com.itmayi.utils.MD5Util;
import com.itmayi.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MemberServiceImpl extends BaseApiService implements MemberService {
    @Autowired
    UserDao userDao;//在当前项目的pom文件下加入mybatis-spring-boot-starter
    @Autowired
    RegisterMailboxProducer registerMailboxProducer;
    @Value("${message.queue}")
    String MESSAGESQUEUE;
    @Override
    public ResponseBase findUserById(Integer id) {
        UserEntity user = userDao.findByID(37l);
        if(user == null){
            return setResultError("未查找到..");
        }
        return setResultSuccess(user);
    }

    @Override
    public ResponseBase regUser(@RequestBody UserEntity user) {
        if(StringUtils.isEmpty(user.getPassword())){
            return setResultError("密码不能为空!");
        }
        String password = MD5Util.MD5(user.getPassword());
        user.setPassword(password);
        userDao.insertUser(user);
        // 采用异步方式发送消息
        String email = user.getEmail();
        String json = emailJson(email);
        log.info("####会员服务推送消息到消息服务平台####json:{}", json);
        sendMsg(json);
        return setResultSuccess("注册成功");
    }
    private String emailJson(String email) {
        JSONObject rootJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType", Constants.MSG_EMAIL);
        JSONObject content = new JSONObject();
        content.put("email", email);
        rootJson.put("header", header);
        rootJson.put("content", content);
        return rootJson.toJSONString();
    }

    private void sendMsg(String json) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue, json);
    }

    @Override
    public ResponseBase login(@RequestBody UserEntity user) {
        if(StringUtils.isEmpty(user.getUsername())){
            setResultError("账号不能为空!");
        }
        if(StringUtils.isEmpty(user.getPassword())){
            setResultError("密码不能为空!");
        }
        UserEntity userEntity = userDao.login(user.getUsername(), MD5Util.MD5(user.getPassword()));
        if(userEntity == null){
            return setResultError("账号或密码错误!");
        }
        String token = TokenUtils.getMemberToken();
        baseRedisService.setString(token,userEntity.getId()+"",Constants.TOKEN_MEMBER_TIME);
        log.info("####用户信息token存放在redis中... key为:{},value为:{}", token, userEntity.getId());
        return setResultSuccess(new JSONObject().put("memberToken",token));
    }

    @Override
    public ResponseBase findByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return setResultError("token不能为空!");
        }
        String userId = baseRedisService.getString(token);
        if(userId == null){
            return setResultError("token无效或者已过期!");
        }
        UserEntity user = userDao.findByID(Long.parseLong(userId));
        user.setPassword(null);
        return setResultSuccess(user);
    }
}
