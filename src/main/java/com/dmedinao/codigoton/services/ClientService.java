package com.dmedinao.codigoton.services;

import com.dmedinao.codigoton.exceptions.InsufficientClientsException;
import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;

public interface ClientService {
    void resolveTableClientsList(InputTableInfoDto tableInfo) throws InsufficientClientsException;
}
