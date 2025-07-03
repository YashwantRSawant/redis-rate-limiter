package org.ytech.ratelimiter.annotation;


import org.ytech.ratelimiter.enums.RateLimitAlgorithm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    int limit();                        // Maximum allowed requests
    int duration();                     // Time duration
    TimeUnit timeUnit() default TimeUnit.MINUTES;
    RateLimitAlgorithm algorithm() default RateLimitAlgorithm.FIXED_WINDOW;
}