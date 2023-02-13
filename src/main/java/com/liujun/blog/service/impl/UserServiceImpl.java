package com.liujun.blog.service.impl;

import com.liujun.blog.common.BizException;
import com.liujun.blog.dto.RegisterUserDto;
import com.liujun.blog.entity.User;
import com.liujun.blog.mapper.UserMapper;
import com.liujun.blog.service.UserService;
import com.liujun.blog.utils.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    // 可以省略该注解
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 查询用户信息
     */
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    /**
     * 注册用户
     */
    @Override
    public int addNewUser(RegisterUserDto registerUser) {
        User selectUser = userMapper.findUserByUsername(registerUser.getUsername());
        if (selectUser != null) {
            // 用户名存在不可以注册
             throw new BizException("用户名已存在");
        }
        User newUser = new User();
        registerUser.setPassword(MD5Utils.encrypt32Bit(registerUser.getUsername() + registerUser.getPassword()));
        // 复制DTO对象的值给DO对象
        BeanUtils.copyProperties(registerUser, newUser);
        newUser.setId(UUID.randomUUID().toString());
        // 设置时间 东八区 LocalDateTime.now(ZoneId.of("+8"))
        // LocalDateTime currentTime = LocalDateTime.now();

        return userMapper.createUser(newUser);
    }

    /**
     * 用户注销账号
     * 有bug 未保证userId和发送该请求的token是同一个用户，只是确保了要注销的userId一定是未注销的
     */
    @Override
    public int deleteAccount(String userId) {
        User tempUser = new User();
        tempUser.setId(userId);
        User selectUser = userMapper.findUserByAny(tempUser);
        if (selectUser == null) {
            throw new BizException("用户不存在");
        }
        // 账号退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        // 账号标注注销状态
        return userMapper.softDeleteAccount(userId, LocalDateTime.now());
    }

    @Override
    public void updateLoginDate(String userId) {
        userMapper.updateLoginDate(userId, LocalDateTime.now());
    }
}
