package com.example.shop.entity;

import com.example.shop.RoleType;
import com.example.shop.converter.RoleTypeConverter;
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
    @Convert(converter = RoleTypeConverter.class)
    private RoleType roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Collection<User> users;
}
