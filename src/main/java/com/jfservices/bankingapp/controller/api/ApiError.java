package com.jfservices.bankingapp.controller.api;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
@ToString
class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    ApiError(final HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}