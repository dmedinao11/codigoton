package com.dmedinao.codigoton;

import com.dmedinao.codigoton.components.ApplicationHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase de entrada de la aplicación de Springboot para la evaluación de selección clientes para las mesas
 *
 * @version 1.0
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * **/
@SpringBootApplication
public class CodigotonApplication implements ApplicationRunner {

    private final String input = "input.txt";
    private final ApplicationHandler applicationHandler;

    public CodigotonApplication(ApplicationHandler applicationHandler) {
        this.applicationHandler = applicationHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(CodigotonApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        applicationHandler.handleTablesEvaluation(input);
    }
}
