package com.dmedinao.codigoton.components.httpclient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Clase que resuelve y desencripta los códigos de clientes encriptados
 *
 * @author Daniel De Jesús Medina Ortega (danielmedina1119@gmail.com) GitHub (dmedinao11)
 * @version 1.0
 **/

@Component
public class HttpCryptResolver {

    /**
     * Método que recibe el código encriptad, crea la request http para obtener el código.
     * <p>
     * De realizar correctamente la solicitud retorna el código desencriptado.
     * <p>
     * De lo contrario lanza una exepción común.
     *
     * @param cryptCode código de usuario encriptado
     * @return el código desencriptado traído del request http
     * @throws Exception
     **/
    public String decryptCode(String cryptCode) throws Exception {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String decryptUri = "https://test.evalartapp.com/extapiquest/code_decrypt/";
            HttpGet httpget = new HttpGet(decryptUri.concat(cryptCode));
            InputStream stream = httpclient.execute(httpget).getEntity().getContent();
            return readStream(stream);
        }
    }

    /**
     * Método que recibe el un stream de bytes provenientes de la petición http.
     * <p>
     * lanza una excepción si al realizar la lectura de bytes ocurre un error
     *
     * @param stream bytes correspondientes a la petición
     * @return la cadena presente en el stream
     * @throws IOException
     **/
    private String readStream(InputStream stream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

}
