package com.dmedinao.codigoton.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {
    private Integer id;

    private String code;

    private Boolean male;

    private String company;

    private Double balance;
}