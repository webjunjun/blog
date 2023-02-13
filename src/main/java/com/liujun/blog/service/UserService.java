package com.liujun.blog.service;

import com.liujun.blog.dto.RegisterUserDto;
import com.liujun.blog.entity.User;

public interface UserService {
    // 根据用户名查询用户信息
    User findUserByUsername(String username);

    // 创建用户
    int addNewUser(RegisterUserDto user);

    // 注销用户
    int deleteAccount(String userId);

    // 更新用户最近登录时间
    void updateLoginDate(String userId);
}
