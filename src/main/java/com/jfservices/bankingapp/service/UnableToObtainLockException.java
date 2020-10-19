package com.jfservices.bankingapp.service;

class UnableToObtainLockException extends RuntimeException {
    UnableToObtainLockException() {
        super("Could not acquire necessary lock when performing business process");
    }
}