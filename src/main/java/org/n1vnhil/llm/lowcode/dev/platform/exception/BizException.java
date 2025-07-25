package org.n1vnhil.llm.lowcode.dev.platform.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ResponseCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BizException(ResponseCodeEnum errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}

