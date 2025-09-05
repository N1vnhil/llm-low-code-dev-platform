package org.n1vnhil.llm.lowcode.dev.platform.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.n1vnhil.llm.lowcode.dev.platform.constant.AppConstant;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
public abstract class CodeFileSaverTemplate<T> {

    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + AppConstant.CODE_OUTPUT_ROOT_DIR;

    public final File saveCode(T result, Long appId) {
        validateInput(result);
        String baseDirPath = buildUniqueDir(appId);
        saveFile(result, baseDirPath);
        return new File(baseDirPath);
    }

    protected void validateInput(T result) {
        if (result == null) {
            throw new BizException(ResponseCodeEnum.PARAMS_ERROR, "代码结果对象不能为空");
        }
    }

    protected final String buildUniqueDir(Long appId) {
        CodeGenerationType type = getType();
        String dirName = StrUtil.format("{}_{}", type.getValue(), appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + dirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    protected final void writeToFile(String dirPath, String filename, String content) {
        String filePath = dirPath + File.separator + filename;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }

    protected abstract CodeGenerationType getType();

    protected abstract void saveFile(T result, String baseDirPath);

}
