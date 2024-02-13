package com.cba.core.wirememobile.mapper;

import com.cba.core.wirememobile.dto.TransactionRequestDto;
import com.cba.core.wirememobile.dto.TransactionResponseDto;
import com.cba.core.wirememobile.model.TransactionCoreFailed;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TransactionFailedMapper {

    public static TransactionResponseDto toDto(TransactionCoreFailed entity) {
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setAmount(String.valueOf(entity.getAmount()));
        responseDto.setAuthCode(entity.getAuthCode());
        responseDto.setBatchNo(String.valueOf(entity.getBatchNo()));
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setCardLabel(entity.getCardLabel());
        responseDto.setCustMobile(entity.getCustMobile());
        responseDto.setDateTime(entity.getDateTime().toString());
//        responseDto.setDccCurrency(entity.getDccCurrency());
//        responseDto.setDccTranAmount(entity.getDccTranAmount());
//        responseDto.setIssettled(entity.getIssettled());
        responseDto.setId(entity.getId());
        responseDto.setEntryMode(entity.getEntryMode());
        responseDto.setInvoiceNo(String.valueOf(entity.getInvoiceNo()));
        responseDto.setMerchantId(entity.getMerchantId());
        responseDto.setNii(entity.getNii());
        responseDto.setRrn(entity.getRrn());
//        responseDto.setTranType(entity.getTranType());
        responseDto.setPan(entity.getPan());
        responseDto.setSignData(entity.getSignData());
        responseDto.setPaymentMode(entity.getPaymentMode());
        responseDto.setTraceNo(String.valueOf(entity.getTraceNo()));
        responseDto.setRespCode(String.valueOf(entity.getRespCode()));
        return responseDto;
    }

    public static TransactionCoreFailed toModel(TransactionRequestDto dto) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        TransactionCoreFailed entity = new TransactionCoreFailed();
        entity.setAmount(Integer.parseInt(dto.getAmount()));
        entity.setAuthCode(dto.getAuthCode());
        entity.setBatchNo(Integer.parseInt(dto.getBatchNo()));
        entity.setCardLabel(dto.getCardLabel());
        entity.setCurrency(dto.getCurrency());
        entity.setCustMobile(dto.getCustMobile());
        entity.setDateTime(dateFormat.parse(dto.getDateTime()));
        entity.setDccCurrency("");// not set
        entity.setDccTranAmount(0);// not set
        entity.setExpDate(dto.getExpDate());
        entity.setEntryMode(dto.getEntryMode());
        entity.setIssettled(false);// not set
        entity.setEntryMode(dto.getEntryMode());
        entity.setInvoiceNo(Integer.parseInt(dto.getInvoiceNo()));
        entity.setMerchantId(dto.getMerchantId());
        entity.setNii(dto.getNii());
        entity.setOriginId(dto.getOriginId());
        entity.setRrn(dto.getRrn());
        entity.setTerminalId(dto.getTerminalId());
        entity.setTranType(dto.getTransType());
        entity.setPan(dto.getPan());
        entity.setSignData(dto.getSignData());
        entity.setPaymentMode(dto.getPaymentMode());
        entity.setTraceNo(Integer.parseInt(dto.getTraceNo()));
        entity.setRespCode(Integer.parseInt(dto.getRespCode()));

        return entity;
    }
}
