package cn.e3mail.service;

import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.pojo.TbItem;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLItemService {
    TbItem getItemById(long itemId);
    LLPageCommon<TbItem> getGridResult(Integer row ,Integer pagesize);
    //插入商品
    TaotaoResult addItem(TbItem item,String desc);

    //删除该条商品
    TaotaoResult deleteItem(long id);

    //根据id查询该条商品描述
    TaotaoResult findItemDescByIdToJson(long id);
    //根据id查询该条商品
    TaotaoResult findItemByIdToJson(long id);
    //更新商品
    TaotaoResult updateItem(TbItem item,String desc);
    //下架产品
    TaotaoResult instockItem(long id);
    //上架产品
    TaotaoResult reshelfItem(long id);
}
