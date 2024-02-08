package com.cba.core.wirememobile.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EReceiptRequestDto implements Serializable {

    @NotBlank(message = "{validation.e_receipt.serialNo.empty}")
    private String serialNo;
    @NotBlank(message = "{validation.e_receipt.rrn.empty}")
    private String rrn;
    @NotNull(message = "{validation.e_receipt.invoiceNo.positive}")
    @Min(value = 1, message = "{validation.e_receipt.invoiceNo.positive}")
    private Integer invoiceNo;
    @NotNull(message = "{validation.e_receipt.traceNo.positive}")
    @Min(value = 1, message = "{validation.e_receipt.traceNo.positive}")
    private Integer traceNo;
    private String email;
    private String contactNo;
    private String dateTime;

    private static final long serialVersionUID = 1L;

}
