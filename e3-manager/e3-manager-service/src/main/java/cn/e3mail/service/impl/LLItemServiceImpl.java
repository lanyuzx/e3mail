package cn.e3mail.service.impl;

import cn.e3mail.service.LLItemService;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LLItemServiceImpl implements LLItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public TbItem getItemById(long itemId) {

        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public LLPageCommon<TbItem> getGridResult(Integer row, Integer pagesize) {
        PageHelper.startPage(row);
        TbItemExample itemExample = new TbItemExample();
       List <TbItem> itemList =  itemMapper.selectByExample(itemExample);
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);
        LLPageCommon pageCommon = new LLPageCommon();
        pageCommon.setRows(itemList);
        pageCommon.setTotal(pageInfo.getTotal());
        return pageCommon;
    }
}
