package org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory;

import org.n1vnhil.llm.lowcode.dev.platform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询对话历史请求
 *
 * @author zhiheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 消息类型: user/ai
     */
    private String messageType;

    private String message;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 最后一条记录的创建时间
     */
    private LocalDateTime lastCreateTime;

    private static final long serialVersionUID = 1L;

}