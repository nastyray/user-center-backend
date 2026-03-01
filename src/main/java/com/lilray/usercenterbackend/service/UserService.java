package com.lilray.usercenterbackend.service;

import com.lilray.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Lenovo
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-02-27 17:35:05
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param useAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword  确认密码
     * @return 用户id
     */
    long userRegister(String useAccount, String userPassword, String checkPassword);
}
