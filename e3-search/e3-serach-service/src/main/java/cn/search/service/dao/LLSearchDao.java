package cn.search.service.dao;

import cn.e3mall.common.pojo.LLSearchItem;
import cn.e3mall.common.pojo.LLSearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LLSearchDao {
    final String solrUrl = "http://127.0.0.1:8983/solr/new_core";
    //创建solrClient同时指定超时时间，不指定走默认配置
    HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl)
            .withConnectionTimeout(10000)
            .withSocketTimeout(60000)
            .build();
    public LLSearchResult searchItem(SolrQuery solrQuery) throws Exception {
        QueryResponse queryResponse  = solrClient.query(solrQuery);
        SolrDocumentList documentList = queryResponse.getResults();
        //总记录数
       long numFound  =  documentList.getNumFound();
        LLSearchResult searchResult = new LLSearchResult();
        searchResult.setRecourdCount((int) numFound);
        List<LLSearchItem> searchItemList = new ArrayList<>();
        //获取高亮的结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        for (SolrDocument solrDocument : documentList) {
            LLSearchItem searchItem = new LLSearchItem();
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setId((String) solrDocument.get("id"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((long) solrDocument.get("item_price"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮结果
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = null;
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(itemTitle);
            //添加到商品列表
            searchItemList.add(searchItem);

        }
        searchResult.setItemList(searchItemList);
        return  searchResult;
    }
}
