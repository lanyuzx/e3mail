package cn.search.service.impl;

import cn.e3mall.common.pojo.LLSearchItem;
import cn.e3mial.common.utils.TaotaoResult;
import cn.search.service.LLSearchService;
import cn.search.service.mapper.LLSearchItemMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LLSearchServiceImpl implements LLSearchService {
    @Autowired
    private LLSearchItemMapper searchItemMapper;
    final String solrUrl = "http://127.0.0.1:8983/solr/new_core";
    //创建solrClient同时指定超时时间，不指定走默认配置
    HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl)
            .withConnectionTimeout(10000)
            .withSocketTimeout(60000)
            .build();



    @Override
    public TaotaoResult importAllSerachItems() {

      TaotaoResult taotaoResult = null;
      try {
          List<LLSearchItem> searchItemList = searchItemMapper.getSearchItemList();
          for (LLSearchItem searchItem : searchItemList) {
              SolrInputDocument document = new SolrInputDocument();
              document.addField("id", searchItem.getId());
              document.addField("item_title", searchItem.getTitle());
              document.addField("item_category_name", searchItem.getCategory_name());
              document.addField("item_sell_point", searchItem.getSell_point());
              document.addField("item_price", searchItem.getPrice());
              document.addField("item_image", searchItem.getImage());
              solrClient.add(document);

          }
          taotaoResult = new TaotaoResult(null);
          solrClient.commit();
          return   taotaoResult;
      }catch (Exception e) {
          e.printStackTrace();
          taotaoResult = new TaotaoResult(500, "数据导入发生异常", null);
          return  taotaoResult;
      }

    }
}
