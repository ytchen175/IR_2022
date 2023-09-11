package com.example.springProjectIR.Service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordService {


    public static void main(String[] args) {
        System.out.println(new PasswordService().addSaltAndHashedPassword("abc"));
    }

    public String addSaltAndHashedPassword(String password){
        String salt =  generateRandomSalt();
        return hashPasswordWithMD5(password, salt);
    }

    public String hashPasswordWithMD5(String password, String salt){
        return DigestUtils.md5Hex(password+salt);
    }

    public String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return token;
    }
}
