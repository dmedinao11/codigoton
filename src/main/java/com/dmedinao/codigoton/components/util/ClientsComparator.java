package com.dmedinao.codigoton.components.util;

import com.dmedinao.codigoton.models.dtos.ClientDto;

import java.util.Comparator;

public class ClientsComparator implements Comparator<ClientDto> {
    @Override
    public int compare(ClientDto o1, ClientDto o2) {
        if(o1.getBalance() > o2.getBalance()){
            return -1;
        }

        if(o1.getBalance() < o2.getBalance()){
            return 1;
        }

        return o1.getCode().compareTo(o2.getCode());
    }
}
