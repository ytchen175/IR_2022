package com.example.springProjectIR.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
public class WelcomePageController {

    @GetMapping("")
    public String getHomePage(Model model){
        return "index";
    }
}
