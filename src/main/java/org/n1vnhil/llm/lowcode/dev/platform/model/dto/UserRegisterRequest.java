package org.n1vnhil.llm.lowcode.dev.platform.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String password;

    private String checkPassword;

}
