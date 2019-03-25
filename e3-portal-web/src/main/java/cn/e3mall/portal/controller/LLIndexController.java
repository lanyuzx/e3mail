package cn.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LLIndexController {
    @RequestMapping("index")
    public  String index() {
        return  "index";
    }
}
