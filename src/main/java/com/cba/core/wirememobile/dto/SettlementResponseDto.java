package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettlementResponseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String originId;
    private String deviceSerialNo;
    private List<SettlementAcquireDto> settlementAcquires;
}
