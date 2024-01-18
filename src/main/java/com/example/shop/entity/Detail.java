package com.example.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

@Table(name = "details")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long quantity;

    private Double unitPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @EqualsAndHashCode.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    @EqualsAndHashCode.Exclude
    private Bill bill;

    private boolean status;
}
