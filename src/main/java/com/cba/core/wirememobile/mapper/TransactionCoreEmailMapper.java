package com.cba.core.wirememobile.mapper;

import com.cba.core.wirememobile.dto.SettlementDetailTranEmailDto;
import com.cba.core.wirememobile.dto.TransactionCoreResponseDto;
import com.cba.core.wirememobile.model.TransactionCore;

public class TransactionCoreEmailMapper {

    public static SettlementDetailTranEmailDto toDto(TransactionCore entity) {
        SettlementDetailTranEmailDto responseDto = new SettlementDetailTranEmailDto();
        responseDto.setAmount(entity.getAmount());
        responseDto.setTransType(entity.getTranType());
        responseDto.setTimestamp(entity.getDateTime().toString());
        responseDto.setAuthCode(entity.getAuthCode());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setCardLabel(entity.getCardLabel());
        responseDto.setInvoiceNo(entity.getInvoiceNo());
        return responseDto;
    }

}
