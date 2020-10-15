package com.jfservices.bankingapp.service;


import com.jfservices.bankingapp.entity.Account;

public class InsufficientFundsException extends RuntimeException {
    InsufficientFundsException(Account debitAccountNumber) {
        super("Insufficient funds in account: " + debitAccountNumber);
    }
}