package com.dmedinao.codigoton.components;

import com.dmedinao.codigoton.components.io.InputReader;
import com.dmedinao.codigoton.components.io.OutputWriter;
import com.dmedinao.codigoton.components.util.ClientsSelector;
import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.dtos.ClientDto;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.models.dtos.io.OutputDto;
import com.dmedinao.codigoton.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase con el método que se encarga de controlar y orquestar los distintos componentes para resolver
 * la solicitud
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

@Component
public class ApplicationHandler {

    private final InputReader inputReader;
    private final ClientService clientService;
    private final ClientsSelector clientsSelector;
    private final OutputWriter outputWriter;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationHandler.class);

    public ApplicationHandler(InputReader inputReader, ClientService clientService, ClientsSelector clientsSelector, OutputWriter outputWriter) {
        this.inputReader = inputReader;
        this.clientService = clientService;
        this.clientsSelector = clientsSelector;
        this.outputWriter = outputWriter;
    }

    /**
     * Método que acepta la dirección de archivo como entrada y realiza las
     * operaciones necesarias para dar respuesta a cada mesa que haya en el
     * archivo
     *
     * @param inputFile dirección relativa del archivo de entrada
     * @return void
     * @version 1.0
     * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
     **/

    public void handleTablesEvaluation(String inputFile) {
        LinkedList<InputTableInfoDto> inputs;
        try {
            inputs = inputReader.readDataFromFile(inputFile);
        } catch (FileNotFoundException e) {
            logger.error("File '" + inputFile + "' not found");
            return;
        } catch (IllegalArgumentException e) {
            logger.error("File '" + inputFile + "' has invalid format");
            return;
        }

        inputs.forEach(toEvaluate -> {
            try {
                List<ClientDto> filteredClients = clientService.resolveTableClientsList(toEvaluate);
                List<String> selectedClientsCodes = clientsSelector.selectClients(filteredClients);
                OutputDto outputDto = OutputDto
                        .builder()
                        .tableName(toEvaluate.getTableName())
                        .codes(selectedClientsCodes)
                        .build();
                outputWriter.writeOutput(outputDto);
            } catch (InsufficientClientsException e) {
                OutputDto outputDto = OutputDto
                        .builder()
                        .tableName(toEvaluate.getTableName())
                        .canceled(true)
                        .build();

                writeError(outputDto);

            } catch (DataAccessException e) {
                e.printStackTrace();
                OutputDto outputDto = OutputDto
                        .builder()
                        .tableName(toEvaluate.getTableName())
                        .withError(true)
                        .build();
                writeError(outputDto);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Método que imprime un error mediante un outputDTO
     *
     * @param output DTO con la información
     **/
    private void writeError(OutputDto output) {
        try {
            outputWriter.writeOutput(output);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Can't write output file");
        }
    }
}
