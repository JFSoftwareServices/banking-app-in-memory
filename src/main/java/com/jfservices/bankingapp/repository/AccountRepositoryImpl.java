package com.jfservices.bankingapp.repository;

import com.jfservices.bankingapp.entity.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AccountRepositoryImpl implements AccountRepository {

    private List<Account> accounts = new ArrayList<>();

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accounts
                .stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    @Override
    public void save(Account account) {
        accounts.add(account);
    }

    @Override
    public List<Account> findAll() {
        return accounts;
    }
}