package com.jfservices.bankingapp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Transaction {

    private static final long serialVersionUID = 1L;

    private Long transactionId;

    private String debitAccountNumber;

    private String creditAccountNumber;

    private BigDecimal transactionAmount;

    private List<Account> accounts = new ArrayList<>();

    public Transaction(String debitAccountNumber, String creditAccountNumber, BigDecimal amount) {
        this.debitAccountNumber = debitAccountNumber;
        this.creditAccountNumber = creditAccountNumber;
        this.transactionAmount = amount;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    private LocalDateTime transactionDateTime = LocalDateTime.now();
}