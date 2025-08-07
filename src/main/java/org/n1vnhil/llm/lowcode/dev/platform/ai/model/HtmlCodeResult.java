package org.n1vnhil.llm.lowcode.dev.platform.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
@Description("生成 HTML 代码文件结果")
public class HtmlCodeResult {

    @Description("HTML 代码")
    private String htmlCode;

    @Description("生成代码的描述")
    private String description;
}
