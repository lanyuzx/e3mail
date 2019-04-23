package cn.e3mail.service.impl;

import cn.e3mail.service.LLItemService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.LLPageCommon;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mial.common.utils.IDUtils;
import cn.e3mial.common.utils.TaotaoResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class LLItemServiceImpl implements LLItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;

    @Autowired
    private JedisClient jedisClient;
    //缓存详情的key
    @Value("${Item_Product_Detail_KEY}")
    private String  Item_Product_Detail_KEY;
   //过期时间
    @Value("${Item_Expiration_Time}")
    private  Integer Item_Expiration_Time;

    //详情缓存前缀
    @Value("${Item_Info}")
    private  String Item_Info;

    @Value("${Item_Info_Desc}")
    private  String Item_Info_Desc;


    @Override
    public LLPageCommon<TbItem> getGridResult(Integer row, Integer pagesize) {
        PageHelper.startPage(row,pagesize);
        TbItemExample itemExample = new TbItemExample();
        List <TbItem> itemList =  itemMapper.selectByExample(itemExample);
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);
        LLPageCommon pageCommon = new LLPageCommon();
        pageCommon.setRows(itemList);
        pageCommon.setTotal(pageInfo.getTotal());
        return pageCommon;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        //补全数据
        //生成产品的id
      final long itemId =  IDUtils.genItemId();
       item.setId(itemId);
        //生成创建时间
        Date createdData =  new Date();
        item.setCreated(createdData);
       //生成更新时间
        Date updatedData =  new Date();
        item.setUpdated(updatedData);
       //生成状态
        item.setStatus((byte) 1);
        //商品描述
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(createdData);
        itemDesc.setUpdated(updatedData);
        //插入数据

        //先插入描述数据
       int result =   itemDescMapper.insert(itemDesc);
        System.out .println("插入结果 === " + result);
        itemMapper.insert(item);

        //发送消息 同步索引库
        jmsTemplate.send(topicDestination,new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = (Message) session.createTextMessage(itemId + "");
                return message;
            }
        });
        //创建返回结果pojo
        TaotaoResult taotaoResult = new TaotaoResult(item);
        return taotaoResult;
    }

    @Override
    public TaotaoResult deleteItem(long id) {
        //先删除产品描述
        itemDescMapper.deleteByPrimaryKey(id);
        //在删除商品
        itemMapper.deleteByPrimaryKey(id);
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public TaotaoResult findItemDescByIdToJson(long id) {
       TbItemDesc itemDesc =  itemDescMapper.selectByPrimaryKey(id);
       //String itemDescJson  = new Gson().toJson(itemDesc);
       TaotaoResult taotaoResult = new TaotaoResult(itemDesc);
        return taotaoResult;
    }

    @Override
    public TaotaoResult findItemByIdToJson(long id) {
       TbItem item =  itemMapper.selectByPrimaryKey(id);
      // String itemJson = new Gson().toJson(item);
       TaotaoResult taotaoResult = new TaotaoResult(item);
        return taotaoResult;
    }

    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {

        return null;
    }

    @Override
    public TaotaoResult instockItem(long id) {
        itemMapper.updateStateByPrimaryKey(id, 2,new Date());
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public TaotaoResult reshelfItem(long id) {
        itemMapper.updateStateByPrimaryKey(id, 1,new Date());
        TaotaoResult taotaoResult = new TaotaoResult(null);
        return taotaoResult;
    }

    @Override
    public TbItem getItemById(long itemId) {
        //取出缓存
     String json = jedisClient.get(Item_Info + ":" + itemId + ":BASE");
     if (StringUtils.isNotBlank(json)) {
         return  new Gson().fromJson(json, TbItem.class);
     }
     TbItem tbItem =  itemMapper.selectByPrimaryKey(itemId);
     //添加缓存
        jedisClient.set(Item_Info + ":" + itemId + ":BASE", new Gson().toJson(tbItem));
        //设置过期时间为一个小时
        jedisClient.expire(Item_Info + ":" + itemId + ":BASE", Item_Expiration_Time);
        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //取出缓存
        String json = jedisClient.get(Item_Info_Desc + ":" + itemId + ":BASE");
        if (StringUtils.isNotBlank(json)) {
            return  new Gson().fromJson(json, TbItemDesc.class);
        }
        TbItemDesc tbItem =  itemDescMapper.selectByPrimaryKey(itemId);
        //添加缓存
        jedisClient.set(Item_Info_Desc + ":" + itemId + ":BASE", new Gson().toJson(tbItem));
        //设置过期时间为一个小时
        jedisClient.expire(Item_Info_Desc + ":" + itemId + ":BASE", Item_Expiration_Time);
        return tbItem;
    }




}
