package cn.e3mall.order.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.LLOrderInfo;
import cn.e3mall.order.service.LLOrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import cn.e3mial.common.utils.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class LLOrderServiceImpl implements LLOrderService {
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Value("${ORDER_ID_START_KEY}")
    private String ORDER_ID_START_KEY;
    @Value("${ORDER_ID_START}")
    private  String ORDER_ID_START;
    @Value("${ORDER_ITEM_ID_KEY}")
    private  String ORDER_ITEM_ID_KEY;
    @Override
    public TaotaoResult insertOrder(LLOrderInfo orderInfo) {
        //根据redis生成订单号
        if (!jedisClient.exists(ORDER_ID_START_KEY)) {
            jedisClient.set(ORDER_ID_START_KEY, ORDER_ID_START);
        }
        String orderId = jedisClient.incr(ORDER_ID_START_KEY).toString();

        orderInfo.setOrderId(orderId);
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderMapper.insert(orderInfo);

        //订单明细插入数据
        for (TbOrderItem orderItem : orderInfo.getOrderItems()) {
           String orderItemId = jedisClient.incr(ORDER_ITEM_ID_KEY).toString();
           orderItem.setId(orderItemId);
           orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
        }
        //插入物流信息
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        return TaotaoResult.ok(orderId);
    }
}
