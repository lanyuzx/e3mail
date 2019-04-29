package cn.e3mall.cart.controller;

import cn.e3mail.service.LLItemService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LLCartController {

    @Value("${COOKIE_CART_EXPIRE}")
    private  Integer COOKIE_CART_EXPIRE;
    @Autowired
    private LLItemService itemService;
    @Autowired
    private LLCartService cartService;

     @RequestMapping("cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {

         //如果用户登录了就把用户选择的商品存在本地数据库中
        TbUser tbUser = (TbUser) request.getAttribute("user");
        if (tbUser != null) {
            cartService.addCartToRedis(tbUser.getId(), itemId, num);
            return  "cartSuccess";
        }
        boolean flag = false;
     List<TbItem> tbItemList = getCartListFromCookie(request);
     for (TbItem item : tbItemList) {
         if (item.getId() == itemId.longValue()) {
             flag = true;
             item.setNum(item.getNum() + num);
             break;
         }
     }
     if (!flag) {
        TbItem  item =  itemService.getItemById(itemId);
        tbItemList.add(item);
        item.setNum(num);
        //取出第一张图片
         if (StringUtils.isNotBlank(item.getImage())) {
             String[] imgs = item.getImage().split(",");
             item.setImage(imgs[0]);
         }

     }
        CookieUtils.setCookie(request, response, "cart", new Gson().toJson(tbItemList),COOKIE_CART_EXPIRE ,true );

        return "cartSuccess";
    }

    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
       String json = CookieUtils.getCookieValue(request, "cart", true);
       if (StringUtils.isBlank(json)) {
           return new ArrayList<>();
       }

        Gson gson = new Gson();
        Type type = new TypeToken<List<TbItem>>(){}.getType();
      List<TbItem> tbItemList =  gson.fromJson(json,  type);
      return  tbItemList;
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult update(@PathVariable long itemId, @PathVariable Integer num,HttpServletRequest request,HttpServletResponse response) {
        TbUser tbUser = (TbUser) request.getAttribute("user");
         if (tbUser != null) {
            cartService.updateItem(tbUser.getId(), itemId,num);
            return TaotaoResult.ok();
        }
      List<TbItem> tbItemList =  getCartListFromCookie(request);
      for (TbItem item : tbItemList) {
          if (item.getId().longValue() == itemId) {
              item.setNum(num);
              break;
          }
      }
      CookieUtils.setCookie(request, response, "cart", new Gson().toJson(tbItemList), COOKIE_CART_EXPIRE, true);
      return  TaotaoResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable long itemId,HttpServletRequest request,HttpServletResponse response) {
        TbUser tbUser = (TbUser) request.getAttribute("user");
        if (tbUser != null) {
            cartService.deleteItem(tbUser.getId(), itemId);
            return "redirect:/cart/cart.html";
        }
         List<TbItem> tbItemList = getCartListFromCookie(request);
         for (TbItem tbItem : tbItemList) {
             if (tbItem.getId().longValue() == itemId) {
                 tbItemList.remove(tbItem);
                 break;
             }
         }
        CookieUtils.setCookie(request, response, "cart", new Gson().toJson(tbItemList), COOKIE_CART_EXPIRE, true);

         return "redirect:/cart/cart.html";
    }

    @RequestMapping("/cart/cart.html")
    public String showCartList(HttpServletRequest request, HttpServletResponse response) {

      TbUser tbUser = (TbUser) request.getAttribute("user");
      List<TbItem> tbItemList = getCartListFromCookie(request);
      if (tbUser != null) {
          //登录了就调用服务合并cooker购物车列表到本地
          cartService.mergeCart(tbItemList, tbUser.getId());
          //清楚cooker
          CookieUtils.deleteCookie(request, response, "cart");
          //从数据库中获取列表传递给表现层
        List<TbItem> tbItems =  cartService.getCartList(tbUser.getId());
        request.setAttribute("cartList", tbItems);
        return "cart";
      }
        request.setAttribute("cartList", tbItemList);
        return "cart";
    }
}
