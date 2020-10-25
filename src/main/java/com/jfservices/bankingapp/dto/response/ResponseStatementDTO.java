package com.jfservices.bankingapp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ResponseStatementDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal currentBalance;
    private List<ResponseTransferTransactionDTO> transactionHistory;
}