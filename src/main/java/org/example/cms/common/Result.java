package org.example.cms.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "成功";
        r.data = data;
        return r;
    }

    // 修改：返回 Result<T> 而不是 Result<Void>
    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.code = 500;
        r.message = message;
        r.data = null;   // 数据为空
        return r;
    }
}