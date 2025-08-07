package org.n1vnhil.llm.lowcode.dev.platform.core.parser;

import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;

public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    public static Object execute(String content, CodeGenerationType type) {
        return switch (type) {
            case HTML -> htmlCodeParser.parse(content);
            case MULTI_FILE -> multiFileCodeParser.parse(content);
            default -> throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "不支持的代码生成类型：" + type.getValue());
        };
    }

}
