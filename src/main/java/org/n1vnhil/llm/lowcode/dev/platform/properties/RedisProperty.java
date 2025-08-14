package org.n1vnhil.llm.lowcode.dev.platform.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("spring.data.redis")
public class RedisProperty {

    private String host;

    private int port;

    private String password;

    private long ttl;

    private String user;

}
