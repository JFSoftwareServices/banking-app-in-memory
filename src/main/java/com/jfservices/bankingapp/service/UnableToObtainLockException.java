package com.jfservices.bankingapp.service;

import com.jfservices.bankingapp.entity.Account;

import static java.lang.String.format;

class UnableToObtainLockException extends RuntimeException {
    UnableToObtainLockException(Account debitAccount, Account creditAccount) {
        super(format("Could not acquire lock on one or both of these account %s  and %s", debitAccount, creditAccount));
    }

    UnableToObtainLockException(Account account) {
        super(format("Could not acquire lock on this account %s", account));
    }
}