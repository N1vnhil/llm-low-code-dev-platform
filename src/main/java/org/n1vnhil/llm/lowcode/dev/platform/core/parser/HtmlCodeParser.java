package org.n1vnhil.llm.lowcode.dev.platform.core.parser;

import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlCodeParser implements CodeParser<HtmlCodeResult> {

    private final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    @Override
    public HtmlCodeResult parse(String content) {        HtmlCodeResult result = new HtmlCodeResult();
        String htmlCode = extractHtmlCodeByPattern(content);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode);
        } else {
            result.setHtmlCode(content.trim());
        }
        return result;
    }

    private String extractHtmlCodeByPattern(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
