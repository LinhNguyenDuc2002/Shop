package com.example.shop.controller;

import com.example.shop.constant.ResponseMessage;
import com.example.shop.dto.UserDto;
import com.example.shop.dto.request.UserRequest;
import com.example.shop.dto.response.CommonResponse;
import com.example.shop.exception.NotFoundException;
import com.example.shop.exception.ValidationException;
import com.example.shop.service.UserService;
import com.example.shop.util.HandleBindingResult;
import com.example.shop.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserDto>> getLoggedInUser() throws NotFoundException {
        return ResponseUtil.wrapResponse(userService.getLoggedInUser(), ResponseMessage.GET_USER_SUCCESS.getMessage());
    }

    @PostMapping("/verify")
    public ResponseEntity<CommonResponse<UserDto>> verifyOTPToCreateUser(@RequestParam(name = "id") String id,
                                                                         @RequestParam(name = "code") String otp,
                                                                         @RequestParam(name = "secret") String secret) throws ValidationException, NotFoundException {
        return ResponseUtil.wrapResponse(userService.createUser(id, otp, secret), ResponseMessage.CREATE_USER_SUCCESS.getMessage());
    }

    @PostMapping()
    public ResponseEntity<CommonResponse<Void>> createTempUser(
            @Valid @RequestBody UserRequest newUserRequest,
            BindingResult bindingResult) throws ValidationException {
        HandleBindingResult.handle(bindingResult, newUserRequest);
        userService.createTempUser(newUserRequest);
        return ResponseUtil.wrapResponse(null, ResponseMessage.WAIT_ENTER_OTP.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<UserDto>> delete(@PathVariable String id) throws NotFoundException {
        userService.delete(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_USER_SUCCESS.getMessage());
    }

    @GetMapping()
    public ResponseEntity<CommonResponse<List<UserDto>>> getAll() {
        return ResponseUtil.wrapResponse(userService.getAll(), ResponseMessage.GET_ALL_USERS_SUCCESS.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<UserDto>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(userService.get(id), ResponseMessage.GET_USER_SUCCESS.getMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<UserDto>> update(@PathVariable String id,
                                                          @Valid @RequestBody UserRequest userRequest,
                                                          BindingResult bindingResult) throws NotFoundException, ValidationException {
        HandleBindingResult.handle(bindingResult, userRequest);
        return ResponseUtil.wrapResponse(userService.update(id, userRequest), ResponseMessage.UPDATE_USER_SUCCESS.getMessage());
    }
}
