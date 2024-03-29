package com.cba.core.wirememobile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettlementAcquireDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String terminalId;
    private String merchantId;
    private Integer settledMethod;
    private Integer batchNo;
    private String settledDate;
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

}
