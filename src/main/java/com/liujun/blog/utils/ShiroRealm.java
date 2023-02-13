package com.liujun.blog.utils;

import com.liujun.blog.entity.User;
import com.liujun.blog.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.realm.AuthenticatingRealm;
// import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthenticatingRealm {
    @Autowired
    UserService userService;

    // doGetAuthorizationInfo方法：授权 请求其余接口会走这里拿请求头里的token，查询用户信息，找到对应的角色和权限
    // doGetAuthenticationInfo方法：用户身份认证，登录会调用这个方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        // 将登录接口传进来的token装换成UsernamePasswordToken类型
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        String username = token.getUsername();
        // 从数据库中查询该用户
        User currentUser = userService.findUserByUsername(username);
        // 认证信息 默认是null
        SimpleAuthenticationInfo authInfo;

        if(currentUser != null) {
            // 用户存在
            String principal = currentUser.getUsername();
            String credentials = currentUser.getPassword();

            // 更新用户最后登录时间
            userService.updateLoginDate(currentUser.getId());

            // 获取盐值，即密码
            // ByteSource salt = ByteSource.Util.bytes(credentials);
            String realmName = this.getName();

            // 将账户名、密码、盐值、realmName实例化交给Shiro来管理，匿名用户实例关联具体用户成功
            // authInfo = new SimpleAuthenticationInfo(principal, credentials, salt, realmName);
            authInfo = new SimpleAuthenticationInfo(principal, credentials, realmName);
        } else {
            // 抛出异常
            return null;
        }
        return authInfo;
    }
}
