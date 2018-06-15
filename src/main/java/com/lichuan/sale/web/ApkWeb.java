package com.lichuan.sale.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.RegEx;

@Controller
@RequestMapping("web")
public class ApkWeb extends BaseController {


    @RequestMapping("index")
    public String login(String username,String password){


        return "index";
    }
}
