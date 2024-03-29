package com.cba.core.wirememobile.mapper;

import com.cba.core.wirememobile.dto.TransactionCoreResponseDto;
import com.cba.core.wirememobile.model.TransactionCore;

public class TransactionCoreMapper {

    public static TransactionCoreResponseDto toDto(TransactionCore entity) {
        TransactionCoreResponseDto responseDto = new TransactionCoreResponseDto();
        responseDto.setAmount(entity.getAmount());
        responseDto.setTipAmount(entity.getTipAmount());
        responseDto.setAuthCode(entity.getAuthCode());
        responseDto.setBatchNo(entity.getBatchNo());
        responseDto.setCardLabel(entity.getCardLabel());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setCustMobile(entity.getCustMobile());
        responseDto.setDateTime(entity.getDateTime());
        responseDto.setDccCurrency(entity.getDccCurrency());
        responseDto.setDccTranAmount(entity.getDccTranAmount());
        responseDto.setIssettled(entity.getIssettled());
        responseDto.setId(entity.getId());
        responseDto.setEntryMode(entity.getEntryMode());
        responseDto.setInvoiceNo(entity.getInvoiceNo());
        responseDto.setMerchantId(entity.getMerchantId());
        responseDto.setNii(entity.getNii());
        responseDto.setRrn(entity.getRrn());
        responseDto.setTranType(entity.getTranType());
        responseDto.setPan(entity.getPan());
        responseDto.setSignData(entity.getSignData());
        responseDto.setPaymentMode(entity.getPaymentMode());
        responseDto.setTraceNo(entity.getId());
        return responseDto;
    }

}
