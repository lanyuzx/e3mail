package cn.e3mail.sso.service.impl;

import cn.e3mail.sso.service.LLLoginService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mial.common.utils.TaotaoResult;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LLLoginServiceImpl implements LLLoginService {
    @Autowired
    private TbUserMapper  userMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_SESSION_EXPIRE}")
    private Integer USER_SESSION_EXPIRE;

    @Override
    public TaotaoResult userLogin(TbUser user) {
        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
       List<TbUser> userList = userMapper.selectByExample(userExample);
       if (userList.isEmpty()) {
           return new TaotaoResult(400, "用户名不存在", false);
       }

       if (StringUtils.isBlank(user.getPassword())) {
           return  new TaotaoResult(400, "密码不能为空", false);
       }
       String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
       if (!md5.equals(userList.get(0).getPassword())){
           return  new TaotaoResult(400, "密码错误", false);
       }
       //生成token
        String token = UUID.randomUUID().toString();
       //保存到redis中
        jedisClient.set("session:" + token,  new Gson().toJson(userList.get(0)));
        jedisClient.expire("session:" + token, USER_SESSION_EXPIRE);

        return new TaotaoResult(200, "登录成功", token);
    }
}
