package cn.e3mail.sso.service;

import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.TaotaoResult;

public interface LLLoginService {
    TaotaoResult userLogin(TbUser user);
}
