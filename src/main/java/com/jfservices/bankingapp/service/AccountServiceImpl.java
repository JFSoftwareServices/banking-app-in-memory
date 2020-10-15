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

import static com.jfservices.bankingapp.service.ModelMapperService.mapList;


@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account create(Account account) {
        accountRepository.save(account);
        return accountRepository.findByAccountNumber(account.getAccountNumber())
                .orElseThrow(() -> (new AccountNotFoundException(account.getAccountNumber())));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> (new AccountNotFoundException(accountNumber)));
    }

    @Override
    public Transaction sendMoney(RequestTransferBalanceDTO transferBalanceRequest) throws InterruptedException {
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

        return sendMoney(amount, debitAccount, creditAccount);
    }

    private Transaction sendMoney(BigDecimal amount, Account debitAccount, Account creditAccount)
            throws InterruptedException {
        boolean acquiredDebitAccountLock = debitAccount.getLock().tryLock(1, TimeUnit.SECONDS);
        boolean acquiredCreditAccountLock = creditAccount.getLock().tryLock(1, TimeUnit.SECONDS);

        if (acquiredDebitAccountLock && acquiredCreditAccountLock) {
            try {
                debitAccount.debit(amount);
                creditAccount.credit(amount);

                accountRepository.save(debitAccount);
                accountRepository.save(creditAccount);

                Transaction transaction =
                        new Transaction(debitAccount.getAccountNumber(), creditAccount.getAccountNumber(), amount);
                debitAccount.addTransaction(transaction);
                transaction.addAccount(debitAccount);
                creditAccount.addTransaction(transaction);
                transaction.addAccount(creditAccount);

                transactionRepository.save(transaction);

                return transaction;
            } finally {
                debitAccount.getLock().unlock();
                creditAccount.getLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException(debitAccount, creditAccount);
        }
    }

    @Override
    public ResponseStatementDTO getStatement(String accountNumber) throws InterruptedException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> (new AccountNotFoundException(accountNumber)));
        boolean acquiredAccountLock = account.getLock().tryLock(1, TimeUnit.SECONDS);
        if (acquiredAccountLock) {
            try {
                List<ResponseTransactionDTO> responseTransactionDTOS = mapList(account.getTransactions(),
                        ResponseTransactionDTO.class);

                return ResponseStatementDTO.builder()
                        .currentBalance(account.getCurrentBalance())
                        .transactionHistory(responseTransactionDTOS).build();

            } finally {
                account.getLock().unlock();
            }
        } else {
            throw new UnableToObtainLockException(account);
        }
    }
}