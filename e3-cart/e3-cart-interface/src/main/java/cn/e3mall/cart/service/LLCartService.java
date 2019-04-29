package cn.e3mall.cart.service;

import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface LLCartService {

    //登录状态下保存用户的购物车信息到本地数据库redis
    void  addCartToRedis(long userId,long itemId,int num);
    //合并cooker中的购物车商品到本地数据库
    void mergeCart(List<TbItem> tbItems ,long userId);

    //获取用户添加的购物车列表
    List<TbItem> getCartList(long userId);

    void updateItem(long userId,long itemId , int num);

    void deleteItem(long userId,long itemId);
}
