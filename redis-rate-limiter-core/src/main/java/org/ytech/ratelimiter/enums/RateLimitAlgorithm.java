package org.ytech.ratelimiter.enums;

public enum RateLimitAlgorithm {
    FIXED_WINDOW,
    SLIDING_WINDOW_LOG,
    SLIDING_WINDOW_COUNTER,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}