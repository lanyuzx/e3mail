package cn.e3mall.controller;

import cn.content.service.LLContentService;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.pojo.TbContent;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LLContentController {
    @Autowired
    private LLContentService contentService;
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent content) {
        TaotaoResult taotaoResult = contentService.addContent(content);
        return  taotaoResult;
    }

    @RequestMapping("/content/query/list")
    @ResponseBody
    public LLPageCommon<TbContent> getContentList(int page,int rows) {
        LLPageCommon<TbContent> pageCommon = contentService.getGridResult(page, rows);
        return  pageCommon;
    }

    @RequestMapping()
    @ResponseBody
    public TaotaoResult deleteItemContent(long ids) {
        TaotaoResult taotaoResult = contentService.deleteItenContent(ids);
        return  taotaoResult;
    }
}
