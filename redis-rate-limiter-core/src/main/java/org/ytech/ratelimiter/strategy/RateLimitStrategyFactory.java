package org.ytech.ratelimiter.strategy;

import org.springframework.stereotype.Component;
import org.ytech.ratelimiter.enums.RateLimitAlgorithm;

import java.util.EnumMap;
import java.util.Map;

@Component
public class RateLimitStrategyFactory {

    private final Map<RateLimitAlgorithm, RateLimitStrategy> strategyMap = new EnumMap<>(RateLimitAlgorithm.class);

    public RateLimitStrategyFactory(Map<String, RateLimitStrategy> strategies) {
        for (RateLimitAlgorithm algo : RateLimitAlgorithm.values()) {
            if (strategies.containsKey(algo.name())) {
                strategyMap.put(algo, strategies.get(algo.name()));
            }
        }
    }

    public RateLimitStrategy getStrategy(RateLimitAlgorithm algorithm) {
        RateLimitStrategy strategy = strategyMap.get(algorithm);
        if (strategy == null) {
            throw new UnsupportedOperationException("No strategy defined for: " + algorithm);
        }
        return strategy;
    }
}