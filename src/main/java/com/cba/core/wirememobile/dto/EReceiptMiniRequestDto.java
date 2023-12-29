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
public class EReceiptMiniRequestDto implements Serializable {

    private String email;
    private String contactNo;

    private static final long serialVersionUID = 1L;

}
