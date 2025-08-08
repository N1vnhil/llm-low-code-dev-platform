package org.n1vnhil.llm.lowcode.dev.platform;

import org.junit.jupiter.api.Test;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.core.saver.CodeFileSaverExecutor;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SaveFileTests {

    @Test
    void test() {
        MultiFileCodeResult result = new MultiFileCodeResult();
        result.setHtmlCode("html");
        result.setCssCode("css");
        result.setJsCode("js");
        CodeFileSaverExecutor.executeSaver(result, CodeGenerationType.MULTI_FILE);
    }

}
