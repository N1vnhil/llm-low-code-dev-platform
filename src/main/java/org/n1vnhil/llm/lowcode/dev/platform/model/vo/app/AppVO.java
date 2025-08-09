package org.n1vnhil.llm.lowcode.dev.platform.model.vo.app;

import lombok.Data;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.n1vnhil.llm.lowcode.dev.platform.model.vo.user.UserVO;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AppVO implements Serializable {

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

    /**
     * 应用封面
     */
    private String cover;

    private String codeGenType;

    private String deployKey;

    private LocalDateTime deployedTime;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 创建者id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private UserVO user;

}