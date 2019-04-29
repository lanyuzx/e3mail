package cn.e3mall.order.interceptor;

import cn.e3mail.sso.service.LLTokenService;
import cn.e3mall.cart.service.LLCartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.CookieUtils;
import cn.e3mial.common.utils.TaotaoResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.List;

public class LLOrderLoginInterceptor implements HandlerInterceptor {

    @Value("${SSO_URL}")
    private String SSO_URL;

    @Autowired
    private LLTokenService tokenService;

    @Autowired
    private LLCartService cartService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      //下获取token
        String token = CookieUtils.getCookieValue(request, "token");
        if (StringUtils.isBlank(token)) {
             response.sendRedirect(SSO_URL +"?redirect=" + request.getRequestURI());
             return false;
        }
       TaotaoResult taotaoResult = tokenService.getUserByToken(token);
        if (taotaoResult.getStatus() != 200) {
            response.sendRedirect(SSO_URL +"?redirect=" + request.getRequestURI());
            return false;
        }
        TbUser tbUser = (TbUser) taotaoResult.getData();
        request.setAttribute("user", tbUser);
        //合并cooker数据到redis
      String json =  CookieUtils.getCookieValue(request, "cart",true);
      if (StringUtils.isNotBlank(json)) {
          Type type =  new TypeToken<List<TbItem>>() {}.getType();
          Gson gson  = new Gson();
          List<TbItem> tbItemList = gson.fromJson(json, type);
          cartService.mergeCart(tbItemList, tbUser.getId());
      }

        return true;
    }
}
