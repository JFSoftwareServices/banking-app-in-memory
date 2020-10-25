package com.jfservices.bankingapp.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ResponseAccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountNumber;
    private BigDecimal currentBalance;
    private List<ResponseTransferTransactionDTO> transactions;
}