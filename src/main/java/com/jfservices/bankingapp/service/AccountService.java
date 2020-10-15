package com.jfservices.bankingapp.service;


import com.jfservices.bankingapp.dto.request.RequestTransferBalanceDTO;
import com.jfservices.bankingapp.dto.response.ResponseStatementDTO;
import com.jfservices.bankingapp.entity.Account;
import com.jfservices.bankingapp.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    List<Account> findAll();

    Account create(Account account);

    Account findByAccountNumber(String accountNumber);

    Transaction sendMoney(RequestTransferBalanceDTO transferBalanceRequest) throws Exception;

    ResponseStatementDTO getStatement(String accountNumber) throws InterruptedException;
}