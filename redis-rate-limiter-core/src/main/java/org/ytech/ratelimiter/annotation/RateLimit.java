package org.ytech.ratelimiter.annotation;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    int limit();                        // Maximum allowed requests
    int duration();                     // Time duration
    TimeUnit timeUnit() default TimeUnit.MINUTES;  // Unit for duration
}