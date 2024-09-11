package ru.example.voting.util.exception;

public class AdminNotAllowedException extends RuntimeException {

    public AdminNotAllowedException(String message) {
        super(message);
    }

}
