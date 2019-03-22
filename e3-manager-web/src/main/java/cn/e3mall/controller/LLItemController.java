package cn.e3mall.controller;

import cn.e3mail.service.LLItemService;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.pojo.TbItem;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LLItemController {
    @Autowired
    private LLItemService itemService;

    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId) {
      TbItem tbItem =   itemService.getItemById(itemId);
      return tbItem;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public LLPageCommon<TbItem> getItemList(int page , int rows) {
        LLPageCommon<TbItem> pageCommon =   itemService.getGridResult(page, rows);
        return pageCommon;
    }

    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(TbItem item,String desc) {
        TaotaoResult taotaoResult = itemService.addItem(item, desc);
        return taotaoResult;
    }
}
