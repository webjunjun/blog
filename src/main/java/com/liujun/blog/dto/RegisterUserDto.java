package com.liujun.blog.dto;

import com.liujun.blog.utils.RegexPatterns;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class RegisterUserDto {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 18, message = "密码长度应不少于8位且不超过18位")
    @Pattern(regexp = RegexPatterns.PASSWORD_REGEX, message = "密码由8~18位的字母、数字、下划线组成")
    private String password;
}
