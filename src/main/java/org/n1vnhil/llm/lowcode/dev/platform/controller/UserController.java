package org.n1vnhil.llm.lowcode.dev.platform.controller;

import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.n1vnhil.llm.lowcode.dev.platform.annotation.AuthCheck;
import org.n1vnhil.llm.lowcode.dev.platform.common.DeleteRequest;
import org.n1vnhil.llm.lowcode.dev.platform.common.Response;
import org.n1vnhil.llm.lowcode.dev.platform.common.ResponseUtils;
import org.n1vnhil.llm.lowcode.dev.platform.constant.UserConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ThrowUtils;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.user.*;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.user.LoginUserVO;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.user.UserVO;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author zhiheng
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Response<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, password, checkPassword);
        return ResponseUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest 登录请求参数
     * @param request 用户请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public Response<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        String account = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        LoginUserVO loginUserVO = userService.userLogin(account, password, request);
        return ResponseUtils.success(loginUserVO);
    }

    /**
     * 获取用户登录信息
     * @param request 用户请求
     * @return 当前用户信息
     */
    @GetMapping("/get/login")
    public Response<LoginUserVO> getUserInfo(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResponseUtils.success(userService.getLoginUserVO(user));
    }

    @PostMapping("/logout")
    public Response<Boolean> logout(HttpServletRequest request) {
        boolean result = userService.logout(request);
        return ResponseUtils.success(result);
    }

    /**
     * 保存用户。
     *
     * @param userAddRequest 添加用户请求
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/add")
    public Response<Long> add(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        final String DEFAULT_PASSWORD = "12345678";
        user.setUserPassword(userService.getEncryptPassword(DEFAULT_PASSWORD));
        boolean result =  userService.save(user);
        ThrowUtils.throwIf(!result, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(user.getId());
    }

    /**
     * 根据主键删除用户。
     *
     * @param deleteRequest 删除用户请求
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/remove")
    public Response<Boolean> remove(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() < 0) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR);
        }
        boolean success = userService.removeById(deleteRequest.getId());
        return ResponseUtils.success(success);
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 更新用户请求
     * @return 更新结果
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/update")
    public Response<Boolean> updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result =  userService.updateById(user);
        return ResponseUtils.success(result);
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户id
     * @return 用户详情
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @GetMapping("/get/{id}")
    public Response<User> getUserById(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id < 0, ResponseCodeEnum.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(user);
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户id
     * @return 用户详情
     */
    @GetMapping("/get/vo/{id}")
    public Response<UserVO> getUserVOById(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id < 0, ResponseCodeEnum.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ResponseCodeEnum.OPERATION_ERROR);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResponseUtils.success(userVO);
    }

    /**
     * 分页查询用户。
     *
     * @param userQueryRequest 分页查询请求
     * @return 分页对象
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/vo")
    public Response<Page<UserVO>> page(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(pageNum, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResponseUtils.success(userVOPage);
    }

}
