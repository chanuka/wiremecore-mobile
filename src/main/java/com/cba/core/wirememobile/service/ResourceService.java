package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.ResourceResponseDto;

import java.util.List;

public interface ResourceService {
    List<ResourceResponseDto> findAll() throws Exception;
}
