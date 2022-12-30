package com.example.webdemo.Service;

import com.example.webdemo.DAO.UserDAO;
import com.example.webdemo.DAO.UserDAOImpl;

public class LoginService {

    public static void main(String[] args) {
        System.out.println(new LoginService().isLoginSuccessful("admin","123"));
    }

    public boolean isLoginSuccessful(String userName, String password){
        UserDAO userDAO = new UserDAOImpl();
        PasswordService service = new PasswordService();
        String saltedHashedPassword = userDAO.getPassword("admin");
        String salt = userDAO.getSalt("admin");

        if(saltedHashedPassword.equals(service.hashPassword(password,salt))){
            return true;
        }
        return false;
    }

}
