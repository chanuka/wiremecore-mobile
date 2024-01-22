package com.cba.core.wirememobile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettlementRequestDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "{validation.settlement.origin_id.empty}")
    private String originId;
    @NotBlank(message = "{validation.settlement.device_serial.empty}")
    private String deviceSerialNo;
    private List<SettlementAcquireDto> settleAcquirers;
}
