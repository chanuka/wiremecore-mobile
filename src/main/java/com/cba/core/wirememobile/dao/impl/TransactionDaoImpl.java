package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.TransactionDao;
import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.mapper.TransactionCoreMapper;
import com.cba.core.wirememobile.mapper.TransactionFailedMapper;
import com.cba.core.wirememobile.mapper.TransactionMapper;
import com.cba.core.wirememobile.model.*;
import com.cba.core.wirememobile.repository.*;
import com.cba.core.wirememobile.service.EmailService;
import com.cba.core.wirememobile.service.SmsService;
import com.cba.core.wirememobile.util.DeviceTypeEnum;
import com.cba.core.wirememobile.util.SettlementMethodEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

    private final TransactionRepository transactionRepository;
    private final TransactionFailedRepository transactionFailedRepository;
    private final DeviceRepository deviceRepository;
    private final MerchantRepository merchantRepository;
    private final TerminalRepository terminalRepository;
    private final EReceiptRepository eReceiptRepository;
    private final EmailService emailService;
    private final SmsService smsService;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String generateEReceipt(EReceiptRequestDto requestDto) throws Exception {

        EReceipt eReceipt = null;
        EReceipt savedEReceipt = null;

        TransactionCore transactionCore = transactionRepository.findByRrnAndInvoiceNoAndTraceNo(
                requestDto.getSerialNo(),
                requestDto.getRrn(),
                requestDto.getInvoiceNo(),
                requestDto.getTraceNo()
        ).orElseThrow(() -> new NotFoundException("Transaction Not Found"));

//        EReceipt eReceipt = eReceiptRepository.findByTransactionCore_IdAndReceiptType(transactionCore.getId(), "customer_copy").orElseThrow(() -> new NotFoundException("e-Receipt Info Not Found"));
        Terminal terminal = terminalRepository.findByTerminalId(transactionCore.getTerminalId()).orElseThrow(() -> new NotFoundException("Terminal Not Found"));
        Merchant merchant = merchantRepository.findByMerchantId(transactionCore.getMerchantId()).orElseThrow(() -> new NotFoundException("Merchant Not Found"));

        if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail()) ||
                requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
            eReceipt = new EReceipt(transactionCore, requestDto.getEmail(), requestDto.getContactNo(), "customer_copy", false, false);
            savedEReceipt = eReceiptRepository.save(eReceipt);

            EReceiptDataDto eReceiptCustomer = new EReceiptDataDto(
                    savedEReceipt.getId(),
                    transactionCore.getMerchantId(),
                    transactionCore.getTerminalId(),
                    transactionCore.getTranType(),
                    transactionCore.getPaymentMode(),
                    transactionCore.getCardLabel(),
                    transactionCore.getInvoiceNo(),
                    transactionCore.getAuthCode(),
                    transactionCore.getCurrency(),
                    transactionCore.getAmount(),
                    transactionCore.getPan(),
                    requestDto.getEmail(),
                    requestDto.getContactNo(),
                    transactionCore.getDateTime(),
                    transactionCore.getSignData()
            );
            if (eReceipt.getEmail() != null && !"".equals(eReceipt.getEmail())) {
                emailService.sendEmail(eReceiptCustomer.getEmail(), eReceiptCustomer);
            }
            if (eReceipt.getContactNo() != null && !"".equals(eReceipt.getContactNo())) {
                smsService.sendSms(eReceiptCustomer.getContactNo(), eReceiptCustomer);
            }
        }
        return "e-Receipt Sent Successfully";
    }

    @Override
    public String generateEReceipt(int id, EReceiptMiniRequestDto requestDto) throws Exception {

        EReceipt eReceipt = null;
        EReceipt savedEReceipt = null;

        TransactionCore transactionCore = transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction Not Found"));

//        EReceipt eReceipt = eReceiptRepository.findByTransactionCore_IdAndReceiptType(id, "customer_copy").orElseThrow(() -> new NotFoundException("e-Receipt Info Not Found"));

        Terminal terminal = terminalRepository.findByTerminalId(transactionCore.getTerminalId()).orElseThrow(() -> new NotFoundException("Terminal Not Found"));
        Merchant merchant = merchantRepository.findByMerchantId(transactionCore.getMerchantId()).orElseThrow(() -> new NotFoundException("Merchant Not Found"));


        if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail()) ||
                requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
            eReceipt = new EReceipt(transactionCore, requestDto.getEmail(), requestDto.getContactNo(), "customer_copy", false, false);
            savedEReceipt = eReceiptRepository.save(eReceipt);

            EReceiptDataDto eReceiptCustomer = new EReceiptDataDto(
                    savedEReceipt.getId(),
                    transactionCore.getMerchantId(),
                    transactionCore.getTerminalId(),
                    transactionCore.getTranType(),
                    transactionCore.getPaymentMode(),
                    transactionCore.getCardLabel(),
                    transactionCore.getInvoiceNo(),
                    transactionCore.getAuthCode(),
                    transactionCore.getCurrency(),
                    transactionCore.getAmount(),
                    transactionCore.getPan(),
                    requestDto.getEmail(),
                    requestDto.getContactNo(),
                    transactionCore.getDateTime(),
                    transactionCore.getSignData()
            );
            if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail())) {
                emailService.sendEmail(eReceiptCustomer.getEmail(), eReceiptCustomer);
            }
            if (requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
                smsService.sendSms(eReceiptCustomer.getContactNo(), eReceiptCustomer);
            }
        }

