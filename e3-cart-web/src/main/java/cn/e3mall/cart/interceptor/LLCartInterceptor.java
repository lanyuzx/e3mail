package cn.e3mall.cart.interceptor;

import cn.e3mail.sso.service.LLTokenService;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.CookieUtils;
import cn.e3mial.common.utils.TaotaoResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//购物车的拦截器
public class LLCartInterceptor implements HandlerInterceptor {

    @Autowired
    private LLTokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先从获取token
        String token = CookieUtils.getCookieValue(request, "token");
        //未登录直接放行
        if (StringUtils.isBlank(token)){
            return true;
        }
        //获取到用户登录调用sso服务获取用户登录信息
      TaotaoResult taotaoResult = tokenService.getUserByToken(token);
        if (taotaoResult.getStatus() != 200) {
            return true;
        }
        TbUser tbUser = (TbUser) taotaoResult.getData();
        request.setAttribute("user", tbUser);
        return true;
    }
}
