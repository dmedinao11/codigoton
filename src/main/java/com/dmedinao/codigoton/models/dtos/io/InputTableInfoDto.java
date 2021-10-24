package com.dmedinao.codigoton.models.dtos.io;

import com.dmedinao.codigoton.models.enums.TableFilters;
import lombok.Data;

import java.util.Map;

@Data
public class InputTableInfoDto {
    private String tableName;
    private Map<TableFilters, String> tableFilters;

    public InputTableInfoDto() {
    }
}
