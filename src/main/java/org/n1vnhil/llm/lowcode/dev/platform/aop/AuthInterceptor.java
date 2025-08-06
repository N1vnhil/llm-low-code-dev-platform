package org.n1vnhil.llm.lowcode.dev.platform.aop;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.n1vnhil.llm.lowcode.dev.platform.constant.UserConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.UserRoleEnum;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.stereotype.Component;
import org.n1vnhil.llm.lowcode.dev.platform.annotation.AuthCheck;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint pjp, AuthCheck authCheck) throws Throwable {
        String requiredRole = authCheck.role();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);

        UserRoleEnum requiredUserRoleEnum = UserRoleEnum.getEnumByRole(requiredRole);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByRole(loginUser.getUserRole());
        if (requiredUserRoleEnum == null) {
            return pjp.proceed();
        }

        if (userRoleEnum == null) {
            throw new BizException(ResponseCodeEnum.NO_AUTH_ERROR);
        }

        if (requiredUserRoleEnum.equals(UserRoleEnum.ADMIN) && !userRoleEnum.equals(UserRoleEnum.ADMIN)) {
            throw new BizException(ResponseCodeEnum.NO_AUTH_ERROR);
        }

        return pjp.proceed();
    }

}
