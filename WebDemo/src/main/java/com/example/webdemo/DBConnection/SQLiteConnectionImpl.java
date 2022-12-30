package com.example.webdemo.DBConnection;

import java.sql.*;

public class SQLiteConnectionImpl implements DBConnection{


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
        String filepath = System.getProperty("user.dir");
        String dbURL = "jdbc:sqlite:"+filepath+"\\src\\main\\webapp\\WEB-INF\\DB\\userdb.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
