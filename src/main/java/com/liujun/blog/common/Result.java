package com.liujun.blog.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 统一返回数据格式
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    // 返回编码 1 成功，-1 失败
    private int code;
    // 返回编码描述
    private String msg;
    // 返回业务数据
    private T data;
    // 返回数据的时间戳 利用注解返回时间格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp timestamp;

    public Result() {
        this.timestamp = new Timestamp(new Date().getTime());
    }

    /**
     * 成功
     */
    public static <T> Result<T> success(T data) {
        Result<T> resultData = new Result<>();
        resultData.setCode(1);
        resultData.setMsg("操作成功");
        resultData.setData(data);
        return resultData;
    }

    /**
     * 失败
     */
    public static <T> Result<T> fail(String message) {
        Result<T> resultData = new Result<>();
        resultData.setCode(-1);
        resultData.setMsg(message);
        return resultData;
    }
}
