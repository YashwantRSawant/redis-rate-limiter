package org.ytech.ratelimiter.strategy;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("LEAKY_BUCKET")
public class LeakyBucketRateLimitStrategy implements RateLimitStrategy {

    private final StringRedisTemplate redisTemplate;

    public LeakyBucketRateLimitStrategy(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String userId, String methodName, int limit, int duration, TimeUnit unit) {
        String key = String.format("rate:leaky:%s:%s", methodName, userId);
        String lastLeakTsKey = key + ":last";

        long now = System.currentTimeMillis();
        long leakInterval = unit.toMillis(duration) / limit;

        String lastLeakStr = redisTemplate.opsForValue().get(lastLeakTsKey);
        long lastLeak = lastLeakStr != null ? Long.parseLong(lastLeakStr) : now;

        long delta = now - lastLeak;
        long leaked = delta / leakInterval;

        String currentLevelStr = redisTemplate.opsForValue().get(key);
        long currentLevel = currentLevelStr != null ? Long.parseLong(currentLevelStr) : 0;

        long newLevel = Math.max(0, currentLevel - leaked);
        if (newLevel >= limit) {
            return false;
        }

        newLevel++;
        redisTemplate.opsForValue().set(key, String.valueOf(newLevel));
        redisTemplate.opsForValue().set(lastLeakTsKey, String.valueOf(now));
        redisTemplate.expire(key, duration, unit);
        redisTemplate.expire(lastLeakTsKey, duration, unit);
        return true;
    }
}