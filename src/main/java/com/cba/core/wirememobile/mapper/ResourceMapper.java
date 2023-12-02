package com.cba.core.wirememobile.mapper;

import com.cba.core.wirememobile.dto.ResourceResponseDto;
import com.cba.core.wirememobile.model.Resource;

public class ResourceMapper {

    public static ResourceResponseDto toDto(Resource entity) {
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setName(entity.getName());
        return responseDto;
    }

}
