package com.dmedinao.codigoton.models;

import lombok.Builder;
import lombok.Data;

/**
 * Modelo para la entidad cliente obtenido desde la base de datos (la consulta)
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

@Data
@Builder
public class Client {
    private Integer id;

    private String code;

    private Boolean male;

    private Integer type;

    private String location;

    private String company;

    private Boolean encrypt;

    private Double balance;
}
