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
 * Description:1.实现一个 REST 接口（简单接口即可，比如 json 串 {"msg":"hello"}）
 *             2.接口提供限流功能，当请求达到每秒 100 次的时候，返回 429（Too many requests）
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
