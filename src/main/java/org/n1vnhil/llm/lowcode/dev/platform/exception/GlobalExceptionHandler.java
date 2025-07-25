package org.n1vnhil.llm.lowcode.dev.platform.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.n1vnhil.llm.lowcode.dev.platform.common.Response;
import org.n1vnhil.llm.lowcode.dev.platform.common.ResponseUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Response<?> businessExceptionHandler(BizException e) {
        log.error("BusinessException", e);
        return ResponseUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResponseUtils.error(ResponseCodeEnum.SYSTEM_ERROR, "系统错误");
    }
}

