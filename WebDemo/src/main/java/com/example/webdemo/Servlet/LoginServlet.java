package com.example.webdemo.Servlet;

import com.example.webdemo.Service.LoginService;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static com.example.webdemo.Service.LoginService.*;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");
        String path = "jdbc:sqlite:E:\\userdb.db";

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username:"+username);

        LoginService loginService = LoginService.createBySpecificDBPath(path);

        boolean isLogin = loginService.isLoginSuccessful(username,password);
        System.out.println(isLogin);
        if(isLogin){
            session.setAttribute("isLogin",isLogin);
            session.setAttribute("userName",username);
        }

        PrintWriter out = response.getWriter();
        out.print(new JSONObject());
        out.flush();
        out.close();
    }
}
