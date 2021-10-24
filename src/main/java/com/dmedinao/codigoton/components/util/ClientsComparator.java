package com.dmedinao.codigoton.components.util;

import com.dmedinao.codigoton.models.dtos.ClientDto;

import java.util.Comparator;

/**
 * Clase que implementa a Comparator para mantener correctamente ordenados los datos de los clientes
 * en las colas de prioridad
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/
public class ClientsComparator implements Comparator<ClientDto> {
    /**
     * Método que compara el balance total de un cliente, colocandolo descendentemente
     * y de tener el mismo código las ordenara por el código de usuario
     *
     * @param o1 primer cliente a comparar
     * @param o2 segundo cliente a comparar
     * @return Un entero positivo si o2 > o1, negativo si o2 < o1, según los criterios
     **/
    @Override
    public int compare(ClientDto o1, ClientDto o2) {
        if (o1.getBalance() > o2.getBalance()) {
            return -1;
        }

        if (o1.getBalance() < o2.getBalance()) {
            return 1;
        }

        return o1.getCode().compareTo(o2.getCode());
    }
}
