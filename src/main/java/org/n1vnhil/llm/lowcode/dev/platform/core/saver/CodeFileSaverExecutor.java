package org.n1vnhil.llm.lowcode.dev.platform.core.saver;

import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;

import java.io.File;

public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileSaver = new MultiFileCodeFileSaverTemplate();

    public static File executeSaver(Object result, CodeGenerationType type, Long appId) {
        return switch (type) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) result, appId);
            case MULTI_FILE -> multiFileSaver.saveCode((MultiFileCodeResult) result, appId);
            default -> throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "不支持的代码生成类型：" + type.getValue());
        };
    }

}
