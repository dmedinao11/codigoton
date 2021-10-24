package com.dmedinao.codigoton.models.dtos.io;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OutputDto {
    private String tableName;
    private List<String> codes;
    private boolean canceled;
}
