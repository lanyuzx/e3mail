package cn.e3mall.controller;

import cn.e3mial.common.utils.TaotaoResult;
import cn.search.service.LLSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LLSearchItemController {
    @Autowired
    private LLSearchService searchService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public TaotaoResult importAllSearchItem() {
       TaotaoResult taotaoResult = searchService.importAllSerachItems();
       return  taotaoResult;
    }
}
