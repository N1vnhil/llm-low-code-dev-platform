package org.n1vnhil.llm.lowcode.dev.platform.core;

import org.n1vnhil.llm.lowcode.dev.platform.ai.model.HtmlCodeResult;
import org.n1vnhil.llm.lowcode.dev.platform.ai.model.MultiFileCodeResult;

import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("```css\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_CODE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    public static HtmlCodeResult parseHtmlCode(String content) {
        HtmlCodeResult result = new HtmlCodeResult();
        String htmlCode = extractCodeByPattern(content, HTML_CODE_PATTERN);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode);
        } else {
            result.setHtmlCode(content.trim());
        }
        return result;
    }

    public static MultiFileCodeResult parseMultiFileCode(String content) {
        MultiFileCodeResult result = new MultiFileCodeResult();
        String htmlCode = extractCodeByPattern(content, HTML_CODE_PATTERN);
        String cssCode = extractCodeByPattern(content, CSS_CODE_PATTERN);
        String jsCode = extractCodeByPattern(content, JS_CODE_PATTERN);

        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode);
        }

        if (cssCode != null && !cssCode.trim().isEmpty()) {
            result.setCssCode(cssCode);
        }

        if (jsCode != null && !jsCode.trim().isEmpty()) {
            result.setJsCode(jsCode);
        }
        return result;
    }

    private static String extractCodeByPattern(String content, Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
