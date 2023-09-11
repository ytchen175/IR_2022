package com.example.springProjectIR.Service;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Service
public class ElasticSearchHttpRequesterService extends HttpRequester {

    private JSONObject requestJSONObject;
    public ElasticSearchHttpRequesterService(){
        super("http://localhost:9200/warc/_search");
    }



    public void setRequestParameter(String searchContent){
        JSONObject inner1 = new JSONObject();
        inner1.put("title",searchContent);
        JSONObject inner2 = new JSONObject();
        inner2.put("match",inner1);
        JSONObject main = new JSONObject();
        main.put("query",inner2);
        this.requestJSONObject = main;
    }

    public JSONObject makeHttpRequestAndGetResult() {
        String result = null;
        HttpResponse response = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(super.getRequestUrl());
            StringEntity params = new StringEntity(this.requestJSONObject.toString());
            request.addHeader("Content-Type", "application/json; utf-8");
            request.setEntity(params);
            response = httpClient.execute(request);

            if (response != null) {
                result = super.getResult(response);

            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new JSONObject(result);
    }
}
