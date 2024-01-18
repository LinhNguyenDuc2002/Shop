package com.example.shop.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

import java.util.Collection;
import java.util.Date;

@Data
@Table(name = "user")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String fullname;

    private Date dob;

    private String email;

    private String phone;

    private String note;

    @Column(name = "join_date")
    private Date joinDate;

    private boolean status;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Image avatar;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Collection<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Collection<Detail> details;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Collection<Bill> bills;
}
