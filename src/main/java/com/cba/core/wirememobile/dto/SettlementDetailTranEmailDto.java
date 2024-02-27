package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettlementDetailTranEmailDto implements Serializable {

    private Integer invoiceNo;
    private String timestamp;
    private String authCode;
    private String cardLabel;
    private Integer pan;
    private String transType;
    private String currency;
    private BigDecimal amount;


    private static final long serialVersionUID = 1L;

}
