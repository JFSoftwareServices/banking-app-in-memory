package com.jfservices.bankingapp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {

    private static final long serialVersionUID = 1L;

    private Long transactionId;

    private String debitAccountNumber;

    private String creditAccountNumber;

    private BigDecimal transactionAmount;

    public Transaction(String debitAccountNumber, String creditAccountNumber, BigDecimal amount) {
        this.debitAccountNumber = debitAccountNumber;
        this.creditAccountNumber = creditAccountNumber;
        this.transactionAmount = amount;
    }

    private LocalDateTime transactionDateTime = LocalDateTime.now();
}