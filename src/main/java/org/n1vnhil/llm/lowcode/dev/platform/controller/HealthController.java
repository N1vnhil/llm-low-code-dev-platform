package org.n1vnhil.llm.lowcode.dev.platform.controller;

import org.n1vnhil.llm.lowcode.dev.platform.common.Response;
import org.n1vnhil.llm.lowcode.dev.platform.common.ResponseUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Response<String> healthCheck() {
        return ResponseUtils.success("ok");
    }

}
