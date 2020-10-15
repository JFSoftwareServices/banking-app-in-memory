package com.jfservices.bankingapp.repository;

import com.jfservices.bankingapp.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository {
    Optional<Account> findByAccountNumber(String accountNumber);

    void save(Account account);

    List<Account> findAll();
}