package org.n1vnhil.llm.lowcode.dev.platform.model.vo.chatHistory;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对话历史VO
 *
 * @author zhiheng
 */
@Data
public class ChatHistoryVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型: user/ai/error
     */
    private String messageType;

    /**
     * 消息类型描述
     */
    private String messageTypeDesc;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

}