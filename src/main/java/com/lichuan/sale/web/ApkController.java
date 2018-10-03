package com.lichuan.sale.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApkController extends BaseController {

    @RequestMapping("updateApk")
    public String updateApk(ModelMap map) {
        map.put("name", "Spring Boot");
        return "uploadApk";
    }


}
