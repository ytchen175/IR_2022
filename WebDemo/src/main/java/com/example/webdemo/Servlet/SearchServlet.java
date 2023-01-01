package com.example.webdemo.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;

@WebServlet(name = "searchServlet", value = "/search-servlet")
public class SearchServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        Gson gson = new Gson();
        response.setCharacterEncoding("UTF-8");
        String searchContent = request.getParameter("searchContent");
        System.out.println(searchContent);
        List<String> result = new ArrayList<>();
        result.add("docID_00000.html");
        result.add("docID_00001.html");
        result.add("docID_00002.html");
        result.add("docID_00003.html");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(result));
        out.flush();
        out.close();
    }

    public void destroy() {
    }
}