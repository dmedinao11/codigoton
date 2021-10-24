package com.dmedinao.codigoton.models.dtos.io;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO que representa los datos de salida para una mesa
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

@Builder
@Data
public class OutputDto {
    private String tableName;
    private List<String> codes;
    private boolean canceled;
}
