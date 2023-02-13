package com.liujun.blog.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class BaseResponse implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 是否支持advice功能
     * true 支持 则将对返回数据进行自定义，false 不支持 则使用系统默认的返回数据返回
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 对返回的数据进行处理
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 返回数据是字符串 直接返回
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        // 返回的数据已被Result处理过(针对异常的) 直接返回
        // 因为实现了自定义的ResponseBodyAdvice，异常最终也会走到这里处理
        // 所以全局处理过的异常在这里就不在再处理一遍了，直接返回就可以
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
