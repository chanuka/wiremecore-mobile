package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.PermissionResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface PermissionDao<T, K> extends GenericDao<T, K> {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException;

    List<PermissionResponseDto> findAllPermissionsByUser() throws SQLException;
}
