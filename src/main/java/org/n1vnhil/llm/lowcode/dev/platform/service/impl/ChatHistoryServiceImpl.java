package org.n1vnhil.llm.lowcode.dev.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ThrowUtils;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryAddRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.ChatHistory;
import org.n1vnhil.llm.lowcode.dev.platform.mapper.ChatHistoryMapper;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.ChatHistoryMessageType;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.chatHistory.ChatHistoryVO;
import org.n1vnhil.llm.lowcode.dev.platform.service.ChatHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对话历史 服务层实现。
 *
 * @author zhiheng
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

    @Override
    public Long addChatHistory(ChatHistoryAddRequest chatHistoryAddRequest) {
        return 0L;
    }

    @Override
    public ChatHistoryVO getChatHistoryVO(ChatHistory chatHistory) {
        return null;
    }

    @Override
    public List<ChatHistoryVO> getChatHistoryVOList(List<ChatHistory> chatHistoryList) {
        return List.of();
    }

    @Override
    public QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest) {
        return null;
    }

    @Override
    public Page<ChatHistoryVO> pageChatHistoryVO(ChatHistoryQueryRequest chatHistoryQueryRequest) {
        return null;
    }

    @Override
    public boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null, ResponseCodeEnum.PARAMS_ERROR);
        QueryWrapper wrapper = QueryWrapper.create()
                .eq("appId", appId);
        return this.remove(wrapper);
    }

    @Override
    public List<ChatHistoryVO> getLatestChatHistoryByAppId(Long appId, int limit) {
        return List.of();
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
