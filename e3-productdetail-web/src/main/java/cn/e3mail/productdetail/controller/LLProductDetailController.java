package cn.e3mail.productdetail.controller;

import cn.e3mail.service.LLItemService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.productdetail.pojo.LLProductDetailItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LLProductDetailController {

    @Autowired
    private LLItemService itemService;
    @RequestMapping("/item/{itemId}")
    public  String showItemInfo(@PathVariable long itemId, Model model) {
        TbItem tbItem = itemService.getItemById(itemId);LLProductDetailItem detailItem = new LLProductDetailItem(tbItem);
       TbItemDesc itemDesc =  itemService.getItemDescById(itemId);
       model.addAttribute("item", detailItem);
       model.addAttribute("itemDesc", itemDesc);
        return  "item";
    }
}
