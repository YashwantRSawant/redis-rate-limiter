package org.ytech.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ytech.ratelimiter.annotation.RateLimit;
import org.ytech.ratelimiter.enums.RateLimitAlgorithm;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class TokenController {

    @GetMapping("/getMyToken")
    @RateLimit(limit = 2, duration = 1, timeUnit = TimeUnit.MINUTES,
            algorithm = RateLimitAlgorithm.FIXED_WINDOW)
    public ResponseEntity<String> getMyToken(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok("Dummy Token for user: " + userId);
    }
}
