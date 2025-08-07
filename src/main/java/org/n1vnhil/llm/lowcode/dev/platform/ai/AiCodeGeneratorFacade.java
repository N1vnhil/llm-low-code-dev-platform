package org.n1vnhil.llm.lowcode.dev.platform.ai;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.core.CodeFileSaver;
import org.n1vnhil.llm.lowcode.dev.platform.core.CodeParser;
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

    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenerationType type) {
        if (type == null) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (type) {
            case HTML -> generateHtmlCodeAndSaveStream(userMessage);

            case MULTI_FILE -> generateMultiFileCodeAndSaveStream(userMessage);

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

    private Flux<String> generateHtmlCodeAndSaveStream(String userMessage) {
        Flux<String> stringFlux = aiCodeGeneratorService.generateHtmlCodeStreaming(userMessage);
        StringBuilder codeBuilder = new StringBuilder();
        return stringFlux
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                   try {
                       String htmlCode = codeBuilder.toString();
                       HtmlCodeResult result = CodeParser.parseHtmlCode(htmlCode);
                       File saveDir = CodeFileSaver.saveHtmlCodeResult(result);
                       log.info("保存成功，路径：{}", saveDir.getAbsolutePath());
                   } catch (Exception e) {
                       log.error("保存失败：{}", e.getMessage());
                   }
                });
    }

    private Flux<String> generateMultiFileCodeAndSaveStream(String userMessage) {
        Flux<String> stringFlux = aiCodeGeneratorService.generateMultiFileCodeStreaming(userMessage);
        StringBuilder codeBuilder = new StringBuilder();
        return stringFlux
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    try {
                        String htmlCode = codeBuilder.toString();
                        MultiFileCodeResult result = CodeParser.parseMultiFileCode(htmlCode);
                        File saveDir = CodeFileSaver.saveMultiFileCodeResult(result);
                        log.info("保存成功，路径：{}", saveDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败：{}", e.getMessage());
                    }
                });
    }
}
