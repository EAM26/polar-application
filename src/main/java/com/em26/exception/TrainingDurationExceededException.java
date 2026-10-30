package com.em26.exception;

public class TrainingDurationExceededException extends RuntimeException{

    public TrainingDurationExceededException(String message) {
        super(message);
    }
}
