package com.lilray.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilray.usercenterbackend.model.domain.User;
import com.lilray.usercenterbackend.service.UserService;
import com.lilray.usercenterbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.lilray.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author lilray
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    /**\
     * 盐值混淆密码
     */
    private static final String SALT = "lilray";




    @Override
    public long userRegister(String useAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验
        if (StringUtils.isAnyBlank(useAccount, userPassword, checkPassword,planetCode)) {
            //TODO 修改为自定义异常
            return -1;
        }

        if (useAccount.length() < 4) {
            return -1;
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        if (planetCode.length() > 5 ) {
            return -1;
        }

        // 账户不能包含特殊字符
        if (!useAccount.matches("[a-zA-Z0-9_]+")) {
            return -1;
        }

        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //账户不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>().eq(User::getUserAccount, useAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 星球编码不能重复
        queryWrapper = new LambdaQueryWrapper<User>().eq(User::getPlanetCode, planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.存储
        User user = new User();
        user.setUserAccount(useAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        int saveResult = userMapper.insert(user);
        if (saveResult == 0) {
            return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String useAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(useAccount, userPassword)){
            //TODO 修改为自定义异常
            return null;
        }

        if (useAccount.length() < 4) {
            return null;
        }

        if (userPassword.length() < 8) {
            return null;
        }

        // 账户不能包含特殊字符
        if (!useAccount.matches("[a-zA-Z0-9_]+")) {
            return null;
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.查询用户是否存在
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount, useAccount);
        userLambdaQueryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            log.info("用户登录失败，用户名或密码错误");
            return null;
        }
        //3.脱敏用户信息


        //4.记录用户的登录状态
//        String id = request.getSession().getId();
//        System.out.println(id);
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 脱敏用户信息
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

}




