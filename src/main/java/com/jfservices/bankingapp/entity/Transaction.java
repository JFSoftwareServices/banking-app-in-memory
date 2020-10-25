package com.jfservices.bankingapp.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {

    private static final long serialVersionUID = 1L;

    private Long transactionId;

    private String debitAccountNumber;

    private String creditAccountNumber;

    private BigDecimal transactionAmount;

    private LocalDateTime transactionDateTime;
}