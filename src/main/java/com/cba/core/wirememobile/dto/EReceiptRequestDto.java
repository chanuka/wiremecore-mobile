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

    @NotBlank(message = "serialNo is required")
    private String serialNo;
    @NotBlank(message = "rrn is required")
    private String rrn;
    @NotNull(message = "invoiceNo cannot be null")
    @Min(value = 1, message = "invoiceNo must be greater than or equal to 1")
    private Integer invoiceNo;
    @NotNull(message = "traceNo cannot be null")
    @Min(value = 1, message = "traceNo must be greater than or equal to 1")
    private Integer traceNo;
    private String email;
    private String contactNo;

    private static final long serialVersionUID = 1L;

}
