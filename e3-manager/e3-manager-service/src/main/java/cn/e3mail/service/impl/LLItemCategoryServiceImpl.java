package cn.e3mail.service.impl;

import cn.e3mail.service.LLItemCategoryService;
import cn.e3mall.common.pojo.LLTreeNodeCommon;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LLItemCategoryServiceImpl implements LLItemCategoryService {
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Override
    public List<LLTreeNodeCommon> getTreeNodeList(long parentId) {
        TbItemCatExample catExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = catExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> catList = itemCatMapper.selectByExample(catExample);
        List<LLTreeNodeCommon> treeNodeCommonList = new ArrayList<>();
        for (TbItemCat itemCat : catList) {
            LLTreeNodeCommon treeNode = new LLTreeNodeCommon();
            treeNode.setId(itemCat.getId());
            treeNode.setText(itemCat.getName());
            treeNode.setState(itemCat.getIsParent() ? "closed" :"open");
            treeNodeCommonList.add(treeNode);
        }
        return treeNodeCommonList;
    }
}
