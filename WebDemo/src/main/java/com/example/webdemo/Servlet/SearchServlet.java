package com.example.webdemo.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.example.webdemo.Bean.Document;
import com.example.webdemo.Service.SearchService;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "searchServlet", value = "/search-servlet")
public class SearchServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{

        response.setCharacterEncoding("UTF-8");
        String searchContent = request.getParameter("searchContent");
        System.out.println(searchContent);


        SearchService searchService = new SearchService();
        List<Document> searchResults = searchService.searchDocuments(searchContent);

        JSONArray jsonArray = new JSONArray();
        for(int i =0 ;i <searchResults.size();i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url",searchResults.get(i).getLink());
            jsonObject.put("title",searchResults.get(i).getTitle());
            jsonObject.put("score",searchResults.get(i).getScore());
            jsonObject.put("h1",searchResults.get(i).getH1());
            jsonObject.put("body",searchResults.get(i).getBody());
            jsonArray.put(jsonObject);

        }

        PrintWriter out = response.getWriter();
        out.print(jsonArray);
        out.flush();
        out.close();
    }

    public void destroy() {
    }
}