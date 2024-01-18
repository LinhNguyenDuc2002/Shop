package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Collection<User> users;
}
