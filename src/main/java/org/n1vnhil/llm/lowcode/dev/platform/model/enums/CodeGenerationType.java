package org.n1vnhil.llm.lowcode.dev.platform.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeGenerationType {
    HTML("HTML模式", "html"),
    MULTI_FILE("原生多文件模式", "multi_file"),
    VUE("Vue工程模式", "vue")
    ;

    private String text;

    private String value;

    public static CodeGenerationType getEnumByValue(String value) {
        if (value == null) {
            return null;
        }

        for (CodeGenerationType codeGenerationType: CodeGenerationType.values()) {
            if (codeGenerationType.value.equals(value)) {
                return codeGenerationType;
            }
        }

        return null;
    }
}
