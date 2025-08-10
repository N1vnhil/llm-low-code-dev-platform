package org.n1vnhil.llm.lowcode.dev.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.n1vnhil.llm.lowcode.dev.platform.annotation.AuthCheck;
import org.n1vnhil.llm.lowcode.dev.platform.common.DeleteRequest;
import org.n1vnhil.llm.lowcode.dev.platform.common.Response;
import org.n1vnhil.llm.lowcode.dev.platform.common.ResponseUtils;
import org.n1vnhil.llm.lowcode.dev.platform.constant.AppConstant;
import org.n1vnhil.llm.lowcode.dev.platform.constant.UserConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ThrowUtils;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.*;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.UserRoleEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.app.AppVO;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.App;
import org.n1vnhil.llm.lowcode.dev.platform.service.AppService;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用 控制层。
 *
 * @author zhiheng
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    @Resource
    private UserService userService;

    /**
     * 用户新建应用
     *
     * @param appAddRequest 应用
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("/add")
    public Response<Long> addApp(@RequestBody AppAddRequest appAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appAddRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        String initPrompt = appAddRequest.getInitPrompt();
        ThrowUtils.throwIf(StrUtil.isBlank(initPrompt), ResponseCodeEnum.PARAMS_ERROR, "初始化提示词不能为空");

        User loginUser = userService.getLoginUser(request);

        App app = App.builder()
                .initPrompt(initPrompt)
                .userId(loginUser.getId())
                .codeGenType(CodeGenerationType.MULTI_FILE.getValue())
                .appName(initPrompt.substring(0, Math.min(initPrompt.length(), 12)))
                .build();

        boolean success = appService.save(app);
        ThrowUtils.throwIf(!success, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(app.getId());
    }

    /**
     * 用户删除应用
     *
     * @param deleteRequest 删除请求
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PostMapping("/user/delete")
    public Response<Boolean> userDeleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用id无效");
        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.NOT_FOUND_ERROR, "应用不存在");
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BizException(ResponseCodeEnum.NO_AUTH_ERROR);
        }

        boolean success = appService.removeById(id);
        ThrowUtils.throwIf(!success, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(success);
    }

    /**
     * 用户更新应用
     *
     * @param appUpdateRequest 更新请求
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("/user/update")
    public Response<Boolean> userUpdate(@RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appUpdateRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        Long id = appUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用id无效");

        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.NOT_FOUND_ERROR, "应用不存在");

        User loginUser = userService.getLoginUser(request);
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BizException(ResponseCodeEnum.NO_AUTH_ERROR);
        }

        App newApp = new App();
        BeanUtils.copyProperties(appUpdateRequest, newApp);
        newApp.setEditTime(LocalDateTime.now());
        boolean success = appService.updateById(newApp);
        ThrowUtils.throwIf(!success, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(success);
    }

    /**
     * 查询应用详情
     *
     * @param id 应用主键
     * @return 应用详情
     */
    @GetMapping("/user/get/{id}")
    public Response<AppVO> getInfo(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用id无效");

        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.NOT_FOUND_ERROR, "应用不存在");

        return ResponseUtils.success(appService.getAppVO(app));
    }

    /**
     * 查询当前用户应用列表
     * @param appQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list")
    public Response<Page<AppVO>> listMyAppVOByPage(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appQueryRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        long pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ResponseCodeEnum.PARAMS_ERROR, "每页最多查询20个应用");

        User loginUser = userService.getLoginUser(request);
        appQueryRequest.setUserId(loginUser.getId());
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);

        long pageNum = appQueryRequest.getPageNum();
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOS = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOS);
        return ResponseUtils.success(appVOPage);
    }

    /**
     * 查询精选应用列表
     * @param appQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/good/list")
    public Response<Page<AppVO>> listGoodAppVOByPage(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appQueryRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        long pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ResponseCodeEnum.PARAMS_ERROR, "每页最多查询20个应用");

        appQueryRequest.setPriority(AppConstant.GOOD_APP_PRIORITY);

        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        long pageNum = appQueryRequest.getPageNum();
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOS = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOS);
        return ResponseUtils.success(appVOPage);
    }


    /**
     * 管理员删除应用
     *
     * @param deleteRequest 删除请求
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/admin/delete")
    public Response<Boolean> adminDeleteApp(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用id无效");
        Long id = deleteRequest.getId();
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.NOT_FOUND_ERROR, "应用不存在");

        boolean success = appService.removeById(id);
        ThrowUtils.throwIf(!success, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(success);
    }

    /**
     * 管理员更新应用
     *
     * @param appAdminUpdateRequest 更新请求
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/admin/update")
    public Response<Boolean> adminUpdate(@RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        ThrowUtils.throwIf(appAdminUpdateRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        Long id = appAdminUpdateRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用id无效");

        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.NOT_FOUND_ERROR, "应用不存在");

        App newApp = new App();
        BeanUtils.copyProperties(appAdminUpdateRequest, newApp);
        newApp.setEditTime(LocalDateTime.now());
        boolean success = appService.updateById(newApp);
        ThrowUtils.throwIf(!success, ResponseCodeEnum.OPERATION_ERROR);
        return ResponseUtils.success(success);
    }

    /**
     * 管理员查询应用列表
     * @param appQueryRequest
     * @return
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @PostMapping("/admin/list")
    public Response<Page<AppVO>> adminListAppVOByPage(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        long pageSize = appQueryRequest.getPageSize();
        long pageNum = appQueryRequest.getPageNum();
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);

        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOS = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOS);
        return ResponseUtils.success(appVOPage);
    }

    /**
     * 管理员查询应用详情
     *
     * @param id 应用主键
     * @return 应用详情
     */
    @AuthCheck(role = UserConstant.ADMIN_ROLE)
    @GetMapping("/admin/get/{id}")
    public Response<AppVO> adminGetInfo(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用id无效");

        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.NOT_FOUND_ERROR, "应用不存在");

        return ResponseUtils.success(appService.getAppVO(app));
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatToCode(@RequestParam("appId") Long appId, @RequestParam("message") String message, HttpServletRequest request) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用ID无效");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ResponseCodeEnum.PARAMS_ERROR, "用户消息不能为空");
        User loginUser = userService.getLoginUser(request);
        return appService.chatToGenCode(appId, message, loginUser);
    }

    @PostMapping("/deploy")
    public Response<String> deployApp(@RequestBody AppDeployRequest appDeployRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(appDeployRequest == null, ResponseCodeEnum.PARAMS_ERROR);
        Long appId = appDeployRequest.getAppId();
        ThrowUtils.throwIf(appId == null || appId <= 0, ResponseCodeEnum.PARAMS_ERROR,  "应用ID不能为空");
        User loginUser = userService.getLoginUser(request);
        String deployUrl = appService.deployApp(appId, loginUser);
        return ResponseUtils.success(deployUrl);
    }

}
