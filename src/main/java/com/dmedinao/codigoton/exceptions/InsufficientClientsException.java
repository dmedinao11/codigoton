package com.dmedinao.codigoton.exceptions;

/**
 * Excepción que se lanza cuando los clientes seleccionados son insuficientes
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/
public class InsufficientClientsException extends Exception {
    public InsufficientClientsException(String errorMessage) {
        super(errorMessage);
    }
}
