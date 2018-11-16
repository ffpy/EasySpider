package org.ffpy.easyspider.sample.qidian;

import org.ffpy.easyspider.core.entity.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class TestResponse {

    public static Response getResponse() throws IOException {
        Reader reader = new FileReader(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "\\qidian.html");
        BufferedReader br = new BufferedReader(reader);
        StringBuilder html = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            html.append(s);
        }
        return Response.Builder.of(Response.OK, html.toString()).build();
    }
}
