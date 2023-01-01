package com.example.webdemo.DAO;

import com.example.webdemo.DBConnection.DBConnection;
import com.example.webdemo.DBConnection.SQLiteConnectionImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO{
    private DBConnection dbConnection;

    public UserDAOImpl(){
        this.dbConnection = new SQLiteConnectionImpl();
    }

    public UserDAOImpl(String dbPath){
        this.dbConnection = new SQLiteConnectionImpl(dbPath);
    }

    public static void main(String[] args) {
        UserDAOImpl dao = new UserDAOImpl();
        System.out.println(dao.getPassword("admin"));
        System.out.println(dao.getSalt("admin"));
    }
    @Override
    public String getPassword(String userName) {
        Connection connection = dbConnection.getConnection();
        String password = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT PASSWORD FROM USER WHERE ACCOUNT = ? ;")){
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                password = resultSet.getString("PASSWORD");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    @Override
    public String getSalt(String userName) {
        Connection connection = dbConnection.getConnection();
        String salt = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT SALT FROM USER WHERE ACCOUNT = ? ;")){
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                salt = resultSet.getString("SALT");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salt;
    }
}
