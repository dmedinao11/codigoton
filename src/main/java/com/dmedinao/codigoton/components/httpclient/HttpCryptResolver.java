package com.dmedinao.codigoton.components.httpclient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class HttpCryptResolver {

    public String decryptCode(String cryptCode) throws Exception {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            String decryptUri = "https://test.evalartapp.com/extapiquest/code_decrypt/";
            HttpGet httpget = new HttpGet(decryptUri.concat(cryptCode));
            InputStream stream = httpclient.execute(httpget).getEntity().getContent();
            return readStream(stream);
        }
    }

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
