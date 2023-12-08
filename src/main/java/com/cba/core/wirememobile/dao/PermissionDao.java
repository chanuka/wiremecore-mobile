package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.PermissionResponseDto;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface PermissionDao {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException;

    List<PermissionResponseDto> findAllPermissionsByUser() throws SQLException;

    Page<PermissionResponseDto> findAll(int page, int pageSize) throws Exception;

    PermissionResponseDto findById(int id) throws Exception;

}
