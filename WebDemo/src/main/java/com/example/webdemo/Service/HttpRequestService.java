package com.example.webdemo.Service;






import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpRequestService {
    public static void main(String[] args) throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title","computer science");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("match",jsonObject);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("query",jsonObject1);
        System.out.println(jsonObject2);

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:9200/warc/_search");
            StringEntity params = new StringEntity(jsonObject2.toString());
            request.addHeader("Content-Type", "application/json; utf-8");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            /*Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                System.out.println("JSON Parser"+ "result: " + result.toString());
            }

        } catch (Exception ex) {
        } finally {
            // @Deprecated httpClient.getConnectionManager().shutdown();
        }
//        HttpRequestService service = new HttpRequestService();
//        service.makeHttpRequest("http://localhost:9200/warc/_search",query);

//        HttpsURLConnection con;
//        URL urlObj;
//        urlObj = new URL("http://www.javatpoint.com");
//        con = (HttpsURLConnection) urlObj.openConnection();
//        HttpURLConnection httpConnection = (HttpURLConnection) new URL("http://www.javatpoint.com").openConnection();
    }
    String charset = "UTF-8";
    HttpURLConnection con;
    JSONObject jObj = null;
    StringBuilder result;
    public JSONObject makeHttpRequest(String url,
                                      String paramsJSON) {

        URL urlObj;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Content-Encoding", "gzip");
            con.setRequestProperty("Content-length", "11688");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Access-Control-Allow-Origin", "github.com.*");

            con.setDoOutput(true);
            con.setReadTimeout(60000);
            con.setConnectTimeout(60000);

//            OutputStream os = con.getOutputStream();
//            os.write(paramsJSON.toString().getBytes("UTF-8"));
//            os.close();
//            try (OutputStream os = con.getOutputStream()) {
//                byte[] input = paramsJSON.getBytes(charset);
//                os.write(input, 0, input.length);
//            }

            int code = con.getResponseCode();
            System.out.println("HTTP CODE " + String.valueOf(code));;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            System.out.println("JSON Parser"+ "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        con.disconnect();
//
//        // try parse the string to a JSON object
//        try {
//            jObj = new JSONObject(result.toString());
//        } catch (JSONException e) {
//            System.out.println("JSON Parser " + "Error parsing data " + e.toString());
//        }

        return jObj;
    }
}
