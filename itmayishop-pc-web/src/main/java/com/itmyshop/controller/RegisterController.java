package com.itmyshop.controller;

import com.itmayi.base.ResponseBase;
import com.itmayi.constants.Constants;
import com.itmayi.entity.UserEntity;
import com.itmyshop.fegin.MemberServiceFegin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class RegisterController {
    private static final String REGISTER="register";
    private static final String LOGIN = "login";
    @Autowired
    private MemberServiceFegin memberServiceFegin;
    @RequestMapping(value="/register",method=RequestMethod.GET)
    public String getRegister(){
        return REGISTER;
    }
    @RequestMapping(value="/register",method=RequestMethod.POST)
    public String postRegister(UserEntity user, HttpServletRequest request){
        user.setCreated(new Date());
        user.setUpdated(new Date());
        ResponseBase result = memberServiceFegin.regUser(user);
        if(!Constants.HTTP_RES_CODE_200.equals(result.getCode())){
            request.setAttribute("error","注册失败!");
            return REGISTER;
        }
        int i = 1/0;
        return LOGIN;
    }
}
