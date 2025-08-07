package org.n1vnhil.llm.lowcode.dev.platform.ai;

import jakarta.annotation.Resource;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.core.CodeFileSaver;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    public File generateAndSaveCode(String userMessage, CodeGenerationType type) {
        if (type == null) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (type) {
            case HTML -> generateHtmlCodeAndSave(userMessage);

            case MULTI_FILE -> generateMultiFileCodeAndSave(userMessage);

            default -> {
                String message = "不支持的生成类型：" + type;
                throw new BizException(ResponseCodeEnum.PARAMS_ERROR, message);
            }
        };
    }

    private File generateHtmlCodeAndSave(String userMessage) {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
        return CodeFileSaver.saveHtmlCodeResult(result);
    }

    private File generateMultiFileCodeAndSave(String userMessage) {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }
}
