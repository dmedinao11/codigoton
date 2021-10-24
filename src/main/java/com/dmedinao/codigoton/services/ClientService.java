package com.dmedinao.codigoton.services;

import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.dtos.ClientDto;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> resolveTableClientsList(InputTableInfoDto tableInfo) throws InsufficientClientsException;
}
