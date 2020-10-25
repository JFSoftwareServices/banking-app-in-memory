package com.jfservices.bankingapp.controller.api;

import com.jfservices.bankingapp.dto.request.RequestAccountDTO;
import com.jfservices.bankingapp.dto.request.RequestTransferTransactionDTO;
import com.jfservices.bankingapp.dto.response.ResponseAccountDTO;
import com.jfservices.bankingapp.dto.response.ResponseStatementDTO;
import com.jfservices.bankingapp.dto.response.ResponseTransferTransactionDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RequestMapping(value = "/api/v1/account",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public interface AccountContract {

    //If we need headers then use RequestEntity rather than RequestBody
    //https://goodwinwei.wordpress.com/2017/01/06/springresquestbody-responsebody-vs-httpentityresponseentity/
    @PostMapping("/createAccount")
    ResponseEntity<ResponseAccountDTO> createAccount(@RequestBody() RequestAccountDTO requestAccountDTO) throws Exception;

    @GetMapping("/allAccounts")
    ResponseEntity<List<ResponseAccountDTO>> findAllAccounts() throws Exception;

    @PostMapping("/sendMoney")
    ResponseEntity<ResponseTransferTransactionDTO> sendMoney(@RequestBody() RequestTransferTransactionDTO transferBalanceRequest) throws Exception;

    @GetMapping("/statement/{accountNumber}")
    ResponseEntity<ResponseStatementDTO> getStatement(@PathVariable()
                                                      @Size(min = 7, max = 7, message = "accountNumber must be 7 characters")
                                                              String accountNumber) throws Exception;

    @GetMapping("/{accountNumber}")
    ResponseEntity<ResponseAccountDTO> findByAccountNumber(@PathVariable()
                                                           @Size(min = 7, max = 7, message = "accountNumber must be 7 characters")
                                                                   String accountNumber) throws Exception;
}