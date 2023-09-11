package com.example.springProjectIR.Service;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class HttpRequester {
    private String url;

    public HttpRequester(String url){this.url =url; }

    public abstract JSONObject makeHttpRequestAndGetResult();

    protected String getRequestUrl(){
        return this.url;
    }
    protected String getResult(HttpResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = response.getEntity().getContent(); //Get the data in the entity
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    protected abstract void setRequestParameter(String requestParameter);
}
