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
public class TransactionRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String originId;
    private String paymentMode;
    private String deviceSerialNo;
    private String uniqueId;
    private String custMobile;
    private String transType;
    private String cardLabel;
    private String terminalId;
    private String merchantId;
    private String traceNo;
    private String invoiceNo;
    private String amount;
    private String tipAmount;
    private String currency;
    private String batchNo;
    private String pan;
    private String dateTime;
    private String expDate;
    private String nii;
    private String rrn;
    private String authCode;
    private String entryMode;
    private String isDccTransaction;
    private String signData;
    private Float lat;
    private Float lng;
    private String email;
    private String contactNo;
    private String respCode;

}
