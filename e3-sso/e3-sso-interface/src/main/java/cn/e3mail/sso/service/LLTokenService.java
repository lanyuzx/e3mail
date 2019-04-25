package cn.e3mail.sso.service;

import cn.e3mial.common.utils.TaotaoResult;

public interface LLTokenService {
    TaotaoResult getUserByToken(String token);
}
