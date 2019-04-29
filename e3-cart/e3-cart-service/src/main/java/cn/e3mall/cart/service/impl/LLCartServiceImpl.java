package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.LLCartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class LLCartServiceImpl implements LLCartService {
    @Autowired
    private  JedisClient jedisClient;
    @Value("${REDIS_CART_PRE}")
    private  String  REDIS_CART_PRE;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Override
    public void addCartToRedis(long userId, long itemId, int num) {
       String redisCartKey  =  REDIS_CART_PRE + userId;
        //先从本地数据库中差寻该条商品存不存在
        boolean  hexists = jedisClient.hexists(redisCartKey, itemId + "");
        if (hexists) {
            String json = jedisClient.hget(redisCartKey, itemId + "");
            Type type = new TypeToken<TbItem>(){}.getType();
            TbItem tbItem = new Gson().fromJson(json, type);
            tbItem.setNum(tbItem.getNum() + num);
            jedisClient.hset(redisCartKey, itemId + "",  new Gson().toJson(tbItem));
        }

        //不存在 就调用查询
       TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        tbItem.setNum(num);
        if (StringUtils.isNotBlank(tbItem.getImage())) {
            tbItem.setImage(tbItem.getImage().split(",")[0]);
        }
        jedisClient.hset(redisCartKey, itemId + "",  new Gson().toJson(tbItem));

    }

    @Override
    public void mergeCart(List<TbItem> tbItems,long userId) {
       for (TbItem tbItem : tbItems) {
           addCartToRedis(userId, tbItem.getId(), tbItem.getNum());
       }
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        String redisCartKey  =  REDIS_CART_PRE + userId;
       List<String> jsonList =  jedisClient.hvals(redisCartKey);
       List<TbItem> tbItemList = new ArrayList<>();
      for (String json : jsonList) {
          Type type = new TypeToken<TbItem>(){}.getType();
          tbItemList.add(new Gson().fromJson(json, type));
      }
        return tbItemList;
    }

    @Override
    public void updateItem(long userId, long itemId, int num) {
        String redisCartKey  =  REDIS_CART_PRE + userId;
      String json = jedisClient.hget(redisCartKey,  itemId + "");
      if (StringUtils.isNotBlank(json)) {
          Type type = new TypeToken<TbItem>(){}.getType();
          TbItem tbItem = new Gson().fromJson(json, type);
          tbItem.setNum(num < 0 ? 0 : num);
          jedisClient.hset(redisCartKey, itemId + "", new Gson().toJson(tbItem));
      }
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        String redisCartKey  =  REDIS_CART_PRE + userId;
        jedisClient.hdel(redisCartKey,itemId + "");
    }

    @Override
    public void clearCart(long userId) {
        String redisCartKey  =  REDIS_CART_PRE + userId;
        jedisClient.del(redisCartKey);
    }
}
