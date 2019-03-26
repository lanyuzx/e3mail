package cn.content.service.impl;

import cn.content.service.LLContentCategoryService;
import cn.e3mall.common.pojo.LLTreeNodeCommon;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
           nodeCommon.setState(category.getIsParent() ? "closed" : "open");
           nodeCommon.setText(category.getName());
           treeNodeCommonList.add(nodeCommon);
       }
        return treeNodeCommonList;
    }

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //添加的都是叶子节点
        contentCategory.setIsParent(false);
        //1正常 2 删除
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);

        contentCategoryMapper.insert(contentCategory);

        TbContentCategory contentCategoryParent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!contentCategoryParent.getIsParent()){
            contentCategoryParent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(contentCategoryParent);
        }
        TaotaoResult taotaoResult = new TaotaoResult(contentCategoryParent);
        return taotaoResult;
    }

    @Override
    public TaotaoResult deleteItemContentCategory(long id) {
        TbContentCategory contentCategory =  contentCategoryMapper.selectByPrimaryKey(id);
        if (contentCategory.getIsParent()){
            TaotaoResult taotaoResult = new TaotaoResult(300, "请先删除该类下的叶子节点!", null);
            return  taotaoResult;
        }else  {
            contentCategoryMapper.deleteByPrimaryKey(id);
            //根据parentId查询下面有没有叶子节点 ,如果没有就把is_parent标识改为0
            TbContentCategoryExample categoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = categoryExample.createCriteria();
            criteria.andParentIdEqualTo(contentCategory.getParentId());
            List<TbContentCategory> contentCategoryList =  contentCategoryMapper.selectByExample(categoryExample);
            if (contentCategoryList.size() < 0  ) { //没有了叶子节点
                TbContentCategory parentContentCategory =  contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
                parentContentCategory.setIsParent(false);
                contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
            }
            TaotaoResult taotaoResult = new TaotaoResult(null);
            return  taotaoResult;
        }
    }

    @Override
    public TaotaoResult updateContentCategoryName(long id,String name) {
        TbContentCategory contentCategory =  contentCategoryMapper.selectByPrimaryKey(id);
         contentCategory.setName(name);
         contentCategoryMapper.updateByPrimaryKey(contentCategory);
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return  taotaoResult;
    }
}
