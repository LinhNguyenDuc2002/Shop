package com.example.shop.message;

import com.example.shop.config.AsyncTaskConfig;
import org.springframework.scheduling.annotation.Async;

/**
 * Handle sending email
 * @param <T>
 */
public interface EmailService<T extends BaseMessage> {
    /**
     * @param message
     */
    @Async(AsyncTaskConfig.BEAN_ASYNC_EXECUTOR)
    void sendMessage(T message);
}
