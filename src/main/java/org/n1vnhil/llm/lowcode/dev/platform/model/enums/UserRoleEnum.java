package org.n1vnhil.llm.lowcode.dev.platform.model.enums;


import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    USER("用户", "user"),
    ADMIN("管理员", "admin")
    ;

    private final String text;

    private final String value;

    public static UserRoleEnum getEnumByRole(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }

        for (UserRoleEnum userRoleEnum: UserRoleEnum.values()) {
            if (userRoleEnum.value.equals(value)) {
                return userRoleEnum;
            }
        }
        return null;
    }

}
