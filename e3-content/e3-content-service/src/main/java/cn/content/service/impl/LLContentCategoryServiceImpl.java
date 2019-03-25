package cn.content.service.impl;

import cn.content.service.LLContentCategoryService;
import cn.e3mall.common.pojo.LLTreeNodeCommon;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class LLContentCategoryServiceImpl implements LLContentCategoryService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<LLTreeNodeCommon> getContentCategoryList(long parentId) {
        TbContentCategoryExample categoryExample = new TbContentCategoryExample();
       TbContentCategoryExample.Criteria criteria =  categoryExample.createCriteria();
       criteria.andParentIdEqualTo(parentId);
       List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(categoryExample);
       List<LLTreeNodeCommon> treeNodeCommonList = new ArrayList<>();
       for (TbContentCategory category : categoryList){
           LLTreeNodeCommon nodeCommon = new LLTreeNodeCommon();
           nodeCommon.setId(category.getId());
           nodeCommon.setState(category.getIsParent() ? "" : "");
           nodeCommon.setText(category.getName());
           treeNodeCommonList.add(nodeCommon);
       }
        return treeNodeCommonList;
    }
}
