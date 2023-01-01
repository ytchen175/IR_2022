package com.example.webdemo.Service;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordService {


    public static void main(String[] args) {
        System.out.println(new PasswordService().addSaltAndHashedPassword("abc"));
    }

    public String addSaltAndHashedPassword(String password){
        String salt =  generateSafeToken();
        return hashPassword(password, salt);
    }

    public String hashPassword(String password,String salt){
        return DigestUtils.md5Hex(password+salt);
    }

    private String generateSafeToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return token;
    }
}
