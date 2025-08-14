package org.n1vnhil.llm.lowcode.dev.platform;

import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(scanBasePackages = "org.n1vnhil.llm.lowcode.dev.platform", exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("org.n1vnhil.llm.lowcode.dev.platform.mapper")
public class LlmLowCodeDevPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(LlmLowCodeDevPlatformApplication.class, args);
    }

}
