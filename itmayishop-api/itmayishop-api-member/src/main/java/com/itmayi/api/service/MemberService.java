package com.itmayi.api.service;

import com.itmayi.base.ResponseBase;
import com.itmayi.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member")
public interface MemberService {
    @RequestMapping("findUserById")
    ResponseBase findUserById(Integer id);
    @RequestMapping("/regUser")
    ResponseBase regUser(@RequestBody UserEntity user);
    @RequestMapping("/login")
    ResponseBase login(@RequestBody UserEntity user);
    @RequestMapping("/findByToken")
    ResponseBase findByToken(String token);
}
