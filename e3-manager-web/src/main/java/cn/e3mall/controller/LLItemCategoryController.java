package cn.e3mall.controller;

import cn.e3mail.service.LLItemCategoryService;
import cn.e3mall.common.pojo.LLTreeNodeCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LLItemCategoryController {

    @Autowired
    private LLItemCategoryService categoryService;
    @RequestMapping("item/cat/list")
    @ResponseBody
    public List<LLTreeNodeCommon> getItemCategoryList(@RequestParam(value = "id",defaultValue = "0") long parentId) {
       List<LLTreeNodeCommon> treeNodeCommonList = categoryService.getTreeNodeList(parentId);
       return treeNodeCommonList;
    }
}
