package com.example.springProjectIR.Service;


import com.example.springProjectIR.Model.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SearchService {

    private final ElasticSearchHttpRequesterService elasticService;

    public SearchService(ElasticSearchHttpRequesterService elasticService) {
        this.elasticService = elasticService;
    }

    public List<Document> searchDocuments(String keyword){
        this.elasticService.setRequestParameter(keyword);
        JSONObject object = this.elasticService.makeHttpRequestAndGetResult();
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
