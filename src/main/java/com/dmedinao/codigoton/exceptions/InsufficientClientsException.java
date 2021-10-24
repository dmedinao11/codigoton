package com.dmedinao.codigoton.exceptions;

public class InsufficientClientsException extends Exception {
    public InsufficientClientsException(String errorMessage) {
        super(errorMessage);
    }
}
