package com.liujun.blog.controller;

import com.liujun.blog.common.BizException;
import com.liujun.blog.dto.RegisterUserDto;
import com.liujun.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;

@RestController()
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    // 可以省略该注解
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 注册用户
    @PostMapping("/register")
    public void addUser(@Valid @RequestBody RegisterUserDto user) {
        userService.addNewUser(user);
    }

    // 用户登录
    @PostMapping("/login")
    public Serializable loginUser(@Valid @RequestBody RegisterUserDto user) {
        String username = user.getUsername();
        String password = user.getUsername() + user.getPassword();
        // 获取匿名的用户实例
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            // 将身份信息传给匿名实例，成功则登录成功
            subject.login(usernamePasswordToken);
            // shiro是通过session管理会话，所以将sessionId作为token返回
            return subject.getSession().getId();
        } catch (Exception e) {
            throw new BizException("用户名或密码错误");
        }
    }

    // 退出登录
    @PostMapping("/logout")
    public void logoutUser() {
        // 获取用户实例(因为是登录过的，所以会从请求头拿该用户token，获取到用户信息)
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        } else {
            throw new BizException("用户不存在");
        }
    }

    // 账号注销
    @PostMapping("/delete")
    public void deleteUser(@RequestParam String userId) {
        userService.deleteAccount(userId);
    }
}
