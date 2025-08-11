package org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加对话历史请求
 *
 * @author zhiheng
 */
@Data
public class ChatHistoryAddRequest implements Serializable {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型: user/ai/error
     */
    private String messageType;

    /**
     * 用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;

}