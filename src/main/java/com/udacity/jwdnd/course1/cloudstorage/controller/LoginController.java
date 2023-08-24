package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.User;

@Controller
@RequestMapping("/login")

public class LoginController {

    @GetMapping()
    public String loginView(@ModelAttribute("user") User user) {
        return "login";
    }

}
