package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.TransactionDao;
import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.mapper.TransactionCoreMapper;
import com.cba.core.wirememobile.mapper.TransactionFailedMapper;
import com.cba.core.wirememobile.mapper.TransactionMapper;
import com.cba.core.wirememobile.model.TransactionCore;
import com.cba.core.wirememobile.model.TransactionCoreFailed;
import com.cba.core.wirememobile.repository.TransactionFailedRepository;
import com.cba.core.wirememobile.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TransactionResponseDto create(TransactionRequestDto requestDto) throws Exception {

        TransactionCore toInsert = TransactionMapper.toModel(requestDto);
        TransactionCore savedEntity = transactionRepository.save(toInsert);
        TransactionResponseDto responseDto = TransactionMapper.toDto(savedEntity);

//        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
//                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
//                userBeanUtil.getRemoteAdr()));

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
