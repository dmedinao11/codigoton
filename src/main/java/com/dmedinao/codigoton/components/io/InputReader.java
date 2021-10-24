package com.dmedinao.codigoton.components.io;

import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.models.enums.TableFilters;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Component
public class InputReader {
    public LinkedList<InputTableInfoDto> readDataFromFile(String file) throws FileNotFoundException, IllegalArgumentException {
        File inputFile = new File(file);
        List<String> lines = readLines(inputFile);
        return buildInputTransactionObj(lines);
    }


    private List<String> readLines(File file) throws FileNotFoundException,
            IllegalArgumentException {
        List<String> lines = new LinkedList<>();
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            lines.add(reader.nextLine());
        }
        reader.close();
        return lines;
    }

    private LinkedList<InputTableInfoDto> buildInputTransactionObj(List<String> inputs) {
        LinkedList<InputTableInfoDto> tableRequest = new LinkedList<>();

        InputTableInfoDto currentTable = new InputTableInfoDto();
        Map<TableFilters, String> currentTableFilters = new HashMap<>();
        int index = 0;

        for (String input : inputs) {
            if (index++ == 0) {
                currentTable.setTableName(input);
                continue;
            }

            boolean isATableName = input.indexOf('<') > 0 || input.indexOf('>') > 0;

            if (isATableName) {
                currentTable.setTableFilters(currentTableFilters);
                tableRequest.add(currentTable);
                currentTable = new InputTableInfoDto();
                currentTable.setTableName(input.trim());
                currentTableFilters = new HashMap<>();
            } else {
                String[] filterAndValue = input.split(":");
                TableFilters filter = TableFilters.valueOf(filterAndValue[0].trim());
                String value = filterAndValue[1];
                currentTableFilters.put(filter, value);
            }
        }


        return tableRequest;
    }

}
