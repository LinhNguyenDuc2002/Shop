package com.example.shop.cache;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name = "temp_user")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;

    private String password;

    private String fullname;

    private Date dob;

    private String email;

    private String phone;

    private String otp;

    @Column(name = "create_at")
    private Date createdAt;
}
