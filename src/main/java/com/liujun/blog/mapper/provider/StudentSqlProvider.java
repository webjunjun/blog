package com.liujun.blog.mapper.provider;

import com.liujun.blog.entity.User;
import org.apache.ibatis.jdbc.SQL;

public class StudentSqlProvider {
    public String selectByAnyColumn(User user) {
        return new SQL() {
            {
                SELECT("id, username, real_name, gender, brief, avatar, email, " +
                        "phone, password, status, created, last_login");
                FROM("user");
                if (user.getId() != null) {
                    WHERE("id = #{id}");
                }
                if (user.getUsername() != null) {
                    WHERE("username = #{username}");
                }
                if (user.getRealName() != null) {
                    WHERE("real_name = #{realName}");
                }
                if (user.getEmail() != null) {
                    WHERE("email = #{email}");
                }
                if (user.getPhone() != null) {
                    WHERE("phone = #{phone}");
                }
                WHERE("status != -1");
            }
        }.toString();
    }
}
