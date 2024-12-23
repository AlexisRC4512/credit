package com.nttdata.credit.model.exception;

public class InvalidCreditDataException extends RuntimeException {
    public InvalidCreditDataException(String message) {
        super(message);
    }
}
