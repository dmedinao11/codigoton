package com.dmedinao.codigoton.models.dtos;

import lombok.Builder;
import lombok.Data;

/**
 * DTO que representa los datos de un cliente
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/
@Data
@Builder
public class ClientDto {
    private Integer id;

    private String code;

    private Boolean male;

    private String company;

    private Double balance;
}