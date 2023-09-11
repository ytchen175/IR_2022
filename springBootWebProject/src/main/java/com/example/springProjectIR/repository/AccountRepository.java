package com.example.springProjectIR.repository;

import com.example.springProjectIR.Model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;


    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getStoredPasswordByUsername(String username){
        String sql = "SELECT password FROM account WHERE username=?";
        return  this.jdbcTemplate.queryForObject(sql,new Object[]{username},String.class);
    }

    public String getSaltByUsername(String username){
        String sql = "SELECT salt FROM Account WHERE username=?";
        return  this.jdbcTemplate.queryForObject(sql,new Object[]{username},String.class);
    }

    public boolean checkIfUserNameExists(String username) {
        String sql = "SELECT * FROM Account WHERE username=?";
        List<Account> list =this.jdbcTemplate.query(sql,new Object[]{username},AccountRepository::mapRow);
        return list.size() == 1;
    }

    private static Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Account(resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("salt"),
                resultSet.getString("email"),
                resultSet.getObject("date_created", LocalDateTime.class),
                resultSet.getObject("date_updated", LocalDateTime.class)
                );
    }

    public void insertAccountData(String username,String encryptedPassword,String salt,String email){
        String sql = "INSERT INTO  account(username, password, salt, email,date_created) VALUES (?, ?, ?, ?, NOW())";
        jdbcTemplate.update(sql, username, encryptedPassword, salt,  email);
    }
}
