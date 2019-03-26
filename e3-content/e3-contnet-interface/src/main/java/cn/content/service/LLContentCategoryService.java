package cn.content.service;

import cn.e3mall.common.pojo.LLTreeNodeCommon;
import cn.e3mial.common.utils.TaotaoResult;

import java.util.List;

public interface LLContentCategoryService {
    List<LLTreeNodeCommon> getContentCategoryList(long parentId);

    TaotaoResult addContentCategory(long parentId, String name);

    TaotaoResult deleteItemContentCategory(long id);

    TaotaoResult updateContentCategoryName(long id,String name);
}
