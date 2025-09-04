package org.n1vnhil.llm.lowcode.dev.platform.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.internal.http2.ErrorCode;
import org.n1vnhil.llm.lowcode.dev.platform.ai.tools.FileWriteTool;
import org.n1vnhil.llm.lowcode.dev.platform.exception.BizException;
import org.n1vnhil.llm.lowcode.dev.platform.exception.ResponseCodeEnum;
import org.n1vnhil.llm.lowcode.dev.platform.model.enums.CodeGenerationType;
import org.n1vnhil.llm.lowcode.dev.platform.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.time.Duration;

@Slf4j
@Configuration
public class AiCodeGeneratorServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Resource(name = "openAiStreamingChatModel")
    private StreamingChatModel streamingChatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource(name = "reasoningChatModel")
    private StreamingChatModel reasoningChatModel;

    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return getAiCodeGenerationService(0L, CodeGenerationType.HTML);
    }

    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener(((key, value, cause) -> {
                log.debug("AI服务实例被移除，appId：{}，原因：{}", key, cause);
            }))
            .build();

    public AiCodeGeneratorService getAiCodeGenerationService(Long appId, CodeGenerationType codeGenerationType) {
        String cacheKey = buildCacheKey(appId, codeGenerationType);
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenerationType));
    }

    public AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenerationType codeGenerationType) {
        log.info("创建新的AI服务实例，appId：{}", appId);
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
        return switch (codeGenerationType) {
            case HTML, MULTI_FILE -> AiServices.builder(AiCodeGeneratorService.class)
                        .chatModel(chatModel)
                        .streamingChatModel(streamingChatModel)
                        .chatMemory(chatMemory)
                        .build();


            case VUE -> AiServices.builder(AiCodeGeneratorService.class)
                        .streamingChatModel(reasoningChatModel)
                        .chatMemoryProvider(memoryId -> chatMemory)
                        .tools(new FileWriteTool())
                        .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                                toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name()
                        ))
                        .build();


            default -> {
                throw new BizException(ResponseCodeEnum.OPERATION_ERROR,
                        "不支持的代码生成类型：" + codeGenerationType.getText());
            }
        };
    }

    private String buildCacheKey(Long appId, CodeGenerationType codeGenerationType) {
        return appId + "_" + codeGenerationType.getValue();
    }
}
