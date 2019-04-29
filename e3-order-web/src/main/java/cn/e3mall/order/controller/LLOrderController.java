package cn.e3mall.order.controller;

import cn.e3mall.cart.service.LLCartService;
import cn.e3mall.order.pojo.LLOrderInfo;
import cn.e3mall.order.service.LLOrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LLOrderController {

    @Autowired
    private LLCartService cartService;
    @Autowired
    private LLOrderService orderService;
    @RequestMapping("/order/order-cart")
    public  String showOrder(HttpServletRequest request) {
       TbUser tbUser = (TbUser) request.getAttribute("user");
       List<TbItem> tbItemList = cartService.getCartList(tbUser.getId());
       request.setAttribute("cartList", tbItemList);
        request.setAttribute("totalPrice", 100000);
        return "order-cart";
    }

    @RequestMapping("/order/create")
    public String createOrder(HttpServletRequest request, LLOrderInfo orderInfo) {
       TbUser tbUser = (TbUser) request.getAttribute("user");
       orderInfo.setUserId(tbUser.getId());
       orderInfo.setBuyerNick(tbUser.getUsername());
      TaotaoResult taotaoResult = orderService.insertOrder(orderInfo);
      if (taotaoResult.getStatus() == 200) {
          cartService.clearCart(tbUser.getId());
      }
      request.setAttribute("payment", Long.getLong(orderInfo.getPayment()));
      request.setAttribute("orderId", taotaoResult.getData());
      return "success";
    }
}
