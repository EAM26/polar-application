package com.em26.exception;

public class TimeExceedingException extends RuntimeException{

    public TimeExceedingException(String message) {
        super(message);
    }
}
