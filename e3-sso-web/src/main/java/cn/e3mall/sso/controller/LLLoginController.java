package cn.e3mall.sso.controller;

import cn.e3mail.sso.service.LLLoginService;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.CookieUtils;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LLLoginController {

    @Autowired
    private LLLoginService loginService;
     @Value("${TOKEN_KEY}")
      private  String TOKEN_KEY;

    @RequestMapping("/user/login")
    @ResponseBody
    public TaotaoResult userLogin(TbUser user, HttpServletRequest request, HttpServletResponse response,String redirect) {
      TaotaoResult result =  loginService.userLogin(user);
      //把生成的token写入cooker
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
      return result;
    }

    @RequestMapping("/page/login")
    public String showLogin(String redirect , Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }
}
