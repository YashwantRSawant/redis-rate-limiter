package org.ytech.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.ytech.example",
        "org.ytech.ratelimiter"  // 👈 explicitly include the core
})
public class RateLimiterExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterExampleApplication.class, args);
    }
}