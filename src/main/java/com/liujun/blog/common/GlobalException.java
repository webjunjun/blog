package com.liujun.blog.common;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

// 全局异常处理类
@RestControllerAdvice
public class GlobalException {
    /**
     * 统一处理自定义业务异常
     */
    @ExceptionHandler(BizException.class)
    public Object bizException(BizException e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 处理所有没有单独处理的异常
     */
    @ExceptionHandler(Exception.class)
    public Object Exception(Exception e) {
        return Result.fail(e.getMessage());
    }

    /**
     * Validation参数效验异常处理器
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object bindException(MethodArgumentNotValidException e) {
        BindingResult results = e.getBindingResult();
        if (results.hasErrors()) {
              List<ObjectError> errors = results.getAllErrors();
              if (!errors.isEmpty()) {
                  ObjectError error = errors.get(0);
                  return Result.fail(error.getDefaultMessage());
              }
        }
        return Result.fail("请求参数校验异常");
    }

    /**
     * 缺少请求体的异常处理 参数解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object bodyMissingException(HttpMessageNotReadableException e) {
        return Result.fail("请求参数不能为空");
    }
}
