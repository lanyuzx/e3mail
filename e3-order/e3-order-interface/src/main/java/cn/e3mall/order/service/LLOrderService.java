package cn.e3mall.order.service;

import cn.e3mall.order.pojo.LLOrderInfo;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLOrderService {

    TaotaoResult insertOrder(LLOrderInfo orderInfo);
}
