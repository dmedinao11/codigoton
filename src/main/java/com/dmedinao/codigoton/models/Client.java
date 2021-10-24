package com.dmedinao.codigoton.models;

import lombok.Builder;
import lombok.Data;

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
