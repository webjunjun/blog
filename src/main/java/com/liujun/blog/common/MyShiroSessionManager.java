package com.liujun.blog.common;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class MyShiroSessionManager extends DefaultWebSessionManager {
    public MyShiroSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String AUTHORIZATION = "Authorization";
        String SESSION_ID_SOURCE = "Stateless request";
        // 获取请求头中的token的值
        String token = WebUtils.toHttp(request).getHeader(AUTHORIZATION);

        // 禁止在url上拼接sessionId
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, Boolean.FALSE);
        if (token == null) {
            // 请求头中没有token 返回空
            return null;
        } else {
            // 设置session来源 暂不清楚作用
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, SESSION_ID_SOURCE);
            // 请求头中如果有token 将token的值赋值给sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            // 将sessionId设为有效
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        }
    }
}
