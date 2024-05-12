package com.example.shop.service.impl;

import com.example.shop.config.RabbitMQConfig;
import com.example.shop.constant.RoleType;
import com.example.shop.entity.Role;
import com.example.shop.entity.User;
import com.example.shop.exception.ValidationException;
import com.example.shop.payload.CustomerRequest;
import com.example.shop.service.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private RabbitTemplate template;

    @Override
    public void createActor(User user) throws ValidationException {
        List<RoleType> roles = user.getRoles().stream().map(Role::getRoleName).toList();

        if (!roles.contains(RoleType.CUSTOMER) && !roles.contains(RoleType.EMPLOYEE)) {
            throw ValidationException.builder().message("abc").build();
        }

        CustomerRequest customerRequest = CustomerRequest.builder()
                .accountId(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(roles.get(0).name())
                .build();

        send(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, customerRequest);
    }

    private void send(String exchange, String routingKey, Object object) {
        template.convertAndSend(exchange, routingKey, object);
    }
}
