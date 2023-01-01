package com.example.webdemo.Service;

import com.example.webdemo.Bean.Document;
import com.example.webdemo.DAO.UserDAO;
import com.example.webdemo.DAO.UserDAOImpl;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginService {

    private UserDAO userDAO;

    private LoginService(){
        this.userDAO = new UserDAOImpl();
    }
    private LoginService(String dbPath){
        this.userDAO = new UserDAOImpl(dbPath);
    }

    public static LoginService createByDefaultDBPath(){
        return new LoginService();
    }

    public static LoginService createBySpecificDBPath(String dbPath){
        return new LoginService(dbPath);
    }


    public static void main(String[] args) {
//        System.out.println(LoginService.createByDefaultDBPath().isLoginSuccessful("admin","123"));
        Boolean isNotNull = null;
        System.out.println((Boolean)isNotNull);
    }

    public boolean isLoginSuccessful(String userName, String password){
        PasswordService service = new PasswordService();
        String saltedHashedPassword = this.userDAO.getPassword("admin");
        String salt = this.userDAO.getSalt("admin");

        if(saltedHashedPassword.equals(service.hashPassword(password,salt))){
            return true;
        }
        return false;
    }

}
