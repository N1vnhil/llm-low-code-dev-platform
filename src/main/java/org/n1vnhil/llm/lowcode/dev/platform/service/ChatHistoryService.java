package org.n1vnhil.llm.lowcode.dev.platform.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryAddRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory.ChatHistoryQueryRequest;
import org.n1vnhil.llm.lowcode.dev.platform.model.entity.ChatHistory;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.chatHistory.ChatHistoryVO;

import java.util.List;

/**
 * 对话历史 服务层。
 *
 * @author zhiheng
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 创建对话历史
     *
     * @param chatHistoryAddRequest 添加请求
     * @return 对话历史id
     */
    Long addChatHistory(ChatHistoryAddRequest chatHistoryAddRequest);

    /**
     * 获取对话历史VO
     *
     * @param chatHistory 对话历史
     * @return 对话历史VO
     */
    ChatHistoryVO getChatHistoryVO(ChatHistory chatHistory);

    /**
     * 获取对话历史VO列表
     *
     * @param chatHistoryList 对话历史列表
     * @return 对话历史VO列表
     */
    List<ChatHistoryVO> getChatHistoryVOList(List<ChatHistory> chatHistoryList);

    /**
     * 获取查询条件
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 分页查询对话历史VO
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 分页结果
     */
    Page<ChatHistoryVO> pageChatHistoryVO(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 根据应用id删除所有对话历史
     *
     * @param appId 应用id
     * @return 是否成功
     */
    boolean deleteByAppId(Long appId);

    /**
     * 查询某个应用的最新n条对话历史
     *
     * @param appId 应用id
     * @param limit 查询数量
     * @return 对话历史列表
     */
    List<ChatHistoryVO> getLatestChatHistoryByAppId(Long appId, int limit);

    boolean addChatMessage(Long appId, String message, String type, Long userId);

}
