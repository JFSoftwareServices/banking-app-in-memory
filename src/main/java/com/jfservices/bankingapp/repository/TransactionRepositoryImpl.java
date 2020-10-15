package com.jfservices.bankingapp.repository;

import com.jfservices.bankingapp.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class TransactionRepositoryImpl implements TransactionRepository {

    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        return transactions
                .stream()
                .filter(byDebitOrCreditAccountsEquals(accountNumber))
                .collect(Collectors.toList());
    }

    private Predicate<Transaction> byDebitOrCreditAccountsEquals(String accountNumber) {
        return t -> t.getDebitAccountNumber().equals(accountNumber) || t.getCreditAccountNumber().equals(accountNumber);
    }

    @Override
    public void save(Transaction transaction) {
       transactions.add(transaction);
    }
}