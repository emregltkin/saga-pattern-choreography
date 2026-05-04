package org.saga.booking.exception;

public class PaymentServiceException extends RuntimeException{

    public PaymentServiceException(String message) {
        super(message);
    }
}
