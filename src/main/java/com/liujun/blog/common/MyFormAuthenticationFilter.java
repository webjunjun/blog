package com.liujun.blog.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    // 拒绝访问后处理逻辑
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 前后端分离后，原方法已形同虚设，所以重写该部分的逻辑
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String AUTHORIZATION = "Authorization";
        String accessToken = request.getHeader(AUTHORIZATION);
        ObjectMapper objectMapper = new ObjectMapper();
        String backInfo;
        if (accessToken == null) {
            // 没有token
            backInfo = objectMapper.writeValueAsString(Result.fail("用户未登录"));
        } else {
            // 有 说明token过期
            backInfo = objectMapper.writeValueAsString(Result.fail("用户登录失效"));
        }
        // 解决返回中文乱码问题
        response.setCharacterEncoding("UTF-8"); // 设置HttpServletResponse使用utf-8编码
        response.setHeader("Content-Type", "application/json;charset=utf-8"); // 设置响应头的编码
        response.getWriter().write(backInfo);
        response.getWriter().close();

        // false表示已经处理了，其余拦截器不必处理
        return false;
    }
}
