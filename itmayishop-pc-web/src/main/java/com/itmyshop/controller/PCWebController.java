package com.itmyshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PCWebController {
    private static final String INDEX="index";
    @RequestMapping("/")
    public String index(){
        return INDEX;
    }
}
