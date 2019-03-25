package cn.content.service;

import cn.e3mall.common.pojo.LLTreeNodeCommon;

import java.util.List;

public interface LLContentCategoryService {
    List<LLTreeNodeCommon> getContentCategoryList(long parentId);
}
