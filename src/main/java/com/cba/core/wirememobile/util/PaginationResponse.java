package com.cba.core.wirememobile.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse <T>{

    private List<T> content;
    private long totalElements;
}
