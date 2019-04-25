package cn.e3mall.sso.controller;

import cn.e3mail.sso.service.LLTokenService;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.TaotaoResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LLTokenController {
    @Autowired
    private LLTokenService tokenService;

    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callBack, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        TaotaoResult result =  tokenService.getUserByToken(token);
        if (StringUtils.isNotBlank(callBack)) {
            MappingJacksonValue mappingJacksonValue =  new MappingJacksonValue(result);
          mappingJacksonValue.setValue(callBack);
          return   mappingJacksonValue;
        }
        return  result;
    }

}
