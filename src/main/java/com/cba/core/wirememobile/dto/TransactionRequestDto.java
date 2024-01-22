package com.cba.core.wirememobile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "{validation.transaction.origin_id.empty}")
    private String originId;
    @NotBlank(message = "{validation.transaction.payment_mode.empty}")
    private String paymentMode;
    @NotBlank(message = "{validation.transaction.device_serial.empty}")
    private String deviceSerialNo;
    @NotBlank(message = "{validation.transaction.unique_id.empty}")
    private String uniqueId;
    @NotBlank(message = "{validation.transaction.cust_mobile.empty}")
    private String custMobile;
    @NotBlank(message = "{validation.transaction.trans_type.empty}")
    private String transType;
    @NotBlank(message = "{validation.transaction.card_label.empty}")
    private String cardLabel;
    @NotBlank(message = "{validation.transaction.terminal_id.empty}")
    private String terminalId;
    @NotBlank(message = "{validation.transaction.merchant_id.empty}")
    private String merchantId;
    @NotBlank(message = "{validation.transaction.trace_no.empty}")
    private String traceNo;
    @NotBlank(message = "{validation.transaction.invoice_no.empty}")
    private String invoiceNo;
    @NotBlank(message = "{validation.transaction.amount.empty}")
    private String amount;
    private String tipAmount;
    @NotBlank(message = "{validation.transaction.currency.empty}")
    private String currency;
    @NotBlank(message = "{validation.transaction.batch_no.empty}")
    private String batchNo;
    @NotBlank(message = "{validation.transaction.pan.empty}")
    private String pan;
    @NotBlank(message = "{validation.transaction.date_time.empty}")
    private String dateTime;
    private String expDate;
    @NotBlank(message = "{validation.transaction.nii.empty}")
    private String nii;
    private String rrn;
    private String authCode;
    @NotBlank(message = "{validation.transaction.entry_mode.empty}")
    private String entryMode;
    private String isDccTransaction;
    private String signData;
    @NotNull(message = "{validation.transaction.lat.positive}")
    @Positive(message = "{validation.transaction.lat.positive}")
    private Float lat;
    @NotNull(message = "{validation.transaction.lng.positive}")
    @Positive(message = "{validation.transaction.lng.positive}")
    private Float lng;
    private String email;
    private String contactNo;
    private String respCode;

}
