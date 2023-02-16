package com.training.eshop.exception;

public class UserIsPresentException extends RuntimeException {

    public UserIsPresentException(String msg) {
        super(msg);
    }
}
