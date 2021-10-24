package com.dmedinao.codigoton.components.io;

import com.dmedinao.codigoton.models.dtos.io.OutputDto;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class OutputWriter {

    private File file;
    private FileWriter writer;
    private boolean hasError;
    private OutputDto outputDto;

    @PostConstruct
    private void postConstruct() {
        try {
            String outputFile = "output.txt";
            file = new File(outputFile);
            if (file.exists()) {
                file.delete();
            }
            file = new File(outputFile);
            writer = new FileWriter(file, false);

        } catch (IOException e) {
            e.printStackTrace();
            hasError = true;
        }

    }

    @PreDestroy
    private void preDestroy() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que acepta un DTO y escribe su información en consola y el archivo output.txt
     *
     * @param outputDto DTO con la información de la salida
     * @throws IOException si el archivo no se puedo escribir correctamente
     **/
    public void writeOutput(OutputDto outputDto) throws IOException {
        this.outputDto = outputDto;
        writeOnConsole();
        writeOnFile();
    }

    /**
     * Método que escribe la información del DTO en consola
     **/
    private void writeOnConsole() {
        System.out.println(outputDto.getTableName());
        String content = outputDto.isWithError() ? "ERROR" : outputDto.isCanceled() ? "CANCELADA" : String.join(",", outputDto.getCodes());
        System.out.println(content);
    }

    /**
     * Método que escribe la información del DTO en el archivo output.txt
     *
     * @throws IOException
     **/
    private void writeOnFile() throws IOException {
        if (!hasError) {
            writer.write(outputDto.getTableName().concat("\n"));
            String content = outputDto.isWithError() ? "ERROR" : outputDto.isCanceled() ? "CANCELADA" : String.join(",", outputDto.getCodes());
            writer.write(content.concat("\n"));
        } else {
            System.out.println("Can't write on file :(");
        }
    }
}
