package com.example.springProjectIR.controller;

import com.example.springProjectIR.Service.LoginService;
import com.example.springProjectIR.Service.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class UserAccountController {

    private final LoginService loginService;

    private final RegisterService registerService;

    public UserAccountController(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public Map<String,Object> register(String username,String password,String email){

        boolean usernameUsed = this.registerService.checkIfUserNameExsits(username);

        HashMap<String,Object> map = new HashMap<>();
        if(usernameUsed){
            map.put("isSuccessfullyRegistered",false);
            map.put("errorMessage","This username has been used.");
        }else {
            this.registerService.register(username,password,email);
            map.put("isSuccessfullyRegistered",true);
            map.put("errorMessage",null);
        }
        return map;
    }

    @PostMapping("/login")
    public Map<String,Object> login(String username, String password, HttpSession httpSession){
        boolean isLogin = this.loginService.isLoginSuccessful(username,password);
        HashMap<String,Object> map = new HashMap<>();
        if(isLogin) httpSession.setAttribute("username",username);
        map.put("isLogin",isLogin);
        map.put("username",username);
        return map;
    }

}
