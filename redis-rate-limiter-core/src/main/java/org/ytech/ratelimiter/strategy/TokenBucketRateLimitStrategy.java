package org.ytech.ratelimiter.strategy;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("TOKEN_BUCKET")
public class TokenBucketRateLimitStrategy implements RateLimitStrategy {

    private final StringRedisTemplate redisTemplate;

    public TokenBucketRateLimitStrategy(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit unit) {
        String key = String.format("rate:token:%s:%s", methodName, userId);
        String timestampKey = key + ":ts";

        long now = System.currentTimeMillis();
        long refillInterval = unit.toMillis(duration) / limit;

        String lastTsStr = redisTemplate.opsForValue().get(timestampKey);
        String tokensStr = redisTemplate.opsForValue().get(key);

        long lastTs = lastTsStr != null ? Long.parseLong(lastTsStr) : now;
        int tokens = tokensStr != null ? Integer.parseInt(tokensStr) : limit;

        long elapsed = now - lastTs;
        int tokensToAdd = (int) (elapsed / refillInterval);

        tokens = Math.min(limit, tokens + tokensToAdd);
        if (tokens == 0) {
            return false;
        }

        tokens--;
        redisTemplate.opsForValue().set(key, String.valueOf(tokens));
        redisTemplate.opsForValue().set(timestampKey, String.valueOf(now));
        redisTemplate.expire(key, duration, unit);
        redisTemplate.expire(timestampKey, duration, unit);
        return true;
    }
}