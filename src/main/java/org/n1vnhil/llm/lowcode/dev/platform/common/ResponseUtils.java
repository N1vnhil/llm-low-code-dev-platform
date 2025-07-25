package org.n1vnhil.llm.lowcode.dev.platform.common;

import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;

public class ResponseUtils {

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static Response<?> error(ResponseCodeEnum errorCode) {
        return new Response<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param message 错误信息
     * @return 响应
     */
    public static Response<?> error(int code, String message) {
        return new Response<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @return 响应
     */
    public static Response<?> error(ResponseCodeEnum errorCode, String message) {
        return new Response<>(errorCode.getCode(), null, message);
    }
}

