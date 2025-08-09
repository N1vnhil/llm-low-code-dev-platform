package org.n1vnhil.llm.lowcode.dev.platform.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AppAdminUpdateRequest implements Serializable {

    private Long id;

    private String appName;

    private Integer priority;

    private String cover;

    @Serial
    private static final long serialVersionUID = 1L;

}
