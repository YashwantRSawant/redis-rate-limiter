package org.ytech.ratelimiter.service;

import org.springframework.stereotype.Service;
import org.ytech.ratelimiter.enums.RateLimitAlgorithm;
import org.ytech.ratelimiter.strategy.RateLimitStrategy;
import org.ytech.ratelimiter.strategy.RateLimitStrategyFactory;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private final RateLimitStrategyFactory strategyFactory;

    public RateLimiterService(RateLimitStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit timeUnit, RateLimitAlgorithm algorithm) {
        RateLimitStrategy strategy = strategyFactory.getStrategy(algorithm);
        return strategy.isAllowed(userId, methodName, limit, duration, timeUnit);
    }
}