package org.n1vnhil.llm.lowcode.dev.platform.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息类型枚举
 *
 * @author zhiheng
 */
@Getter
@AllArgsConstructor
public enum ChatHistoryMessageType {

    USER("用户消息", "user"),
    AI("AI消息","ai" ),
    ;

    private final String text;
    private final String value;


    /**
     * 根据value获取枚举
     *
     * @param value value
     * @return MessageType
     */
    public static ChatHistoryMessageType getEnumByValue(String value) {
        for (ChatHistoryMessageType type: ChatHistoryMessageType.values()) {
            if (type.getValue().equals(value)) return type;
        }
        return null;
    }

}