package com.hobbyFinder.hubby.exception;

public class HubbyException extends RuntimeException {

    public HubbyException() {
        super("Erro inesperado no Hubby");
    }

    public HubbyException(String message) {
        super(message);
    }
}
