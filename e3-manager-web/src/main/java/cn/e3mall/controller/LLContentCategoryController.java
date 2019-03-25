package cn.e3mall.controller;

import cn.content.service.LLContentCategoryService;
import cn.e3mall.common.pojo.LLTreeNodeCommon;
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
}
