package com.dmedinao.codigoton.components;

import com.dmedinao.codigoton.components.io.InputReader;
import com.dmedinao.codigoton.components.io.OutputWriter;
import com.dmedinao.codigoton.components.util.ClientsSelector;
import com.dmedinao.codigoton.models.dtos.ClientDto;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.models.dtos.io.OutputDto;
import com.dmedinao.codigoton.services.ClientService;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase con el método que se encarga de controlar y orquestar los distintos componentes para resolver
 * la solicitud
 *
 * @version 1.0
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * **/

@Component
public class ApplicationHandler {

    private final InputReader inputReader;
    private final ClientService clientService;
    private final ClientsSelector clientsSelector;
    private final OutputWriter outputWriter;

    public ApplicationHandler(InputReader inputReader, ClientService clientService, ClientsSelector clientsSelector, OutputWriter outputWriter) {
        this.inputReader = inputReader;
        this.clientService = clientService;
        this.clientsSelector = clientsSelector;
        this.outputWriter = outputWriter;
    }

    /**
     * Método que acepta la dirección de archivo como entrada y reliza las
     * operaciones necesarias para dar respuesta a cada mesa que haya en el
     * archivo
     *
     * @param inputFile dirección relativa del archivo de entrada
     * @return void
     *
     * @version 1.0
     *
     * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
     * **/

    public void handleTablesEvaluation(String inputFile) {
        LinkedList<InputTableInfoDto> inputs;
        try {
            inputs = inputReader.readDataFromFile(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
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
            } catch (Exception e) {
                OutputDto outputDto = OutputDto
                        .builder()
                        .tableName(toEvaluate.getTableName())
                        .canceled(true)
                        .build();
                outputWriter.writeOutput(outputDto);
            }
        });
    }
}
