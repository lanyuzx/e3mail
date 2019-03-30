package cn.search.service.impl;

import cn.e3mall.common.pojo.LLSearchItem;
import cn.e3mall.common.pojo.LLSearchResult;
import cn.e3mial.common.utils.TaotaoResult;
import cn.search.service.LLSearchService;
import cn.search.service.dao.LLSearchDao;
import cn.search.service.mapper.LLSearchItemMapper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LLSearchServiceImpl implements LLSearchService {
    @Autowired
    private LLSearchItemMapper searchItemMapper;

    @Autowired
    private LLSearchDao searchDao;
    @Value("${SORL_URL}")
    private String solrUrl;
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

    @Override
    public LLSearchResult searchItem(String keyWord, int page, int rows) throws Exception {

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(keyWord);
        //设置分页
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);
        solrQuery.set("df", "item_title");
        //设置高亮显示
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");

        LLSearchResult searchResult =searchDao.searchItem(solrQuery);
        //计算总页数
        int recourdCount = searchResult.getRecourdCount();
        int pages = recourdCount / rows;
        if (recourdCount % rows > 0) pages++;
        searchResult.setTotalPages(pages);
        return searchResult;
    }
}
