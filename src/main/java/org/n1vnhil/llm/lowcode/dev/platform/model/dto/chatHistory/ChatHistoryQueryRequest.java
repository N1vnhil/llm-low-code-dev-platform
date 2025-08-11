package org.n1vnhil.llm.lowcode.dev.platform.model.dto.chatHistory;

import org.n1vnhil.llm.lowcode.dev.platform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询对话历史请求
 *
 * @author zhiheng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 消息类型: user/ai/error
     */
    private String messageType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    private static final long serialVersionUID = 1L;

}