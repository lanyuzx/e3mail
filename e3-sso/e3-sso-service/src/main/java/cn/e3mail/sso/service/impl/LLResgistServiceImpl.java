package cn.e3mail.sso.service.impl;

import cn.e3mail.sso.service.LLResgistService;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mial.common.utils.TaotaoResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class LLResgistServiceImpl implements LLResgistService {
    @Autowired
    private TbUserMapper userMapper;
    @Override
    public TaotaoResult checkUser(String param, int type) {
        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        if ( type == 1) {
            criteria.andUsernameEqualTo(param);
        }else  if (type == 2) {
            criteria.andPhoneEqualTo(param);

        }else  if (type == 3) {
            criteria.andEmailEqualTo(param);
        }else  {
            return new TaotaoResult(400, "数据类型错误", false);
        }
       List<TbUser> userList = userMapper.selectByExample(userExample);
        if (userList!=null && !userList.isEmpty()) {
            return new TaotaoResult(false);
        }

        return new TaotaoResult(true);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        if ( StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone()) ) {
            return new TaotaoResult(400, "用户数据不完整,注册失败", false);
        }
        TaotaoResult result = checkUser(user.getUsername(), 1);
        if (!(Boolean) result.getData()) {
            return new TaotaoResult(400, "用户名被占用", false);
        }
        result = checkUser(user.getPhone(), 2);
        if (!(Boolean) result.getData()) {
            return new TaotaoResult(400, "手机号被占用", false);
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
       //MD5密码加密
       String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
       user.setPassword(md5);
        userMapper.insert(user);
        return new TaotaoResult(200, "注册成功", true);
    }
}
