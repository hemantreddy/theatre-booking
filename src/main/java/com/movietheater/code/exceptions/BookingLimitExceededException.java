package com.movietheater.code.exceptions;


public class BookingLimitExceededException extends Exception {

    public BookingLimitExceededException(String message) {
        super(message);
    }

}
