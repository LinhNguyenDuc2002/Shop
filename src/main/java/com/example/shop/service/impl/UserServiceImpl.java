package com.example.shop.service.impl;

import com.example.shop.cache.TempUser;
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
import com.example.shop.repository.TempUserRepository;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TempUserRepository tempUserRepository;

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

        if(userRepository.existsByEmail(newUserRequest.getEmail())) {
            log.error("Email is already in use");
            throw new ValidationException(newUserRequest, ResponseMessage.EMAIL_EXISTS.getMessage());
        }

        TempUser tempUser = convertToTempUser(newUserRequest);
        Calendar calendar = Calendar.getInstance();

        String otp = OtpUtil.generateOTP();
        log.info("Sending OTP to authenticate ...");
        otpService.sendOTP(tempUser.getEmail(), tempUser.getUsername(), otp);

        tempUser.setCreatedAt(new Date());
        tempUser.setOtp(otp);

        tempUserRepository.save(tempUser);

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
    public UserDto createUser(HttpSession session) throws NotFoundException {
        log.info("Create a new user");
        String id = session.getAttribute("user").toString();

        TempUser tempUser = tempUserRepository.findById(Long.valueOf(id))
                .orElseThrow(()->{
                    log.error("No registration information found");

                    return NotFoundException.builder()
                            .message(ResponseMessage.USER_NOT_FOUND.getMessage())
                            .build();
        });

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName("ROLE_USER"));

        User user = convertToUser(tempUser);
        tempUserRepository.deleteById(tempUser.getId());

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
                .username(newUserRequest.getUsername())
                .password(newUserRequest.getPassword())
                .fullname(newUserRequest.getFullname())
                .dob(newUserRequest.getDob())
                .phone(newUserRequest.getPhone())
                .email(newUserRequest.getEmail())
                .build();
    }
}
