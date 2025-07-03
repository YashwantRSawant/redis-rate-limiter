package org.ytech.ratelimiter.strategy;


import java.util.concurrent.TimeUnit;

public interface RateLimitStrategy {
    boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit unit);
}