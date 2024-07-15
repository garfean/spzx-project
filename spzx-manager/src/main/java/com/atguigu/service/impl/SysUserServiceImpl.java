package com.atguigu.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.exception.HuiGeException;
import com.atguigu.mapper.SysUserMapper;
import com.atguigu.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper ;

    @Autowired
    private RedisTemplate<String , String> redisTemplate ;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //获取用户输入的验证码
        String captcha = loginDto.getCaptcha();//用户输入的验证码
        //获取key
        String codeKey = loginDto.getCodeKey();//获取redis中的验证码
        //在redis里面查询key
        String redisCode = redisTemplate.opsForValue().get("user:login:validatecode:" + codeKey);
        //判断是否一致，否就报异常
        if (StrUtil.isEmpty(redisCode)||StrUtil.equalsAnyIgnoreCase(captcha,redisCode)){
            throw new HuiGeException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //是的话，删除校验码
        redisTemplate.delete(codeKey);




        // 根据用户名查询用户
        SysUser sysUser = sysUserMapper.selectByUserName(loginDto.getUserName());
        if(sysUser == null) {
            throw new HuiGeException(ResultCodeEnum.LOGIN_ERROR );
        }

        // 验证密码是否正确
        String inputPassword = loginDto.getPassword();
        String md5InputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if(!md5InputPassword.equals(sysUser.getPassword())) {
            throw new HuiGeException(ResultCodeEnum.LOGIN_ERROR) ;
        }

        // 生成令牌，保存数据到Redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("user:login:" + token , JSON.toJSONString(sysUser) , 30 , TimeUnit.MINUTES);

        // 构建响应结果对象
        LoginVo loginVo = new LoginVo() ;
        loginVo.setToken(token);
        loginVo.setRefresh_token("");

        // 返回
        return loginVo;
    }
}