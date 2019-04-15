package cn.e3mall.controller;

import cn.e3mall.pojo.TbItem;
import cn.e3mial.common.utils.TaotaoResult;
import cn.search.service.LLSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Controller
public class LLSearchItemController {
    @Autowired
    private LLSearchService searchService;
//    @Autowired
//    private JmsTemplate jmsTemplate;
//    @Resource
//    private Destination topicDestination;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public TaotaoResult importAllSearchItem() {
       TaotaoResult taotaoResult = searchService.importAllSerachItems();
//      TbItem tbItem = (TbItem) taotaoResult.getData();
//        //发送消息 同步索引库
//        jmsTemplate.send(topicDestination,new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                Message message = (Message) session.createTextMessage(tbItem.getId() + "");
//                return message;
//            }
//        });
       return  taotaoResult;
    }
}
