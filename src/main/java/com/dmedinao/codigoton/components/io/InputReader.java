package com.dmedinao.codigoton.components.io;

import com.dmedinao.codigoton.models.dtos.io.InputTableInfoDto;
import com.dmedinao.codigoton.models.enums.TableFilters;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Componente de spring que se encarga de leer el archivo de entrada
 * y generar el correspondiente DTO para dar solución a la solicitud
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

@Component
public class InputReader {

    /**
     * Método que acepta la dirección de archivo como entrada y entrega
     * una lista de DTOS, cada uno con la información necesaria para relizar
     * la selección de una mesa
     *
     * @param file dirección relativa del archivo de entrada
     * @return Una lista de dtos con la información necesaria para una mesa
     **/
    public LinkedList<InputTableInfoDto> readDataFromFile(String file) throws FileNotFoundException, IllegalArgumentException {
        File inputFile = new File(file);
        List<String> lines = readLines(inputFile);
        return buildInputTransactionObj(lines);
    }

    /**
     * Método que acepta un objeto File y retora una lista con las líneas en dicho
     * archivo
     *
     * @param file Objeto File del cual se realizará la lectura
     * @return Una lista con las líneas leídas
     * @throws FileNotFoundException si el archivo no fue encontrado
     * @throws IllegalArgumentException si el archivo no tiene el formato correcto
     **/
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

    /**
     * Método que construye el DTO input desde una lista de cadenas
     * con las entradas
     * <p>
     * El método discrimina si la línea es el nombre de la mesa o un filtro que ha
     * de ser aplicado para la selección de cada mesa
     *
     * @param inputs lista de líneas aceptadas como entrada válida
     * @return Una lista con los dtos por cada mesa
     **/

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
