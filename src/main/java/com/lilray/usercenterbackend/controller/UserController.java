package com.lilray.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lilray.usercenterbackend.model.domain.User;
import com.lilray.usercenterbackend.model.request.UserLoginRequest;
import com.lilray.usercenterbackend.model.request.UserRegisterRequest;
import com.lilray.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lilray.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static com.lilray.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource private UserService userService;

    @PostMapping("/register") public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String useAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(useAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(useAccount, userPassword, checkPassword);

    }

    @PostMapping("/login") public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String useAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(useAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(useAccount, userPassword,request);

    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request){
        User currentUser = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null){
            return null;
        }
        //获取最新的用户信息，比如用户积分等
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        //脱敏
        return userService.getSafetyUser(user);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String UserName,HttpServletRequest request){
        //1.仅管理员可查询
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(UserName)){{
            userLambdaQueryWrapper.like(User::getUsername,UserName);
        }}
        List<User> userList = userService.list(userLambdaQueryWrapper);
        return userList.stream().map(userService::getSafetyUser).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){

        //1.仅管理员可查询
        if (!isAdmin(request)){
            return false;
        }

        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否是管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
