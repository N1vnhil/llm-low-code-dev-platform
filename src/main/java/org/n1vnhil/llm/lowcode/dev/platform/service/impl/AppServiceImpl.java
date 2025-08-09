package org.n1vnhil.llm.lowcode.dev.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.n1vnhil.llm.lowcode.dev.platform.constant.UserConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ThrowUtils;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.AppAddRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.AppQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.app.AppUpdateRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.App;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.mapper.AppMapper;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.UserRoleEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.app.AppVO;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.user.UserVO;
import org.n1vnhil.llm.lowcode.dev.platform.service.AppService;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) return null;
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(app, appVO);
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }

        Set<Long> userIds = appList.stream().map(App::getUserId).collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, userService::getUserVO));

        return appList.stream()
                .map(app -> {
                    AppVO appVO = getAppVO(app);
                    Long userId = appVO.getUserId();
                    appVO.setUser(userVOMap.get(userId));
                    return appVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest request) {
        ThrowUtils.throwIf(request == null, ResponseCodeEnum.PARAMS_ERROR);

        String sortField = request.getSortField();

        return QueryWrapper.create()
                .eq("id", request.getId())
                .like("appName", request.getAppName())
                .like("cover", request.getCover())
                .like("initPrompt", request.getInitPrompt())
                .eq("codeGenType", request.getCodeGenType())
                .eq("deployKey", request.getDeployKey())
                .eq("priority", request.getPriority())
                .eq("userId", request.getUserId())
                .orderBy(sortField, "ascend".equals(sortField));
    }

}
