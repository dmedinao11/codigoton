package com.dmedinao.codigoton.services.impl;

import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.Client;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.models.enums.TableFilters;
import com.dmedinao.codigoton.repositories.ClientRepository;
import com.dmedinao.codigoton.services.ClientService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void resolveTableClientsList(InputTableInfoDto tableInfo) throws InsufficientClientsException {
        String filtersString = getQueryFilters(tableInfo.getTableFilters());
        List<Client> filteredClients = clientRepository.getByFilters(filtersString);
        boolean hasNotSufficientClients = filteredClients.size() < 4;

        if(hasNotSufficientClients){
            throw new InsufficientClientsException("Number of clients: ".concat(String.valueOf(filteredClients.size())));
        }



    }

    private String getQueryFilters(Map<TableFilters, String> filters) {
        LinkedList<String> filtersString = new LinkedList<>();

        if (filters.containsKey(TableFilters.TC)) {
            filtersString.add(String.format("c.type = %s", filters.get(TableFilters.TC)));
        }

        if (filters.containsKey(TableFilters.UG)) {
            filtersString.add(String.format("c.location = %s", filters.get(TableFilters.UG)));
        }

        if (filters.containsKey(TableFilters.RI)) {
            filtersString.add(String.format("balance >= %s", filters.get(TableFilters.RI)));
        }

        if (filters.containsKey(TableFilters.RF)) {
            filtersString.add(String.format("balance <= %s", filters.get(TableFilters.RF)));
        }

        return String.join(" AND ", filtersString);
    }
}