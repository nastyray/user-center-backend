package com.lilray.usercenterbackend.service;
import java.util.Date;

import com.lilray.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
     public void testAddUser() {
        User user = new User();
        user.setUsername("lilray");
        user.setUserAccount("123");
        user.setAvatarUrl("123124124");
        user.setGender(0);
        user.setUserPassword("sdfsfs");
        user.setPhone("123");
        user.setEmail("456");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);



    }

    @Test
    void userRegister() {
        long userId = userService.userRegister("lilray1", "12345678", "12345678");
        System.out.println(userId);
    }
}