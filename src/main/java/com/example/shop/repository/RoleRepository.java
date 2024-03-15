package com.example.shop.repository;

import com.example.shop.constant.RoleType;
import com.example.shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleType roleName);
}
