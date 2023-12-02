package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.PermissionResponseDto;

import java.util.List;

public interface PermissionService<T, K> extends GenericService<T, K> {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws Exception;

}
