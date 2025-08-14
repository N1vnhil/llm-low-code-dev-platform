package org.n1vnhil.llm.lowcode.dev.platform.config;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import jakarta.annotation.Resource;
import lombok.Data;
import org.n1vnhil.llm.lowcode.dev.platform.properties.RedisProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedisChatMemoryStoreConfig {

    @Resource
    private RedisProperty redisProperty;

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore() {
        String host = redisProperty.getHost();
        int port = redisProperty.getPort();
        String password = redisProperty.getPassword();
        long ttl = redisProperty.getTtl();
        String user = redisProperty.getUser();
        return RedisChatMemoryStore.builder()
                .host(host)
                .port(port)
                .password(password)
                .ttl(ttl)
                .user(user)
                .build();
    }

}
