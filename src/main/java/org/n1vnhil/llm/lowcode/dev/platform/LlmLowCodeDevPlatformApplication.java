package org.n1vnhil.llm.lowcode.dev.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class LlmLowCodeDevPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(LlmLowCodeDevPlatformApplication.class, args);
    }

}
