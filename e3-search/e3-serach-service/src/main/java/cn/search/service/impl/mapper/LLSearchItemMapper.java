package cn.search.service.impl.mapper;

import cn.e3mall.common.pojo.LLSearchItem;

import java.util.List;

public interface LLSearchItemMapper {

    List<LLSearchItem> getSearchItemList();
    LLSearchItem findSearchItemById(long itemId);
}
