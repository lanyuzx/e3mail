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
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public  TaotaoResult deleteItem (long ids) {
        TaotaoResult taotaoResult = itemService.deleteItem(ids);
        return   taotaoResult;
    }

    @RequestMapping("/rest/item/edit")
    public ModelAndView editdItem(long ids) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/item-edit");
        //设置产品
        TaotaoResult productItem = itemService.findItemByIdToJson(ids);
        modelAndView.addObject("item", productItem.getData());
        //设置描述
        TaotaoResult itemDesc = itemService.findItemDescByIdToJson(ids);
        modelAndView.addObject("itemDesc", itemDesc.getData());
        return  modelAndView;
    }

    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public TaotaoResult instockItem(long ids) {
         TaotaoResult result =  itemService.instockItem(ids);
        return  result;
    }
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public TaotaoResult reshelfItem(long ids) {
        TaotaoResult result =  itemService.reshelfItem(ids);
        return  result;
    }

}
