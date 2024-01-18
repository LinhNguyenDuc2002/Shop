package com.example.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

import java.util.Collection;

@Entity
@Data
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String note;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Collection<Product> products;
}
