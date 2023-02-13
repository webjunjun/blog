package com.liujun.blog.mapper;

import com.liujun.blog.entity.User;
import com.liujun.blog.mapper.provider.StudentSqlProvider;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

// 表示这是Mybatis的mapper类 必须要添加这个Mapper注解
@Mapper
public interface UserMapper {

    @Select({
            "SELECT",
            "id, username, real_name, gender, brief, avatar, phone, email, password, status, created, last_login",
            "FROM user",
            "WHERE username = #{username}",
            "AND status != -1"
    })
//    User findUserByUsername(@Param("username") String username);
    User findUserByUsername(String username);

    @SelectProvider(type= StudentSqlProvider.class, method = "selectByAnyColumn")
    User findUserByAny(User user);

    // 创建用户sql
    @Insert({
            "INSERT INTO user",
            "(id, username, password)",
            "VALUES (",
            "#{id}, #{username}, #{password}",
            ")"
    })
    int createUser(User user);

    // 用户登录更新最近登录时间
    @Update({
            "UPDATE user",
            "SET last_login = #{lastLogin}",
            "WHERE id = #{userId}"
    })
    int updateLoginDate(String userId, LocalDateTime lastLogin);

    // 用户注销-软删除sql
    @Update({
            "UPDATE user",
            "SET status = -1, last_login = #{lastLogin}",
            "WHERE id = #{userId}"
    })
    int softDeleteAccount(String userId, LocalDateTime lastLogin);
}
