package com.example.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "specific_address")
    private String specificAddress;

    private String ward;

    private String district;

    private String city;

    private String country;
}
