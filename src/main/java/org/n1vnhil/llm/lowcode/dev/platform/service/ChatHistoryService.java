package org.n1vnhil.llm.lowcode.dev.platform.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryAddRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.ChatHistory;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.User;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.chatHistory.ChatHistoryVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史 服务层。
 *
 * @author zhiheng
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 获取查询条件
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 分页查询对话历史VO
     * @return 分页结果
     */
    Page<ChatHistory> pageChatHistory(Long appId, int pageSize, LocalDateTime lastCreateTime, User loginUser);

    /**
     * 根据应用id删除所有对话历史
     *
     * @param appId 应用id
     * @return 是否成功
     */
    boolean deleteByAppId(Long appId);

    boolean addChatMessage(Long appId, String message, String type, Long userId);

    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

}
