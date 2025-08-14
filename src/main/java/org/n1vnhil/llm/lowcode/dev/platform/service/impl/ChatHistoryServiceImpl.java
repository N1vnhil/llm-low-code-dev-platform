package org.n1vnhil.llm.lowcode.dev.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.n1vnhil.llm.lowcode.dev.platform.constant.UserConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ThrowUtils;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryAddRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.App;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.ChatHistory;
import org.n1vnhil.llm.lowcode.dev.platform.mapper.ChatHistoryMapper;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.ChatHistoryMessageType;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.chatHistory.ChatHistoryVO;
import org.n1vnhil.llm.lowcode.dev.platform.service.AppService;
import org.n1vnhil.llm.lowcode.dev.platform.service.ChatHistoryService;
import org.n1vnhil.llm.lowcode.dev.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

    @Resource
    @Lazy
    private AppService appService;

    @Override
    public QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (chatHistoryQueryRequest == null) {
            return queryWrapper;
        }

        Long id = chatHistoryQueryRequest.getId();
        Long appId = chatHistoryQueryRequest.getAppId();
        String messageType = chatHistoryQueryRequest.getMessageType();
        Long userId = chatHistoryQueryRequest.getUserId();
        LocalDateTime lastCreateTime = chatHistoryQueryRequest.getLastCreateTime();
        String message = chatHistoryQueryRequest.getMessage();
        String sortField = chatHistoryQueryRequest.getSortField();
        String sortOrder = chatHistoryQueryRequest.getSortOrder();

        queryWrapper.eq("id", id)
                .eq("appId", appId)
                .like("message", message)
                .eq("userId", userId)
                .eq("messageType", messageType);
        if (lastCreateTime != null) {
            queryWrapper.lt("createTime", lastCreateTime);
        }

        if (StrUtil.isNotBlank(sortField)) {
            queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        } else {
            queryWrapper.orderBy("createTime", false);
        }

        return queryWrapper;
    }

    @Override
    public Page<ChatHistory> pageChatHistory(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(pageSize <= 0 || pageSize > 50, ResponseCodeEnum.PARAMS_ERROR, "页面大小必须在1-50之间");
        ThrowUtils.throwIf(loginUser == null, ResponseCodeEnum.NOT_LOGIN_ERROR);

        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ResponseCodeEnum.PARAMS_ERROR, "应用不存在");
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        boolean isCreator = app.getUserId().equals(loginUser.getId());
        ThrowUtils.throwIf(!isAdmin && !isCreator, ResponseCodeEnum.NO_AUTH_ERROR, "无权查看该应用对话记录");
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setAppId(appId);
        request.setUserId(loginUser.getId());
        QueryWrapper queryWrapper = this.getQueryWrapper(request);
        return this.page(Page.of(1, pageSize), queryWrapper);
    }

    @Override
    public boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null, ResponseCodeEnum.PARAMS_ERROR);
        QueryWrapper wrapper = QueryWrapper.create()
                .eq("appId", appId);
        return this.remove(wrapper);
    }

    @Override
    public boolean addChatMessage(Long appId, String message, String type, Long userId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ResponseCodeEnum.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ResponseCodeEnum.PARAMS_ERROR, "消息内容不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(type), ResponseCodeEnum.PARAMS_ERROR, "消息类型不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ResponseCodeEnum.PARAMS_ERROR, "用户ID不能为空");
        ChatHistoryMessageType messageType = ChatHistoryMessageType.getEnumByValue(type);
        ThrowUtils.throwIf(messageType == null, ResponseCodeEnum.PARAMS_ERROR, "不支持的消息类型");
        ChatHistory chatHistory = ChatHistory.builder()
                .appId(appId)
                .message(message)
                .userId(userId)
                .messageType(type)
                .build();
        return this.save(chatHistory);
    }

}
