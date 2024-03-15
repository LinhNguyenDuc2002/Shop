package com.example.shop.service.impl;

import com.example.shop.constant.RoleType;
import com.example.shop.cache.TempUser;
import com.example.shop.constant.CommonConstant;
import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.UserDto;
import com.example.shop.dto.request.PasswordRequest;
import com.example.shop.dto.request.UserRequest;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.mapper.UserMapper;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.OtpService;
import com.example.shop.service.UserService;
import com.example.shop.util.OtpUtil;
import com.example.shop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto getLoggedInUser() throws NotFoundException {
        log.info("Get info of logged in user");
        Long id = Long.valueOf(SecurityUtil.getLoggedInUserId());

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Logged in user don't exist");
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        log.info("Got info of logged in user successfully");
        return UserMapper.INSTANCE.toDtoWithoutAddress(user);
    }

    @Override
    public void createTempUser(UserRequest newUserRequest, HttpSession session) throws ValidationException {
        log.info("Save registration information temporarily");

        if(!StringUtils.hasText(newUserRequest.getEmail())) {
            log.error("Email is invalid");
            throw new ValidationException(newUserRequest, ResponseMessage.EMAIL_INVALID.getMessage());
        }
        if(userRepository.existsByEmail(newUserRequest.getEmail())) {
            log.error("Email is already in use");
            throw new ValidationException(newUserRequest, ResponseMessage.EMAIL_EXISTS.getMessage());
        }

        TempUser tempUser = convertToTempUser(newUserRequest);

        String otp = OtpUtil.generateOTP();
        tempUser.setCreatedAt(new Date());
        tempUser.setOtp(otp);

        log.info("Sending OTP to authenticate ...");
        otpService.sendOTP(tempUser.getEmail(), tempUser.getUsername(), otp);

        redisTemplate.opsForValue().set(tempUser.getId(),
                                        tempUser,
                                        CommonConstant.existedTimeTempUser,
                                        TimeUnit.MINUTES);

        log.info("OTP code is sent successfully");
        session.setAttribute("user", tempUser.getId().toString());
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        log.info("Delete user {}",id);

        boolean checkUser = userRepository.existsById(id);
        if(!checkUser) {
            log.error("User {} don't exist", id);
            throw NotFoundException.builder()
                    .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                    .build();
        }

        userRepository.deleteById(id);
        log.info("Deleted user with {}",id);
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Get all users");
        return UserMapper.INSTANCE.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto get(Long id) throws NotFoundException {
        log.info("Get user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        log.info("Got user {} successfully", id);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserDto update(Long id, UserRequest userRequest) throws NotFoundException, ValidationException {
        log.info("Update user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        if(!user.getUsername().equals(userRequest.getUsername())) {
            boolean checkUsername = userRepository.existsByUsername(userRequest.getUsername());

            if(checkUsername) {
                log.error("Username {} existed", userRequest.getUsername());
                throw new ValidationException(userRequest, ResponseMessage.USERNAME_EXISTED.getMessage());
            }
            user.setUsername(userRequest.getUsername());
        }

        user.setFullname(userRequest.getFullname());
        user.setDob(userRequest.getDob());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        userRepository.save(user);

        log.info("Updated user {} successfully", id);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserDto createUser(String otp, HttpSession session) throws ValidationException {
        log.info("Verifying otp ...");
        String id = session.getAttribute("user").toString();

        TempUser tempUser = (TempUser) redisTemplate.opsForValue().get(id);
        if(tempUser == null || !otp.equals(tempUser.getOtp())) {
            log.error("OTP code is invalid or expired");
            throw ValidationException.builder()
                    .errorObject(otp)
                    .message(ResponseMessage.INVALID_OTP.getMessage())
                    .build();
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName(RoleType.valueOf(tempUser.getRole())));

        User user = convertToUser(tempUser);
        redisTemplate.delete(id);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        session.invalidate();

        log.info("Created a new user successfully!");
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public void changePwd(Long id, PasswordRequest passwordRequest) throws NotFoundException, ValidationException {
        log.info("Change password of user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            log.error("Old password and new password don't match");
            throw new ValidationException(passwordRequest, ResponseMessage.OLD_PASSWORD_INVALID.getMessage());
        }

        if(passwordRequest.getNewPassword().equals(passwordRequest.getOldPassword())) {
            log.error("Old password and new password are the same");
            throw new ValidationException(passwordRequest, ResponseMessage.INPUT_PASSWORD_INVALID.getMessage());
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        log.info("Changed password of user {} successfully", id);
        userRepository.save(user);
    }

    @Override
    public void resetPwd(Long id, String password) throws NotFoundException, ValidationException {
        log.info("Reset password of user {}", id);

        if(password.length() < 6 || password.isEmpty()) {
            log.error("New password is invalid");
            throw new ValidationException(password, ResponseMessage.INPUT_PASSWORD_INVALID.getMessage());
        }

        User user = userRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
                });

        user.setPassword(passwordEncoder.encode(password));

        log.info("Reset password of user {} successfully", id);
        userRepository.save(user);
    }

    public User convertToUser(TempUser tempUser) {
        Date join = new Date();
        return User.builder()
                .username(tempUser.getUsername())
                .password(tempUser.getPassword())
                .fullname(tempUser.getFullname())
                .email(tempUser.getEmail())
                .phone(tempUser.getPhone())
                .dob(tempUser.getDob())
                .joinDate(join)
                .build();
    }

    public TempUser convertToTempUser(UserRequest newUserRequest) {
        return TempUser.builder()
                .id(UUID.randomUUID().toString())
                .username(newUserRequest.getUsername())
                .password(newUserRequest.getPassword())
                .fullname(newUserRequest.getFullname())
                .dob(newUserRequest.getDob())
                .phone(newUserRequest.getPhone())
                .email(newUserRequest.getEmail())
                .role(newUserRequest.getRole())
                .build();
    }
}
