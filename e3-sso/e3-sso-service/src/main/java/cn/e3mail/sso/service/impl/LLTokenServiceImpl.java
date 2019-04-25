package cn.e3mail.sso.service.impl;

import cn.e3mail.sso.service.LLTokenService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.pojo.TbUser;
import cn.e3mial.common.utils.TaotaoResult;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LLTokenServiceImpl implements LLTokenService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_SESSION_EXPIRE}")
    private Integer USER_SESSION_EXPIRE;
    @Override
    public TaotaoResult getUserByToken(String token) {
       String userJosn = jedisClient.get("session:" + token);
       if (StringUtils.isBlank(userJosn)) {
           return  new TaotaoResult(201, "登录过期", false);
       }
       //刷新过期时间
        jedisClient.expire("session:" + token,USER_SESSION_EXPIRE);
        return new TaotaoResult(200, "获取成功", new Gson().fromJson(userJosn, TbUser.class));
    }
}
