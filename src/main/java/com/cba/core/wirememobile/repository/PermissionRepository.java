package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.role = (SELECT ur.role from UserRole ur WHERE ur.userByUserId=(SELECT u FROM User u WHERE u.userName=:username))")
    List<Permission> findAllPermissionsByUser(String username);

}
