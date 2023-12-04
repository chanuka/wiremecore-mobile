package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SelectionScopeDto implements Serializable {
    private String partner;
    private String merchant;
    private String province;
    private String district;

    private static final long serialVersionUID = 1L;

}