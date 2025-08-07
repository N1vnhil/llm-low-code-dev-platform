package org.n1vnhil.llm.lowcode.dev.platform;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.n1vnhil.llm.lowcode.dev.platform.ai.AiCodeGeneratorFacade;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiCodeGenFacadeTests {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void testHtml() {
        aiCodeGeneratorFacade.generateAndSaveCode("日历小工具", CodeGenerationType.HTML);
    }

}
