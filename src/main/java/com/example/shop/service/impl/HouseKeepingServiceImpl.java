package com.example.shop.service.impl;

import com.example.shop.cache.TempUser;
import com.example.shop.config.ApplicationConfig;
import com.example.shop.repository.TempUserRepository;
import com.example.shop.service.HouseKeepingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class HouseKeepingServiceImpl implements HouseKeepingService {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private TempUserRepository tempUserRepository;

    @Override
    public void cleanTempUser() {
        log.info("Cleaned temporary user in database");

        int lifespan = applicationConfig.getTempUserLifespanInHour();
        long expirationTime = System.currentTimeMillis() - lifespan * 60 * 60 * 1000L;
        log.info("Clean temporary user that were expired before {}", expirationTime);

        List<TempUser> tempUsers = tempUserRepository.findByCreatedAt(new Date(expirationTime));

        tempUserRepository.deleteAll(tempUsers);
    }
}
