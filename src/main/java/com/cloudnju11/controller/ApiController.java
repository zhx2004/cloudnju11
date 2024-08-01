package com.cloudnju11.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * ClassName: ApiController
 * Package: com.cloudnju11.controller
 * Description:
 *
 * @Author: mancanghai
 * @Create: 2024/8/1 - 15:21
 * @Version: v1.0
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private final Bucket bucket;

    public ApiController() {
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofSeconds(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @GetMapping("/hello")
    public String hello(HttpServletResponse response) {
        if (bucket.tryConsume(1)) {
            return "{\"msg\":\"hello\"}";
        } else {
            response.setStatus(429);
            return "{\"msg\":\"Too many requests\"}";
        }
    }
}
