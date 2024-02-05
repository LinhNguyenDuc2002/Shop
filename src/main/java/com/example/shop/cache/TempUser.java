package com.example.shop.cache;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempUser implements Serializable {
    @Id
    private String id;

    private String username;

    private String password;

    private String fullname;

    private Date dob;

    private String email;

    private String phone;

    private String otp;

    private Date createdAt;
}
