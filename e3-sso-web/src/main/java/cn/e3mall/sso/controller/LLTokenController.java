package cn.e3mall.sso.controller;
import cn.e3mail.sso.service.LLTokenService;
import cn.e3mial.common.utils.TaotaoResult;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LLTokenController {
    @Autowired
    private LLTokenService tokenService;

    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public String getUserByToken(@PathVariable String token, HttpServletRequest request) {
       String callback = request.getParameter("callback");
        TaotaoResult result =  tokenService.getUserByToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(result);
        return  callback + "(" + json +")";
    }

}
