package com.jfservices.bankingapp.repository;

import com.jfservices.bankingapp.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository {
    List<Transaction> findByAccountNumber(String accountNumber);

    void save(Transaction transaction);
}