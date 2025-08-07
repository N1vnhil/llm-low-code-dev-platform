package org.n1vnhil.llm.lowcode.dev.platform;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n1vnhil.llm.lowcode.dev.platform.ai.AiCodeGeneratorService;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiCodeGenerateTests {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("生成一个日历小工具");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode("生成一个日历小工具");
        Assertions.assertNotNull(result);
    }


}
