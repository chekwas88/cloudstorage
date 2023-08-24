package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    @GetMapping()
    public String signupView(@ModelAttribute("user") User user) {
        return "signup";
    }

    @PostMapping()
    public RedirectView signupUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "Username already exists";
        }

        if (signupError == null) {
            int userId = userService.createUser(user);
            if (userId < 0) {
                signupError = "Error during signup";
            }
        }

        if (signupError == null) {
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return new RedirectView("/login", true);
        } else {
            redirectAttributes.addFlashAttribute("signupError", signupError);
            return new RedirectView("signup", true);
        }
    }
}
