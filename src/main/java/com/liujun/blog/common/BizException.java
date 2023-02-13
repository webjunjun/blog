package com.liujun.blog.common;

import lombok.Data;

// 自定义异常基类/自定义业务异常类
@Data
public class BizException extends RuntimeException {
    private String message;

    public BizException(String message) {
        super();
        this.message = message;
    }
}
