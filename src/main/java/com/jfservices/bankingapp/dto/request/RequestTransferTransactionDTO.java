package com.jfservices.bankingapp.dto.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class RequestTransferTransactionDTO {
    @NotEmpty(message = "debitAccountNumber cannot be missing or empty")
    @Size(min = 7, max = 7, message = "debitAccountNumber must be 7 characters")
    private String debitAccountNumber;

    @NotEmpty(message = "creditAccountNumber cannot be missing or empty")
    @Size(min = 7, max = 7, message = "creditAccountNumber must be 7 characters")
    private String creditAccountNumber;

    @NotNull(message = "amount cannot be missing or empty")
    @DecimalMin(value = "0.0", message = "amount cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "amount integral digits cannot be greater than 10 and " +
            "factional digits greater than 2")
    private BigDecimal amount;
}