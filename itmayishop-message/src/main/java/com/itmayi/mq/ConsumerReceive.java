package com.itmayi.mq;

import com.alibaba.fastjson.JSONObject;
import com.itmayi.adapter.MessageAdapter;
import com.itmayi.constants.Constants;
import com.itmayi.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerReceive {
    private MessageAdapter messageAdapter;
    @Autowired
    private EmailService emailService;
    @JmsListener(destination = "message_queue")
    public void receive(String json){
        log.info("#####消息服务平台接受消息内容:{}#####", json);
        if(StringUtils.isEmpty(json)){
            return;
        }
        JSONObject rootJSON = new JSONObject().parseObject(json);
        JSONObject header = rootJSON.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");

        if (StringUtils.isEmpty(interfaceType)) {
            return;
        }
        // 判断接口类型是否为发邮件
        if (interfaceType.equals(Constants.MSG_EMAIL)) {
            messageAdapter = emailService;
        }
        if (messageAdapter == null) {
            return;
        }
        JSONObject contentJson = rootJSON.getJSONObject("content");
        messageAdapter.sendMsg(contentJson);
    }
}
