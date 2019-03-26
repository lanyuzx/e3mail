package cn.e3mall.controller;

import cn.content.service.LLContentCategoryService;
import cn.e3mall.common.pojo.LLTreeNodeCommon;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LLContentCategoryController {
    @Autowired
    private LLContentCategoryService contentCategoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<LLTreeNodeCommon> getContentCategoryList(@RequestParam(name = "id",defaultValue = "0") long id) {
       List<LLTreeNodeCommon> list = contentCategoryService.getContentCategoryList(id);
       return  list;
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCategory(long parentId , String name) {
        TaotaoResult taotaoResult = contentCategoryService.addContentCategory(parentId, name);
        return  taotaoResult;
    }

    @RequestMapping("/content/category/delete")
    @ResponseBody
    public  TaotaoResult deleteItemContentCategory(long id) {
        TaotaoResult taotaoResult = contentCategoryService.deleteItemContentCategory(id);
        return  taotaoResult;
    }
    @RequestMapping("/content/category/update")
    @ResponseBody
    public  TaotaoResult updateContentCategoryItemName(long id,String name) {
        TaotaoResult taotaoResult = contentCategoryService.updateContentCategoryName(id, name);
        return  taotaoResult;
    }
}
