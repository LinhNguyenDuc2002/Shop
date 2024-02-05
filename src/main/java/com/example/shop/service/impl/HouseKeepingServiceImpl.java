package com.example.shop.service.impl;

import com.example.shop.config.ApplicationConfig;
import com.example.shop.service.HouseKeepingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HouseKeepingServiceImpl implements HouseKeepingService {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void cleanTempUser() {
        log.info("Cleaned temporary user in database");

        int lifespan = applicationConfig.getTempUserLifespanInHour();
        long expirationTime = System.currentTimeMillis() - lifespan * 60 * 60 * 1000L;
        log.info("Clean temporary user that were expired before {}", expirationTime);
    }
}
