package org.n1vnhil.llm.lowcode.dev.platform.common;

import lombok.Data;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public Response(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Response(int code, T data) {
        this(code, data, "");
    }

    public Response(ResponseCodeEnum errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

