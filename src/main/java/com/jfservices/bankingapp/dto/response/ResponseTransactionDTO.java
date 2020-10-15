package com.jfservices.bankingapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ResponseTransactionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private BigDecimal transactionAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime transactionDateTime;
}