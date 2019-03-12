package cn.e3mail.service;

import cn.e3mall.common.pojo.LLTreeNodeCommon;

import java.util.List;

public interface LLItemCategoryService {
    List<LLTreeNodeCommon> getTreeNodeList(long parentId);
}
