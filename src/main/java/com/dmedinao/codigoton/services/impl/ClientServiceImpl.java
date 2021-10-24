package com.dmedinao.codigoton.services.impl;

import com.dmedinao.codigoton.components.httpclient.HttpCryptResolver;
import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.Client;
import com.dmedinao.codigoton.models.dtos.ClientDto;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.models.enums.TableFilters;
import com.dmedinao.codigoton.repositories.ClientRepository;
import com.dmedinao.codigoton.services.ClientService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para la comunicación con la capa de persistencia para los clientes
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final HttpCryptResolver httpCryptResolver;


    public ClientServiceImpl(ClientRepository clientRepository, HttpCryptResolver httpCryptResolver) {
        this.clientRepository = clientRepository;
        this.httpCryptResolver = httpCryptResolver;
    }

    /**
     * Método que recibe el dto. Con la información de los clientes
     * y retorna una lista de clientes con aplicando su dto. De la capa de persistencia.
     * <p>
     * En caso de seleccionar usuarios insuficientes lanza una excepción
     *
     * @param tableInfo Dto. Con los datos de entrada y filtros para realizar la consulta
     * @return lista ordenada con los DTOS de usuario
     * @throws InsufficientClientsException de no seleccionar suficientes clientes
     **/
    @Override
    public List<ClientDto> resolveTableClientsList(InputTableInfoDto tableInfo) throws InsufficientClientsException, DataAccessException {
        String filtersString = getQueryFilters(tableInfo.getTableFilters());
        List<Client> filteredClients = clientRepository.getByFilters(filtersString);
        boolean hasNotSufficientClients = filteredClients.size() < 4;

        if (hasNotSufficientClients) {
            throw new InsufficientClientsException("Number of clients: ".concat(String.valueOf(filteredClients.size())));
        }

        decryptCodes(filteredClients);

        return filteredClients.stream().map(this::buildClientDto).collect(Collectors.toList());
    }

    /**
     * Método que construye los filtros correctamente en lenguaje SQL de acuerdo a los datos
     * de los filtros presentes en la entrada
     *
     * @param filters Mapa con cada filtro y su valor
     * @return Un string en lenguaje SQL que se integrará a la consulta
     **/
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

    /**
     * Método que busca en la lista de usuarios aquellos que tengan su código encriptado
     * y utiliza el servicio para resolverlos
     *
     * @param clients Lista de usuarios
     **/
    private void decryptCodes(List<Client> clients) {
        clients.forEach(client -> {
            if (client.getEncrypt()) {
                try {
                    String decryptedCode = httpCryptResolver.decryptCode(client.getCode());
                    client.setCode(decryptedCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Método que a partir de un cliente construye su DTO
     *
     * @param client Cliente
     **/
    private ClientDto buildClientDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .code(client.getCode())
                .male(client.getMale())
                .company(client.getCompany())
                .balance(client.getBalance())
                .build();
    }
}
