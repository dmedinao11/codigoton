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
    private boolean error;

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
            error = true;
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

    public void writeOutput(OutputDto toOutput) {
        System.out.println(toOutput.getTableName());
        String content = toOutput.isCanceled() ? "CANCELADA" : String.join(",", toOutput.getCodes());
        System.out.println(content);
    }
}
