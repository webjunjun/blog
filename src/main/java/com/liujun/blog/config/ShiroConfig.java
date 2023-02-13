package com.liujun.blog.config;

import com.liujun.blog.common.MyFormAuthenticationFilter;
import com.liujun.blog.common.MyShiroSessionManager;
import com.liujun.blog.utils.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// 该注解类似于spring的配置文件
@Configuration
public class ShiroConfig {
    @Value("${spring.redis.host}")
    String redisHost;

    @Value("${spring.redis.port}")
    String redisPort;

    /**
     * ShiroFilterFactoryBean会拦截前端请求
     * 交给SecurityManager
     * 再交给MyRealm进行认证和授权处理
     */
    // 过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 给ShiroFilter配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 给ShiroFilter配置自定义过滤器
        Map<String, Filter> myFilters = new LinkedHashMap<>();
        myFilters.put("authc", new MyFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(myFilters);

        /*
        anon: 无需认证即可访问
        authc: 必须认证才能用
        user: 必须拥有 “记住我” 功能才能用
        perms: 拥有对某个资源的权限才能用
        role: 拥有某个角色权限才能访问
        */
        Map<String,String> interceptMap = new HashMap<>();

        // 配置不会被拦截的链接
        interceptMap.put("/users/register", "anon");
        interceptMap.put("/users/login", "anon");

        // 剩余请求需要身份认证 拦截所有请求的通配符必须放到最后
        interceptMap.put("/**", "authc");
        // 认证失败会重定向到这里 前后端分离项目不建议使用这个
        // shiroFilterFactoryBean.setLoginUrl("/shiro/un-login");

        // 将拦截器链设置到shiro中
        shiroFilterFactoryBean.setFilterChainDefinitionMap(interceptMap);

        return shiroFilterFactoryBean;
    }

    // 安全管理器
    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联自定义realm对象
        securityManager.setRealm(shiroRealm);
        // 自定义会话管理
        securityManager.setSessionManager(getMyShiroSessionManager());
        // 自定义缓存管理 使用redis
        securityManager.setCacheManager(getRedisCacheManager());
        return securityManager;
    }

    // 创建realm对象，需要自定义类继承AuthenticatingRealm抽象类
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        // 将md5密码比对器传给realm
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    // 密码比对即对密码进行编码 因为数据库的密码是加密过的
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 指定加密方式
        credentialsMatcher.setHashAlgorithmName("MD5");
        // 加密次数 默认1次 其实就是用md5加密几次
        credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

    // shiro-redis插件RedisManager
    public RedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
        // 设置超时时间
        redisManager.setTimeout(1800);
        return redisManager;
    }

    // 将Session存储到redis中 因为要更改默认的存储前缀，所以把它注入到spring容器
    @Bean
    public RedisSessionDAO getRedisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(getRedisManager());
        // 设置session前缀
        redisSessionDAO.setKeyPrefix("SPRINGBOOT_SESSION:");
        // 设置session过期时间 半小时
        redisSessionDAO.setExpire(1800);
        return redisSessionDAO;
    }

    // 自定义会话管理器，作用是在创建会话时，将token的值赋给会话的sessionId
    public MyShiroSessionManager getMyShiroSessionManager() {
        MyShiroSessionManager sessionManager = new MyShiroSessionManager();
        sessionManager.setSessionDAO(getRedisSessionDAO());
        return sessionManager;
    }

    // 将认证信息以及权限信息存储到redis中
    public RedisCacheManager getRedisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());
        // 设置前缀
        redisCacheManager.setKeyPrefix("SPRINGBOOT_CACHE:");
        return redisCacheManager;
    }
}
