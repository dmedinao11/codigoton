package com.dmedinao.codigoton;

import com.dmedinao.codigoton.components.ApplicationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

/**
 * Clase de entrada de la aplicación de Springboot para la evaluación de selección clientes para las mesas
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/
@SpringBootApplication
public class CodigotonApplication implements ApplicationRunner {

    private final ApplicationHandler applicationHandler;
    private static final Logger logger = LoggerFactory.getLogger(CodigotonApplication.class);

    public CodigotonApplication(ApplicationHandler applicationHandler) {
        this.applicationHandler = applicationHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(CodigotonApplication.class, args);
    }

    /**
     * Método que inicia la aplicación llamando al controlador de la entrada
     * <p>
     * Recibe los argumentos colocados en la línea de comandos
     *
     * @param args Argumentos pasados por Spring desde la consola
     * @return
     * @throws Exception Se lanza si la aplicación no inició correctamente
     **/
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String input = "input.txt";
        if (args.containsOption("input")) {
            String argument = args.getOptionValues("input").get(0);
            if (validateInputArg(argument)) {
                input = args.getOptionValues("input").get(0);
            } else {
                logger.error("File must to have .txt extension");
            }
        }
        applicationHandler.handleTablesEvaluation(input);
    }

    /**
     * Método para validar que la extensión del archivo pasado por argumento sea txt
     *
     * @param arg Argumento a evaluar
     * @return true si el argumento termina en .txt false si no
     **/
    public boolean validateInputArg(String arg) {
        String[] slicedString = arg.split("\\.");
        return Objects.equals(slicedString[slicedString.length - 1], "txt");
    }
}
