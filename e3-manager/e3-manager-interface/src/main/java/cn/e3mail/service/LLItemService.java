package cn.e3mail.service;

import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.pojo.TbItem;

public interface LLItemService {
    TbItem getItemById(long itemId);
    LLPageCommon<TbItem> getGridResult(Integer row ,Integer pagesize);
}
