package cn.search.service;

import cn.e3mall.common.pojo.LLSearchResult;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLSearchService {
    TaotaoResult importAllSerachItems();
    LLSearchResult searchItem(String keyWord, int page, int rows) throws Exception;
}

