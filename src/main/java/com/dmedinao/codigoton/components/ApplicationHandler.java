package com.dmedinao.codigoton.components;

import com.dmedinao.codigoton.components.io.InputReader;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.services.ClientService;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class ApplicationHandler {

    private final InputReader inputReader;
    private final ClientService clientService;

    public ApplicationHandler(InputReader inputReader, ClientService clientService) {
        this.inputReader = inputReader;
        this.clientService = clientService;
    }

    public void handleTablesEvaluation(String inputFile){
        try {
            LinkedList<InputTableInfoDto> inputs = inputReader.readDataFromFile(inputFile);
            clientService.resolveTableClientsList(inputs.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
