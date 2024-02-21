package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.*;
import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.mapper.TransactionCoreMapper;
import com.cba.core.wirememobile.mapper.TransactionFailedMapper;
import com.cba.core.wirememobile.mapper.TransactionMapper;
import com.cba.core.wirememobile.model.*;
import com.cba.core.wirememobile.service.EmailService;
import com.cba.core.wirememobile.service.LocationService;
import com.cba.core.wirememobile.service.SmsService;
import com.cba.core.wirememobile.service.TransactionService;
import com.cba.core.wirememobile.util.DeviceTypeEnum;
import com.cba.core.wirememobile.util.SettlementMethodEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final TerminalDao terminalDao;
    private final MerchantDao merchantDao;
    private final EReceiptDao eReceiptDao;
    private final DeviceDao deviceDao;
    private final EmailService emailService;
    private final SmsService smsService;
    private final LocationService locationService;


    @Override
    public void settlement(SettlementRequestDto requestDto) throws Exception {

        if (requestDto.getSettleAcquirers() == null || requestDto.getSettleAcquirers().isEmpty()) {
            throw new NotFoundException("No Terminals for settle");
        }
        Device device = deviceDao.findBySerialNo(requestDto.getDeviceSerialNo());

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

            Terminal terminal = terminalDao.findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo
                    (settleObj.getTerminalId(), settleObj.getMerchantId(), requestDto.getDeviceSerialNo());

            Merchant merchant = merchantDao.findByMerchantId(settleObj.getMerchantId());

            if (!"ACTV".equals(merchant.getStatus().getStatusCode())) {
                throw new NotFoundException("Merchant is not Active");
            }
            if (!"ACTV".equals(terminal.getStatus().getStatusCode())) {
                throw new NotFoundException("Terminal is not Active");
            }
            if (settleObj.getSettledMethod() != SettlementMethodEnum.AUTO.getValue() &&
                    settleObj.getSettledMethod() != SettlementMethodEnum.MANUAL.getValue()) {
                throw new NotFoundException("Invalid Settlement Method");
            }

            transactionDao.updateRecordsWithCondition(true, settleObj.getSettledMethod(), requestDto.getOriginId(),
                    settleObj.getMerchantId(), settleObj.getTerminalId(), settleObj.getBatchNo());

            SettlementEmailDto settlementEmailDto = new SettlementEmailDto();
            settlementEmailDto.setSubject("Wire-me Settlement Report with TID :" + terminal.getTerminalId());
            settlementEmailDto.setMid(merchant.getMerchantId());
            settlementEmailDto.setTid(terminal.getTerminalId());
            settlementEmailDto.setMerchantName(merchant.getName());
            settlementEmailDto.setTo(merchant.getEmail());
            settlementEmailDto.setMerchantAddress(merchant.getAddress());
            settlementEmailDto.setTimestamp("");
            settlementEmailDto.setAmount(settleObj.getSaleAmount().toString());
            settlementEmailDto.setBatchNo(settleObj.getBatchNo());
            settlementEmailDto.setSaleAmount(settleObj.getSaleAmount());
            settlementEmailDto.setSaleCount(settleObj.getSaleCount());
            settlementEmailDto.setSaleVoidAmount(settleObj.getSaleVoidAmount());
            settlementEmailDto.setSaleVoidCount(settleObj.getSaleVoidCount());
            settlementEmailDto.setPrecompAmount(settleObj.getPrecompAmount());
            settlementEmailDto.setPrecompCount(settleObj.getPrecompCount());
            settlementEmailDto.setPrecompVoidAmount(settleObj.getPrecompVoidAmount());
            settlementEmailDto.setPrecompVoidCount(settleObj.getPrecompVoidCount());
            settlementEmailDto.setOfflineAmount(settleObj.getOfflineAmount());
            settlementEmailDto.setOfflineCount(settleObj.getOfflineCount());
            settlementEmailDto.setOfflineVoidAmount(settleObj.getOfflineVoidAmount());
            settlementEmailDto.setOfflineVoidCount(settleObj.getOfflineVoidCount());
            emailService.sendEmail(settlementEmailDto);

        });


    }

    @Override
    public String generateEReceipt(EReceiptRequestDto requestDto) throws Exception {
        EReceipt eReceipt = null;
        EReceipt savedEReceipt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        TransactionCore transactionCore = transactionDao.findByRrnAndInvoiceNoAndTraceNo(
                requestDto.getSerialNo(),
                requestDto.getRrn(),
                requestDto.getInvoiceNo(),
                requestDto.getTraceNo(),
                dateFormat.parse(requestDto.getDateTime()));

        Terminal terminal = terminalDao.findByTerminalId(transactionCore.getTerminalId());
        Merchant merchant = merchantDao.findByMerchantId(transactionCore.getMerchantId());

        if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail()) ||
                requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
            eReceipt = new EReceipt(transactionCore, requestDto.getEmail(),
                    requestDto.getContactNo(), "customer_copy", false, false);
            savedEReceipt = eReceiptDao.create(eReceipt);

            DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
            String formattedAmount = currencyFormat.format(transactionCore.getAmount());
            List<String> values = Arrays.asList("void-sale", "void-offline", "void-precomp", "void-preauth");
            if (values.stream().anyMatch(transactionCore.getTranType()::equals)) {
                formattedAmount = "-" + formattedAmount;
            }
            EReceiptDataDto eReceiptCustomer = new EReceiptDataDto(
                    "card-holder-copy",
                    merchant.getName(),
                    merchant.getDistrict(),
                    transactionCore.getMerchantId(),
                    transactionCore.getTerminalId(),
                    formattedAmount,
                    transactionCore.getDateTime().toString(),
                    transactionCore.getTranType(),
                    transactionCore.getCardLabel(),
                    transactionCore.getExpDate(),
                    transactionCore.getInvoiceNo(),
                    transactionCore.getAuthCode(),
                    transactionCore.getBatchNo(),
                    transactionCore.getRrn(),
                    transactionCore.getCurrency(),
                    transactionCore.getPan(),
                    requestDto.getEmail(),
                    "Wire-me Transaction Receipt(Customer)",
                    transactionCore.getSignData()
            );
            if (eReceipt.getEmail() != null && !"".equals(eReceipt.getEmail())) {
                emailService.sendEmail(savedEReceipt.getId(), eReceiptCustomer);
            }
            if (eReceipt.getContactNo() != null && !"".equals(eReceipt.getContactNo())) {
                smsService.sendSms(requestDto.getContactNo(), eReceiptCustomer);
            }
        }
        return "e-Receipt Sent Successfully";
    }

    @Override
    public String generateEReceipt(int id, EReceiptMiniRequestDto requestDto) throws Exception {
        EReceipt eReceipt = null;
        EReceipt savedEReceipt = null;

        TransactionCore transactionCore = transactionDao.findById(id);

        Terminal terminal = terminalDao.findByTerminalId(transactionCore.getTerminalId());
        Merchant merchant = merchantDao.findByMerchantId(transactionCore.getMerchantId());

        if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail()) ||
                requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
            eReceipt = new EReceipt(transactionCore, requestDto.getEmail(), requestDto.getContactNo(), "customer_copy", false, false);
            savedEReceipt = eReceiptDao.create(eReceipt);

            DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
            String formattedAmount = currencyFormat.format(transactionCore.getAmount());
            List<String> values = Arrays.asList("void-sale", "void-offline", "void-precomp", "void-preauth");
            if (values.stream().anyMatch(transactionCore.getTranType()::equals)) {
                formattedAmount = "-" + formattedAmount;
            }
            EReceiptDataDto eReceiptCustomer = new EReceiptDataDto(
                    "card-holder-copy",
                    merchant.getName(),
                    merchant.getDistrict(),
                    transactionCore.getMerchantId(),
                    transactionCore.getTerminalId(),
                    formattedAmount,
                    transactionCore.getDateTime().toString(),
                    transactionCore.getTranType(),
                    transactionCore.getCardLabel(),
                    transactionCore.getExpDate(),
                    transactionCore.getInvoiceNo(),
                    transactionCore.getAuthCode(),
                    transactionCore.getBatchNo(),
                    transactionCore.getRrn(),
                    transactionCore.getCurrency(),
                    transactionCore.getPan(),
                    requestDto.getEmail(),
                    "Wire-me Transaction Receipt(Customer)",
                    transactionCore.getSignData()
            );
            if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail())) {
                emailService.sendEmail(savedEReceipt.getId(), eReceiptCustomer);
            }
            if (requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
                smsService.sendSms(requestDto.getContactNo(), eReceiptCustomer);
            }
        }

        return "e-Receipt Sent Successfully";
    }

    @Override
    public TransactionResponseDto create(TransactionRequestDto requestDto) throws Exception {
        EReceipt eReceipt = null;
        EReceipt savedEReceipt = null;
        TransactionCore toInsert = TransactionMapper.toModel(requestDto);

        Terminal terminal = terminalDao.findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo
                (requestDto.getTerminalId(), requestDto.getMerchantId(), requestDto.getDeviceSerialNo());


        Merchant merchant = merchantDao.findByMerchantId(requestDto.getMerchantId());
        Device device = deviceDao.findByTransactionTerminal(requestDto.getTerminalId(), requestDto.getDeviceSerialNo());


        boolean isLocationOutOfRange = locationService.isLocationOutOfRange(
                requestDto.getLat(), requestDto.getLng(),
                merchant.getLat(), merchant.getLon(), merchant.getRadius()
        );

        if (isLocationOutOfRange) {
            toInsert.setIsAway(true);
            device.setIsAway(true);
        } else {
            toInsert.setIsAway(false);
            device.setIsAway(false);
        }

        device.setLat(toInsert.getLat());
        device.setLon(toInsert.getLon());
        device.setLastActive(toInsert.getDateTime());


        TransactionCore transactionCore = transactionDao.create(toInsert);
        deviceDao.create(device);

        if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail()) ||
                requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
            eReceipt = new EReceipt(transactionCore, requestDto.getEmail(), requestDto.getContactNo(), "customer_copy", false, false);
            savedEReceipt = eReceiptDao.create(eReceipt);

            DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
            String formattedAmount = currencyFormat.format(transactionCore.getAmount());

            List<String> values = Arrays.asList("void-sale", "void-offline", "void-precomp", "void-preauth");
            if (values.stream().anyMatch(transactionCore.getTranType()::equals)) {
                formattedAmount = "-" + formattedAmount;
            }
            EReceiptDataDto eReceiptCustomer = new EReceiptDataDto(
                    "card-holder-copy",
                    merchant.getName(),
                    merchant.getDistrict(),
                    transactionCore.getMerchantId(),
                    transactionCore.getTerminalId(),
                    formattedAmount,
                    transactionCore.getDateTime().toString(),
                    transactionCore.getTranType(),
                    transactionCore.getCardLabel(),
                    transactionCore.getExpDate(),
                    transactionCore.getInvoiceNo(),
                    transactionCore.getAuthCode(),
                    transactionCore.getBatchNo(),
                    transactionCore.getRrn(),
                    transactionCore.getCurrency(),
                    transactionCore.getPan(),
                    eReceipt.getEmail(),
                    "Wire-me Transaction Receipt(Customer)",
                    transactionCore.getSignData()
            );
            if (requestDto.getEmail() != null && !"".equals(requestDto.getEmail())) {
                emailService.sendEmail(savedEReceipt.getId(), eReceiptCustomer);
            }
            if (requestDto.getContactNo() != null && !"".equals(requestDto.getContactNo())) {
                smsService.sendSms(requestDto.getContactNo(), eReceiptCustomer);
            }
        }

        TransactionResponseDto responseDto = TransactionMapper.toDto(transactionCore);

        if (merchant.getIsEmailEnabled() || merchant.getIsSmsEnabled()) {
            eReceipt = new EReceipt(transactionCore, merchant.getEmail(), merchant.getContactNo(), "merchant_copy", false, false);
            savedEReceipt = eReceiptDao.create(eReceipt);

            DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
            String formattedAmount = currencyFormat.format(transactionCore.getAmount());
            List<String> values = Arrays.asList("void-sale", "void-offline", "void-precomp", "void-preauth");
            if (values.stream().anyMatch(transactionCore.getTranType()::equals)) {
                formattedAmount = "-" + formattedAmount;
            }
            EReceiptDataDto eReceiptMerchant = new EReceiptDataDto(
                    "merchant-copy",
                    merchant.getName(),
                    merchant.getDistrict(),
                    transactionCore.getMerchantId(),
                    transactionCore.getTerminalId(),
                    formattedAmount,
                    transactionCore.getDateTime().toString(),
                    transactionCore.getTranType(),
                    transactionCore.getCardLabel(),
                    transactionCore.getExpDate(),
                    transactionCore.getInvoiceNo(),
                    transactionCore.getAuthCode(),
                    transactionCore.getBatchNo(),
                    transactionCore.getRrn(),
                    transactionCore.getCurrency(),
                    transactionCore.getPan(),
                    merchant.getEmail(),
                    "Wire-me Transaction Receipt(Merchant)",
                    transactionCore.getSignData()
            );
            if (merchant.getIsEmailEnabled()) {
                emailService.sendEmail(savedEReceipt.getId(), eReceiptMerchant);
            }
            if (merchant.getIsSmsEnabled()) {
                smsService.sendSms(merchant.getContactNo(), eReceiptMerchant);
            }
        }
        return responseDto;
    }

    @Override
    public TransactionResponseDto createFailed(TransactionRequestDto requestDto) throws Exception {


        Terminal terminal = terminalDao.findByTerminalIdAndMerchant_MerchantIdAndDevice_SerialNo
                (requestDto.getTerminalId(), requestDto.getMerchantId(), requestDto.getDeviceSerialNo());
//        Merchant merchant = merchantDao.findByMerchantId(requestDto.getMerchantId());
//        Device device = deviceDao.findByTransactionTerminal(requestDto.getTerminalId(), requestDto.getDeviceSerialNo());

        TransactionCoreFailed toInsert = TransactionFailedMapper.toModel(requestDto);
        TransactionCoreFailed savedEntity = transactionDao.createFailed(toInsert);
        TransactionResponseDto responseDto = TransactionFailedMapper.toDto(savedEntity);

        return responseDto;
    }

    @Override
    public Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Page<TransactionCore> entitiesPage = transactionDao.getAllTransactions(dateFrom, dateTo,
                page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Transactions found");
        }
        return entitiesPage.map(TransactionCoreMapper::toDto);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {

        ArrayList<Map<String, Object>> countList = new ArrayList();
        ArrayList<Map<String, Object>> amountList = new ArrayList();

        String whereClause = setWhereCondition(dateFrom, dateTo);
        String selectClause = setSelectCondition(queryBy);
        String groupByClause = setGroupByCondition(queryBy);


        List<Object[]> list = transactionDao.getAllTransactionSummary(dateFrom, dateTo, queryBy,
                whereClause, selectClause, groupByClause);

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
