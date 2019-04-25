package cn.e3mail.sso.service;

import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLResgistService {

    TaotaoResult checkUser(String param , int type);
    TaotaoResult register(TbUser user);
}
