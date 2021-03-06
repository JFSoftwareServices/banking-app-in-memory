package com.jfservices.bankingapp.controller.api;


import com.jfservices.bankingapp.dto.request.RequestAccountDTO;
import com.jfservices.bankingapp.dto.request.RequestTransferTransactionDTO;
import com.jfservices.bankingapp.dto.response.ResponseAccountDTO;
import com.jfservices.bankingapp.dto.response.ResponseStatementDTO;
import com.jfservices.bankingapp.dto.response.ResponseTransferTransactionDTO;
import com.jfservices.bankingapp.entity.Account;
import com.jfservices.bankingapp.service.AccountService;
import com.jfservices.bankingapp.service.ModelMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jfservices.bankingapp.service.ModelMapperService.convertAccountToAccountDto;
import static com.jfservices.bankingapp.service.ModelMapperService.modelMap;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
public class AccountController implements AccountContract {

    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<ResponseAccountDTO> createAccount(RequestAccountDTO requestAccountDTO) throws Exception {
        log.info("Creating account {}", requestAccountDTO);
        Account account = accountService.create(modelMap(requestAccountDTO, Account.class));
        ResponseAccountDTO accountDTO = convertAccountToAccountDto(account);
        log.info("Account {} created", account);
        return ResponseEntity.ok(accountDTO);
    }

    @Override
    public ResponseEntity<List<ResponseAccountDTO>> findAllAccounts() throws Exception {
        log.info("Looking for all accounts");
        List<ResponseAccountDTO> accounts =
                accountService
                        .findAll()
                        .stream()
                        .map(ModelMapperService::convertAccountToAccountDto)
                        .collect(toList());
        log.info("Found all accounts");
        return ResponseEntity.ok(accounts);
    }

    @Override
    public ResponseEntity<ResponseTransferTransactionDTO> sendMoney(RequestTransferTransactionDTO requestTransferBalanceDTO)
            throws Exception {
        log.info("Transferring {} from account {} to account {}", requestTransferBalanceDTO.getAmount(),
                requestTransferBalanceDTO.getDebitAccountNumber(),
                requestTransferBalanceDTO.getCreditAccountNumber());

        ResponseTransferTransactionDTO transaction =
                modelMap(accountService.transfer(requestTransferBalanceDTO), ResponseTransferTransactionDTO.class);

        log.info("Successfully transferred {} from account {} to account {}", requestTransferBalanceDTO.getAmount(),
                requestTransferBalanceDTO.getDebitAccountNumber(),
                requestTransferBalanceDTO.getCreditAccountNumber());
        return ResponseEntity.ok(transaction);
    }

    @Override
    public ResponseEntity<ResponseStatementDTO> getStatement(String accountNumber) throws Exception {
        log.info("Retrieving statements for account {}", accountNumber);
        ResponseStatementDTO statement = accountService.getStatement(accountNumber);
        log.info("Retrieved statements for account {}", accountNumber);
        return ResponseEntity.ok(statement);
    }

    @Override
    public ResponseEntity<ResponseAccountDTO> findByAccountNumber(String accountNumber) throws Exception {
        log.info("Retrieving account {}", accountNumber);
        ResponseAccountDTO accountDTO;
        accountDTO = ModelMapperService
                .convertAccountToAccountDto(accountService.findByAccountNumber(accountNumber));
        log.info("Retrieved account {}", accountNumber);
        return ResponseEntity.ok(accountDTO);
    }
}