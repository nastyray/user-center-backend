package com.lilray.usercenterbackend.service;

import com.lilray.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2026-02-27 17:35:05
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param useAccount    用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @param planetCode 星球编码
     * @return 用户id
     */
    long userRegister(String useAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户登录
     *
     * @param useAccount   用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String useAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
