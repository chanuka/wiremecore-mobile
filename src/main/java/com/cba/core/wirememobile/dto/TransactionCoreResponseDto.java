package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionCoreResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String paymentMode;
    private String custMobile;
    private String tranType;
    private String cardLabel;
    private String terminalId;
    private Integer traceNo;
    private Integer invoiceNo;
    private BigDecimal amount;
    private String currency;
    private Integer batchNo;
    private String pan;
    private Date dateTime;
    private String nii;
    private String rrn;
    private String authCode;
    private String signData;
    private BigDecimal tipAmount;
    private String entryMode;
    private String dccCurrency;
    private Integer dccTranAmount;
    private Boolean issettled;
    private Integer settledMethod;
    private String merchantId;

}
