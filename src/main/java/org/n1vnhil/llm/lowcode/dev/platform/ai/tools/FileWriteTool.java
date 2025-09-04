package org.n1vnhil.llm.lowcode.dev.platform.ai.tools;

import cn.hutool.core.util.StrUtil;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ThrowUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileWriteTool {

    @Tool("根据相对路径写入文件内容")
    public String writeFile(@P("文件相对路径") String relativePath, @P("文件内容") String content) {
        ThrowUtils.throwIf(StrUtil.isEmpty(relativePath) || StrUtil.isEmpty(content), ResponseCodeEnum.PARAMS_ERROR);
        try {
            Path cwd = Paths.get(System.getProperty("user.dir"));
            Path target = cwd.resolve(relativePath).normalize();
            Path parent = target.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
                log.info("Create new dir: {}", parent);
            }

            Files.write(target, content.getBytes());
            log.info("Write file size: {} bytes", content.length());
            return target.toString();
        } catch (Exception e) {
            throw new BizException(ResponseCodeEnum.OPERATION_ERROR, "Tool Calling Fail: 根据绝对路径写入文件内容");
        }

    }

}
