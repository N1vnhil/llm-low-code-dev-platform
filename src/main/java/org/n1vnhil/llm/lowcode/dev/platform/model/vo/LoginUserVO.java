package org.n1vnhil.llm.lowcode.dev.platform.model.vo;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LoginUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String userAccount;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private String userRole;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
