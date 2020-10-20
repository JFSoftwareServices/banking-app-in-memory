package com.jfservices.bankingapp.service;


import com.jfservices.bankingapp.dto.request.RequestTransferBalanceDTO;
import com.jfservices.bankingapp.dto.response.ResponseStatementDTO;
import com.jfservices.bankingapp.dto.response.ResponseTransactionDTO;
import com.jfservices.bankingapp.entity.Account;
import com.jfservices.bankingapp.entity.Transaction;
import com.jfservices.bankingapp.repository.AccountRepository;
import com.jfservices.bankingapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.jfservices.bankingapp.service.ModelMapperService.mapList;


@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
    public Account create(Account account) throws InterruptedException {
        if (lock.writeLock().tryLock(1, TimeUnit.SECONDS)) {
            try {
                accountRepository.save(account);
                return accountRepository.findByAccountNumber(account.getAccountNumber())
                        .orElseThrow(() -> (new AccountNotFoundException(account.getAccountNumber())));
            } finally {
                lock.writeLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException();
        }
    }

    @Override
    public List<Account> findAll() throws InterruptedException {
        if (lock.readLock().tryLock(1, TimeUnit.SECONDS)) {
            try {
                return accountRepository.findAll();
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException();
        }
    }

    @Override
    public Account findByAccountNumber(String accountNumber) throws InterruptedException {
        if (lock.readLock().tryLock(1, TimeUnit.SECONDS)) {
            try {
                return accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() -> (new AccountNotFoundException(accountNumber)));
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException();
        }
    }

    @Override
    public Transaction sendMoney(RequestTransferBalanceDTO transferBalanceRequest) throws InterruptedException {
        if (lock.writeLock().tryLock(1, TimeUnit.SECONDS)) {
            try {
                String debitAccountNumber = transferBalanceRequest.getDebitAccountNumber();
                String creditAccountNumber = transferBalanceRequest.getCreditAccountNumber();
                BigDecimal amount = transferBalanceRequest.getAmount();

                Account debitAccount = accountRepository.findByAccountNumber(debitAccountNumber)
                        .orElseThrow(() -> (new AccountNotFoundException(debitAccountNumber)));

                if (amount.compareTo(debitAccount.getCurrentBalance()) > 0) {
                    throw new InsufficientFundsException(debitAccount);
                }

                Account creditAccount = accountRepository.findByAccountNumber(creditAccountNumber)
                        .orElseThrow(() -> (new AccountNotFoundException(creditAccountNumber)));

                creditAccount.transferTo(debitAccount, amount);

                Transaction transaction =
                        new Transaction(debitAccount.getAccountNumber(), creditAccount.getAccountNumber(), amount);
                debitAccount.addTransaction(transaction);
                transaction.addAccount(debitAccount);
                creditAccount.addTransaction(transaction);
                transaction.addAccount(creditAccount);

                transactionRepository.save(transaction);

                return transaction;

            } finally {
                lock.writeLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException();
        }
    }

    @Override
    public ResponseStatementDTO getStatement(String accountNumber) throws InterruptedException {
        if (lock.readLock().tryLock(1, TimeUnit.SECONDS)) {
            try {
                Account account = accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() -> (new AccountNotFoundException(accountNumber)));

                List<ResponseTransactionDTO> responseTransactionDTOS = mapList(account.getTransactions(),
                        ResponseTransactionDTO.class);

                return ResponseStatementDTO.builder()
                        .currentBalance(account.getCurrentBalance())
                        .transactionHistory(responseTransactionDTOS).build();

            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException();
        }
    }
}