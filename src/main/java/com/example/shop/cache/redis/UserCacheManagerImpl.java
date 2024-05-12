package com.example.shop.cache.redis;

import com.example.shop.cache.UserCacheManager;
import com.example.shop.constant.ResponseMessage;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.redis.model.UserCache;
import com.example.shop.redis.repo.UserCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserCacheManagerImpl implements UserCacheManager {
    @Autowired
    private UserCacheRepository userCacheRepository;

    @Override
    public void storeUserCache(UserCache userCache) {
        userCacheRepository.save(userCache);
    }

    @Override
    public UserCache verifyUserCache(String id, String otp, String secret) throws NotFoundException, ValidationException {
        Optional<UserCache> check = userCacheRepository.findById(id);
        if (!check.isPresent()) {
            throw NotFoundException.builder()
                    .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                    .build();
        }

        UserCache userCache = check.get();
        if (!userCache.getSecretKey().equals(secret) || !userCache.getOtp().equals(otp)) {
            throw ValidationException.builder()
                    .errorObject(otp)
                    .message(ResponseMessage.INVALID_OTP.getMessage())
                    .build();
        }

        return userCache;
    }

    @Override
    public void clearUserCache(String id) {
        if (StringUtils.hasText(id)) {
            boolean check = userCacheRepository.existsById(id);
            if (check) {
                userCacheRepository.deleteById(id);
            }
        }
        userCacheRepository.deleteAll();
    }
}
