package org.n1vnhil.llm.lowcode.dev.platform.model.dto.app;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.n1vnhil.llm.lowcode.dev.platform.common.PageRequest;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    private String cover;

    private String initPrompt;

    private String codeGenType;

    private String deployKey;

    private Integer priority;

    /**
     * 创建者id
     */
    private Long userId;

}