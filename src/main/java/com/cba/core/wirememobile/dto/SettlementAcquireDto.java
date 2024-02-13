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
    private int settledMethod;
    private int batchNo;
    private String settledDate;
    private int saleCount;
    private int saleAmount;
    private int saleVoidCount;
    private int saleVoidAmount;
    private int offlineCount;
    private int offlineAmount;
    private int offlineVoidCount;
    private int offlineVoidAmount;
    private int precompCount;
    private int precompAmount;

}
