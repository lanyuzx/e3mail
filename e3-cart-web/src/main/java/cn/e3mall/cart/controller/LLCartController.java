package cn.e3mall.cart.controller;

import cn.e3mail.service.LLItemService;
import cn.e3mall.pojo.TbItem;
import cn.e3mial.common.utils.CookieUtils;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LLCartController {

    @Autowired
    private LLItemService itemService;

    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {

        boolean flag = false;
    List<TbItem> tbItemList = getCartListFromCookie(request);
     for (TbItem item : tbItemList) {
         if (item.getId() == itemId.longValue()) {
             flag = true;
             item.setNum(item.getNum() + num);
             break;
         }
     }
     if (flag) {
        TbItem  item =  itemService.getItemById(itemId);
        tbItemList.add(item);
        item.setNum(num);
        CookieUtils.setCookie(request, response, "cart", new Gson().toJson(tbItemList));
     }

        return "cartSuccess";
    }

    private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
       String json = CookieUtils.getCookieValue(request, "cart");
       if (StringUtils.isBlank(json)) {
           return new ArrayList<>();
       }
        Gson gson = new Gson();
      List<TbItem> tbItemList = (List<TbItem>) gson.fromJson(json, TbItem.class);
      return  tbItemList;
    }
}
