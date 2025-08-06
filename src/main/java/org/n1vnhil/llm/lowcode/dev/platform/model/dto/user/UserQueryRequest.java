package org.n1vnhil.llm.lowcode.dev.platform.model.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.n1vnhil.llm.lowcode.dev.platform.common.PageRequest;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String userAccount;

    private String userName;

    private String userProfile;

    private String userRole;

}