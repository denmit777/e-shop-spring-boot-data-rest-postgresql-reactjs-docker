package com.training.eshop.exception;

public class OrderNotPlacedException extends RuntimeException {

    public OrderNotPlacedException(String message) {
        super(message);
    }
}
