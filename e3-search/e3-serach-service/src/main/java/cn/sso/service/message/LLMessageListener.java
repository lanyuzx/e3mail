package cn.sso.service.message;

import cn.e3mall.common.pojo.LLSearchItem;
import cn.sso.service.mapper.LLSearchItemMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class LLMessageListener implements MessageListener {
    @Autowired
    private LLSearchItemMapper searchItemMapper;

    final String solrUrl = "http://127.0.0.1:8983/solr/new_core";
    //创建solrClient同时指定超时时间，不指定走默认配置
    HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl)
            .withConnectionTimeout(10000)
            .withSocketTimeout(60000)
            .build();
    @Override
    public void onMessage(Message message) {

        TextMessage textMessage  = (TextMessage) message;
        try {
            //这里停留几秒目的是为了同步事务
            Thread.sleep(1000);
            String text = textMessage.getText();
            Long itemId = Long.parseLong(text);
            LLSearchItem searchItem = searchItemMapper.findSearchItemById(itemId);
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            solrClient.add(document);
            solrClient.commit();
            System.out.println(text);
        }catch (Exception e){

        }
    }
}
