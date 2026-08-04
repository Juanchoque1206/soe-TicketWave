package com.soe.jcb.eventdriven.demo.saga.saga_orchestrator;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Backing store for saga progress, keyed per order. Uses Redis so distributed
 * consumers can deduplicate idempotent steps.
 */
@Component
public class SagaStateManager {

    private static final String KEY_PREFIX = "saga:order:";
    private static final Duration TTL = Duration.ofDays(1);

    private final RedisTemplate<String, String> redisTemplate;

    public SagaStateManager(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Atomically marks a step as completed for an order.
     *
     * @return true if this call performed the transition (i.e. the step was
     *         not already completed), false to cause the caller to skip.
     */
    public boolean markStepCompleted(Long orderId, String step) {
        String key = KEY_PREFIX + orderId + ":" + step;
        Boolean alreadySet = redisTemplate.opsForValue().setIfAbsent(key, "done", TTL);
        return Boolean.TRUE.equals(alreadySet);
    }

    public void markSagaCompleted(Long orderId) {
        redisTemplate.opsForValue().set(KEY_PREFIX + orderId + ":completed", "done", TTL);
    }

    public void removeSagaState(Long orderId) {
        redisTemplate.delete(KEY_PREFIX + orderId + "*");
    }
}