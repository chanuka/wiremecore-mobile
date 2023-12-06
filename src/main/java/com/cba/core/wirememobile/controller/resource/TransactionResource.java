package com.cba.core.wirememobile.controller.resource;

import com.cba.core.wirememobile.dto.*;
import com.cba.core.wirememobile.util.PaginationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public interface TransactionResource {


    @GetMapping("/summary-device")
    ResponseEntity<Map<String, ArrayList<Map<String, Object>>>> getAllDeviceTransactionSummary(@RequestParam(defaultValue = "") String dateFrom,
                                                                                         @RequestParam(defaultValue = "") String dateTo,
                                                                                         @RequestParam(defaultValue = "") String queryBy,
                                                                                         @RequestParam(defaultValue = "") String originId,
                                                                                         @RequestParam(defaultValue = "") String deviceSerial) throws Exception;

    @GetMapping("/detailed-device")
    ResponseEntity<PaginationResponse<TransactionCoreResponseDto>> getAllDeviceTransactions(@RequestParam(defaultValue = "") String dateFrom,
                                                                                      @RequestParam(defaultValue = "") String dateTo,
                                                                                      @RequestParam(defaultValue = "") String originId,
                                                                                      @RequestParam(defaultValue = "") String deviceSerial,
                                                                                      @RequestParam(defaultValue = "") int isSettled,
                                                                                             @RequestParam(defaultValue = "0") int page,
                                                                                             @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @PutMapping("/settle-state")
    ResponseEntity<SettlementResponseDto> settleTransaction(@Valid @RequestBody SettlementRequestDto requestDto) throws Exception;

    @PostMapping("/failed")
    ResponseEntity<TransactionResponseDto> createFailedTransaction(@Valid @RequestBody TransactionRequestDto requestDto) throws Exception;

    @PostMapping()
    ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto) throws Exception;

    @GetMapping()
    ResponseEntity<PaginationResponse<TransactionCoreResponseDto>> getAllTransactions(@RequestParam(defaultValue = "") String dateFrom,
                                                                                      @RequestParam(defaultValue = "") String dateTo,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("summary")
    ResponseEntity<Map<String, ArrayList<Map<String, Object>>>> getAllTransactionSummary(@RequestParam(defaultValue = "") String dateFrom,
                                                                                         @RequestParam(defaultValue = "") String dateTo,
                                                                                         @RequestParam(defaultValue = "") String queryBy) throws Exception;
}
