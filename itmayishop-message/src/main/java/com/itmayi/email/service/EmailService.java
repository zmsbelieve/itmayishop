package com.itmayi.email.service;

import com.alibaba.fastjson.JSONObject;
import com.itmayi.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements MessageAdapter {

    @Value("${msg.subject}")
    String subject;
    @Value("${msg.text}")
    String text;
    @Autowired
    JavaMailSender javaMailSender;
    @Override
    public void sendMsg(JSONObject body) {
    // 处理发送邮件
        String email=body.getString("email");
        if(StringUtils.isEmpty(email)){
            return ;
        }
        log.info("消息服务平台发送邮件:{}开始",email);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text.replace("{}",email));
        javaMailSender.send(simpleMailMessage);
        log.info("消息服务平台发送邮件:{}结束",email);
    }
}
