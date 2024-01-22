package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.Permission;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface PermissionDao {

    List<Permission> findAllPermissionsByUser(String username) throws SQLException;

    List<Permission> findAll() throws Exception;

    Page<Permission> findAll(int page, int pageSize) throws Exception;

    Permission findById(int id) throws Exception;

}
