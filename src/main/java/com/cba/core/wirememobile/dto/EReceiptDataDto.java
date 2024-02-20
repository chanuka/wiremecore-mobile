package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EReceiptDataDto {

    private String receiptType;
    private String merchantName;
    private String merchantAddress;
    private String mid;
    private String tid;
    private BigDecimal amount;
    private String timestamp;
    private String transType;
    private String cardLabel;
    private String expDate;
    private Integer invoiceNo;
    private String authCode;
    private Integer batchNo;
    private String rrn;
    private String currency;
    private String pan;
    private String to;
    private String subject;
    private String signData;

}
