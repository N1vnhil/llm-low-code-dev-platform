package org.n1vnhil.llm.lowcode.dev.platform.ai;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.core.parser.CodeParserExecutor;
import org.n1vnhil.llm.lowcode.dev.platform.core.saver.CodeFileSaverExecutor;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

@Slf4j
@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 代码生成同一入口
     * @param userMessage 用户prompt
     * @param type 生成代码类型
     * @return 生成结果
     */
    public File generateAndSaveCode(String userMessage, CodeGenerationType type, Long appId) {
        if (type == null) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (type) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenerationType.HTML, appId);
            }

            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenerationType.MULTI_FILE, appId);
            }

            default -> {
                String message = "不支持的生成类型：" + type;
                throw new BizException(ResponseCodeEnum.PARAMS_ERROR, message);
            }
        };
    }

    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenerationType type, Long appId) {
        if (type == null) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR, "生成类型为空");
        }

        Flux<String> result = switch (type) {
            case HTML -> aiCodeGeneratorService.generateHtmlCodeStream(userMessage);

            case MULTI_FILE -> aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);

            default -> {
                String message = "不支持的生成类型：" + type;
                throw new BizException(ResponseCodeEnum.PARAMS_ERROR, message);
            }
        };

        return processCodeStream(result, type, appId);
    }

    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenerationType type, Long appId) {
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream.doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    try {
                        String code = codeBuilder.toString();
                        Object parsedCode = CodeParserExecutor.execute(code, type);
                        File saveDir = CodeFileSaverExecutor.executeSaver(parsedCode, type, appId);
                        log.info("保存成功，路径为：{}", saveDir);
                    } catch (Exception e) {
                        log.error("保存失败：{}", e.getMessage());
                    }
                });
    }

}
