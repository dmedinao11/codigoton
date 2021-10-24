package com.dmedinao.codigoton.models.dtos.io;

import com.dmedinao.codigoton.models.enums.TableFilters;
import lombok.Data;

import java.util.Map;

/**
 * DTO que representa los datos de entrada para una mesa
 *
 * @version 1.0
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * **/
@Data
public class InputTableInfoDto {
    private String tableName;
    private Map<TableFilters, String> tableFilters;

    public InputTableInfoDto() {
    }
}
