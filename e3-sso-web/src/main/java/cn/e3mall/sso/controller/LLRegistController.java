package cn.e3mall.sso.controller;

import cn.e3mail.sso.service.LLResgistService;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LLRegistController {
    @Autowired
    private LLResgistService resgistService;
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult cheackUser(@PathVariable String param,@PathVariable Integer type){
        return resgistService.checkUser(param, type);
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public  TaotaoResult register(TbUser user) {
        return resgistService.register(user);
    }

    @RequestMapping("/register")
    public  String showRegister() {
        return "register";
    }

}
