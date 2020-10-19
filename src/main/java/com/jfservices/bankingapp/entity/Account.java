package com.jfservices.bankingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "transactions"})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;

    private BigDecimal currentBalance;

    private List<Transaction> transactions = new ArrayList<>();


    public void debit(BigDecimal amount) {
        currentBalance = currentBalance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        currentBalance = currentBalance.add(amount);
    }

    public Transaction transferTo(Account creditAccount, BigDecimal amount) throws InterruptedException {
        Transaction transaction =
                new Transaction(this.getAccountNumber(), creditAccount.getAccountNumber(), amount);
        this.debit(amount);
        creditAccount.credit(amount);
        this.addTransaction(transaction);
        transaction.addAccount(this);
        creditAccount.addTransaction(transaction);
        transaction.addAccount(creditAccount);
        return transaction;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}