package com.example.webdemo.DAO;

public interface UserDAO {
    public String getPassword(String userName);
    public String getSalt(String userName);
}
