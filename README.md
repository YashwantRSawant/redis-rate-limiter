# Redis Rate Limiter

A pluggable Java-based Redis-backed rate limiting utility built with Spring Boot. Easily integrate and annotate your APIs with fine-grained rate limits per user using different algorithms.

---

## Features

-   Simple annotation-based API rate limiting
-   Per-user request throttling via request headers
-   Redis-backed — stateless and scalable
-   Pluggable algorithm support using Strategy Pattern
-   Multiple algorithms supported:
  - Fixed Window
  - Sliding Window Log
  - Sliding Window Counter
  - Token Bucket
  - Leaky Bucket

---

## Setup

### Maven (Multi-module)

This project has two modules:

- `redis-rate-limiter-core`: Core library
- `redis-rate-limiter-example`: Sample Spring Boot app

### Build

```bash
mvn clean install --settings .mvn/settings.xml
```

## Run Example App
```bash
mvn spring-boot:run -pl redis-rate-limiter-example --settings .mvn/settings.xml
```


## How to use
```bash

@RateLimit(
    limit = 5,
    duration = 1,
    timeUnit = TimeUnit.MINUTES,
    algorithm = RateLimitAlgorithm.FIXED_WINDOW // Optional, defaults to FIXED_WINDOW
)
@GetMapping("/api/getMyToken")
```