//        Device device = deviceRepository.findById(terminal.getDevice().getId()).orElseThrow(() -> new NotFoundException("Device Not Found"));

        return "e-Receipt Sent Successfully";
    }

    @Override
    public void settlement(SettlementRequestDto requestDto) throws Exception {

        if (requestDto.getSettleAcquirers() == null || requestDto.getSettleAcquirers().isEmpty()) {
            throw new NotFoundException("No Terminals for settle");
        }
        Device device = deviceRepository.findBySerialNo(requestDto.getDeviceSerialNo()).orElseThrow(() -> new NotFoundException("Device Not Found"));

        if (!"ACTV".equals(device.getStatus().getStatusCode())) {
            throw new NotFoundException("Device is not Active");
        }

        if (device.getDeviceType().equals(DeviceTypeEnum.MPOS)) {
            /*
             do MPOS related validation
             need to find a way to trigger the device type
             */

        }
        requestDto.getSettleAcquirers().stream().forEach((settleObj) -> {

            Merchant merchant = merchantRepository.findByMerchantId(settleObj.getMerchantId()).orElseThrow(() -> new NotFoundException("Merchant Not Found"));
            if (!"ACTV".equals(merchant.getStatus().getStatusCode())) {
                throw new NotFoundException("Merchant is not Active");
            }
            Terminal terminal = terminalRepository.findByTerminalId(settleObj.getTerminalId()).orElseThrow(() -> new NotFoundException("Terminal Not Found"));
            if (!"ACTV".equals(terminal.getStatus().getStatusCode())) {
                throw new NotFoundException("Terminal is not Active");
            }

            if (settleObj.getSettledMethod() != SettlementMethodEnum.AUTO.getValue() &&
                    settleObj.getSettledMethod() != SettlementMethodEnum.MANUAL.getValue()) {
                throw new NotFoundException("Invalid Settlement Method");
            }
            transactionRepository.updateRecordsWithCondition(true, settleObj.getSettledMethod(), requestDto.getOriginId(),
                    settleObj.getMerchantId(), settleObj.getTerminalId(), settleObj.getBatchNo());
        });

    }

    @Override
    public TransactionResponseDto create(TransactionRequestDto requestDto) throws Exception {

        EReceipt eReceipt = null;
        EReceipt savedEReceipt = null;
        TransactionCore toInsert = TransactionMapper.toModel(requestDto);
        TransactionCore savedEntity = transactionRepository.save(toInsert);

        if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail()) ||
                requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
            eReceipt = new EReceipt(savedEntity, requestDto.getEmail(), requestDto.getContactNo(), "customer_copy", false, false);
            savedEReceipt = eReceiptRepository.save(eReceipt);

            EReceiptDataDto eReceiptCustomer = new EReceiptDataDto(
                    savedEReceipt.getId(),
                    savedEntity.getMerchantId(),
                    savedEntity.getTerminalId(),
                    savedEntity.getTranType(),
                    savedEntity.getPaymentMode(),
                    savedEntity.getCardLabel(),
                    savedEntity.getInvoiceNo(),
                    savedEntity.getAuthCode(),
                    savedEntity.getCurrency(),
                    savedEntity.getAmount(),
                    savedEntity.getPan(),
                    eReceipt.getEmail(),
                    eReceipt.getContactNo(),
                    savedEntity.getDateTime(),
                    savedEntity.getSignData()
            );
            if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail())) {
                emailService.sendEmail(eReceiptCustomer.getEmail(), eReceiptCustomer);
            }
            if (requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
                smsService.sendSms(eReceiptCustomer.getContactNo(), eReceiptCustomer);
            }
        }

        TransactionResponseDto responseDto = TransactionMapper.toDto(savedEntity);

        Merchant merchant = merchantRepository.findByMerchantId(requestDto.getMerchantId()).orElseThrow(() -> new NotFoundException("Merchant Not Found"));
        if (merchant.getIsEmailEnabled() || merchant.getIsSmsEnabled()) {
            eReceipt = new EReceipt(savedEntity, merchant.getEmail(), merchant.getContactNo(), "merchant_copy", false, false);
            savedEReceipt = eReceiptRepository.save(eReceipt);
            EReceiptDataDto eReceiptMerchant = new EReceiptDataDto(
                    savedEReceipt.getId(),
                    savedEntity.getMerchantId(),
                    savedEntity.getTerminalId(),
                    savedEntity.getTranType(),
                    savedEntity.getPaymentMode(),
                    savedEntity.getCardLabel(),
                    savedEntity.getInvoiceNo(),
                    savedEntity.getAuthCode(),
                    savedEntity.getCurrency(),
                    savedEntity.getAmount(),
                    savedEntity.getPan(),
                    merchant.getEmail(),
                    merchant.getContactNo(),
                    savedEntity.getDateTime(),
                    savedEntity.getSignData()
            );
            if (merchant.getIsEmailEnabled()) {
                emailService.sendEmail(eReceiptMerchant.getEmail(), eReceiptMerchant);
            }
            if (merchant.getIsSmsEnabled()) {
                smsService.sendSms(eReceiptMerchant.getContactNo(), eReceiptMerchant);
            }
        }
        return responseDto;
    }

    @Override
    public TransactionResponseDto createFailed(TransactionRequestDto requestDto) throws Exception {
        TransactionCoreFailed toInsert = TransactionFailedMapper.toModel(requestDto);
        TransactionCoreFailed savedEntity = transactionFailedRepository.save(toInsert);
        TransactionResponseDto responseDto = TransactionFailedMapper.toDto(savedEntity);

        return responseDto;
    }

    @Override
    public SettlementResponseDto updateSettlement(SettlementRequestDto requestDto) throws Exception {
//        TransactionCoreFailed toInsert = TransactionFailedMapper.toModel(requestDto);
//        TransactionCoreFailed savedEntity = transactionFailedRepository.save(toInsert);
//        TransactionResponseDto responseDto = TransactionFailedMapper.toDto(savedEntity);

        return null;
    }


    @Override
    public Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Page<TransactionCore> entitiesPage = transactionRepository.findByDateTimeBetween(dateFormat.parse(dateFrom), dateFormat.parse(dateTo),
                pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Transactions found");
        }
        return entitiesPage.map(TransactionCoreMapper::toDto);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {

        ArrayList<Map<String, Object>> countList = new ArrayList();
        ArrayList<Map<String, Object>> amountList = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String whereClause = setWhereCondition(dateFrom, dateTo);
        String selectClause = setSelectCondition(queryBy);
        String groupByClause = setGroupByCondition(queryBy);


        String jpql = "SELECT " + selectClause + " FROM TransactionCore p INNER JOIN Merchant m ON p.merchantId=m.merchantId " +
                " WHERE " + whereClause + " GROUP BY " + groupByClause;

        Query query = entityManager.createQuery(jpql);

        if ((dateFrom != null && !dateFrom.isEmpty())
                && (dateTo != null && !dateTo.isEmpty())) {
            query.setParameter("fromDate", dateFormat.parse(dateFrom));
            query.setParameter("toDate", dateFormat.parse(dateTo));
        } else {
        }

        List<Object[]> list = query.getResultList();

        list.forEach(i -> {
            Map<String, Object> countMap = new HashMap<>();
            Map<String, Object> amountMap = new HashMap<>();
            String label = (String) i[0];
            Long count = (Long) i[1];
            Long amount = (Long) i[2];

            countMap.put("count", count);
            if ((queryBy != null && !"".equals(queryBy))) {
                if ("CardLabel".equalsIgnoreCase(queryBy)) {
                    countMap.put("cardLabel", label);
                }
                if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                    countMap.put("paymentMode", label);
                }
            }
            amountMap.put("total", amount);
            if ((queryBy != null && !"".equals(queryBy))) {
                if ("CardLabel".equalsIgnoreCase(queryBy)) {
                    amountMap.put("cardLabel", label);
                }
                if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                    amountMap.put("paymentMode", label);
                }
            }
            countList.add(countMap);
            amountList.add(amountMap);
        });


        Map<String, ArrayList<Map<String, Object>>> returnMap = new HashMap<>();
        returnMap.put("totalCount", countList);
        returnMap.put("totalAmount", amountList);

        return returnMap;
    }

    private String setGroupByCondition(String queryBy) throws Exception {

        String groupBy = " ";
        if (queryBy != null && !"".equals(queryBy)) {
            if ("CardLabel".equalsIgnoreCase(queryBy)) {
                groupBy += " p.cardLabel";
            }
            if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                groupBy += " p.paymentMode";
            }

        } else {
        }

        return groupBy;
    }

    private String setSelectCondition(String queryBy) throws Exception {

        String select = " ";

        if ((queryBy != null && !"".equals(queryBy))) {
            if ("CardLabel".equalsIgnoreCase(queryBy)) {
                select += " p.cardLabel,count(p) ,sum(p.amount) ";
            }
            if ("PaymentMode".equalsIgnoreCase(queryBy)) {
                select += " p.paymentMode,count(p),sum(p.amount) ";
            }
        } else {
        }
        return select;
    }

    private String setWhereCondition(String dateFrom, String dateTo) throws Exception {

        String where = " 1=1 ";

        if ((dateFrom != null && !dateFrom.isEmpty())
                && (dateTo != null && !dateTo.isEmpty())) {
            where += " AND p.dateTime BETWEEN :fromDate AND :toDate ";
        } else {
        }
        return where;
    }

}
