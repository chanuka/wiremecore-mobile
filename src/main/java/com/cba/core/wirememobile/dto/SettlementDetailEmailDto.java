package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettlementDetailEmailDto implements Serializable {

    private String tid;
    private String mid;
    private String amount;
    private String settledDate;
    private Integer batchNo;
    private String to;
    private String cc;
    private String subject;
    private String currency;
    private String merchantName;
    private String merchantAddress;
    private Integer saleCount;
    private Float saleAmount;
    private Integer saleVoidCount;
    private Float saleVoidAmount;
    private Integer offlineCount;
    private Float offlineAmount;
    private Integer offlineVoidCount;
    private Float offlineVoidAmount;
    private Integer precompCount;
    private Float precompAmount;
    private Integer precompVoidCount;
    private Float precompVoidAmount;
    private List<SettlementDetailTranEmailDto> tranList;

    private static final long serialVersionUID = 1L;

}
