package org.n1vnhil.llm.lowcode.dev.platform;

import cn.hutool.core.lang.Assert;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.n1vnhil.llm.lowcode.dev.platform.ai.AiCodeGeneratorFacade;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class AiCodeGenFacadeTests {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void testHtml() {
        aiCodeGeneratorFacade.generateAndSaveCode("日程表小工具", CodeGenerationType.HTML, 1346L);
    }

    @Test
    void testVue() {
        Flux<String> result = aiCodeGeneratorFacade.generateAndSaveCodeStream("日程表小工具", CodeGenerationType.VUE, 100L);
        Assert.notNull(result);
    }

}
