package cn.e3mail.service;

import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.pojo.TbItem;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLItemService {
    TbItem getItemById(long itemId);
    LLPageCommon<TbItem> getGridResult(Integer row ,Integer pagesize);
    //插入商品
    TaotaoResult addItem(TbItem item,String desc);
}
