package com.example.webdemo.Service;

import com.example.webdemo.Bean.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchService {

    public static void main(String[] args) {
        SearchService service = new SearchService();
        List<Document> results = service.searchDocuments("computer science");

        results.sort(
                Comparator.comparing(Document::getScore).reversed()
        );

        for(Document document :results){
            System.out.println(document.getScore());
            System.out.println(document.getDocID());
            System.out.println(document.getTitle());
            System.out.println(document.getBody().equals(document.getOriginalBody()));
        }
    }



    public List<Document> searchDocuments(String searchContent){

        HttpRequester service = new ElasticSearchHttpRequesterService("http://localhost:9200/warc/_search");
        service.setRequestParameter(searchContent);
        JSONObject object =service.makeHttpRequestAndGetResult();

        List<Document> documents = generateDocuments(object);

        return documents;
    }

    private List<Document> generateDocuments(JSONObject object) {
        List<Document> results = new ArrayList<>();
        JSONObject jsonObject = object.getJSONObject("hits");
        JSONArray jsonArray = jsonObject.getJSONArray("hits");
        for(int i=0;i<jsonArray.length();i++){
            Document document = new Document();
            JSONObject item = jsonArray.getJSONObject(i);
            document.setScore(item.getInt("_score"));
            JSONObject documentJSON = item.getJSONObject("_source");
            document.setDocID(documentJSON.getString("docID"));
            document.setTitle(documentJSON.getString("title"));
            document.setH1(documentJSON.getString("h1"));
            document.setBody(documentJSON.getString("body"));
            document.setOriginalBody(documentJSON.getString("origin_body"));
            results.add(document);
        }
        return results;
    }


}
