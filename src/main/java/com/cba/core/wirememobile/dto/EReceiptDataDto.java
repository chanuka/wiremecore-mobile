package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EReceiptDataDto {
    private String merchantId;
    private String terminalId;
    private String tranType;
    private String paymentMode;
    private String cardLabel;
    private Integer invoiceNo;
    private String authCode;
    private String currency;
    private Integer amount;
    private String pan;
    private String email;
    private String contactNo;
    private Date dateTime;
    private String signData;


}
