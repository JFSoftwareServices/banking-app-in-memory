package com.jfservices.bankingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "transactions"})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;

    private BigDecimal currentBalance;

    private List<Transaction> transactions = new ArrayList<>();

    private final Lock lock;

    public Account() {
        this.lock = new ReentrantLock();
    }

    public void debit(BigDecimal amount) {
        currentBalance = currentBalance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        currentBalance = currentBalance.add(amount);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}