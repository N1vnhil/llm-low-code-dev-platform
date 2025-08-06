package org.n1vnhil.llm.lowcode.dev.platform.ai;

import dev.langchain4j.service.SystemMessage;

public interface AiCodeGeneratorService {

    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt")
    String generateHtmlCode(String userMessage);

    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt")
    String generateMultiFileCode(String userMessage);
}
