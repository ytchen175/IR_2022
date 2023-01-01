package com.example.webdemo.DBConnection;

import java.nio.file.FileSystems;
import java.sql.*;

public class SQLiteConnectionImpl implements DBConnection{

    private String sqliteDBPath;

    public SQLiteConnectionImpl(){
        String filepath = System.getProperty("user.dir");
        this.sqliteDBPath = "jdbc:sqlite:"+filepath+"\\src\\main\\webapp\\WEB-INF\\DB\\userdb.db";
    }

    public SQLiteConnectionImpl(String sqliteDBPath){
        this.sqliteDBPath = sqliteDBPath;
    }


    public static void main(String[] args) {
        SQLiteConnectionImpl DBConnection = new SQLiteConnectionImpl();
        Connection connection = DBConnection.getConnection();
        selectAll(connection);

    }
    public static void selectAll(Connection conn) {
        String sql = "SELECT * FROM USER; ";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("ACCOUNT") + "\t" + rs.getString("PASSWORD") + "\t" + rs.getString("EMAIL"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Connection getConnection() {

        System.out.println("actual dbpath:"+this.sqliteDBPath);
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("xxxxxxx");;
        }
        try {
            conn = DriverManager.getConnection(this.sqliteDBPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
