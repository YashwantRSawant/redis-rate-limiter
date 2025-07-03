package org.ytech.ratelimiter.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ytech.ratelimiter.annotation.RateLimit;
import org.ytech.ratelimiter.enums.RateLimitAlgorithm;
import org.ytech.ratelimiter.exception.RateLimitExceededException;
import org.ytech.ratelimiter.service.RateLimiterService;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;

    @Value("${rate-limiter.user-id-header:X-User-Id}")
    private String userIdHeader;

    public RateLimitInterceptor(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
        if (rateLimit == null) {
            return true;
        }

        String userId = request.getHeader(userIdHeader);
        if (userId == null || userId.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required header: " + userIdHeader);
            return false;
        }

        int limit = rateLimit.limit();
        int duration = rateLimit.duration();
        TimeUnit timeUnit = rateLimit.timeUnit();
        RateLimitAlgorithm algorithm = rateLimit.algorithm();

        boolean allowed = rateLimiterService.isAllowed(userId, handlerMethod.getMethod().getName(), limit, duration, timeUnit,algorithm);

        if (!allowed) {
            throw new RateLimitExceededException("Rate limit exceeded for user " + userId);
        }

        return true;
    }
}