package com.liujun.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String realName;
    private String gender;
    private String brief;
    private String avatar;
    private String phone;
    private String email;
    private String password;
    private int status;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
}
