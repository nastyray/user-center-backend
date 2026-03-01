package com.lilray.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilray.usercenterbackend.model.domain.User;
import com.lilray.usercenterbackend.service.UserService;
import com.lilray.usercenterbackend.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.digester.Digester;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 用户服务实现类
 * @author lilray
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;


    @Override public long userRegister(String useAccount, String userPassword, String checkPassword) {
        //1.校验
        if(StringUtils.isAnyBlank(useAccount,userPassword,checkPassword)){
            return -1 ;
        }

        if(useAccount.length() < 4){
            return -1;
        }

        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }

        // 账户不能包含特殊字符
        if(!useAccount.matches("[a-zA-Z0-9_]+")){
            return -1;
        }

        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        //账户不能重复
        //        long count = this.count(new QueryWrapper<User>().eq(User::getUserAccount,useAccount));
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
            .eq(User::getUserAccount,useAccount);
//        long count = this.count(queryWrapper);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return -1;
        }
        //2.加密
        final String SALT = "lilray";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.存储
        User user = new User();
        user.setUserAccount(useAccount);
        user.setUserPassword(encryptPassword);
        int saveResult = userMapper.insert(user);
        if(saveResult == 0){
            return -1;
        }

        return user.getId();
    }
}




