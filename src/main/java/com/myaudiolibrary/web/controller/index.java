package com.myaudiolibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class index {

    @GetMapping(value = "")
    public String getAccueil(){
        return "accueil";
    }
}
