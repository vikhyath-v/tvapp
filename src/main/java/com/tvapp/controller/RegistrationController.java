package com.tvapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tvapp.model.User;
import com.tvapp.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    private final UserService userService;
    
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }
    
    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
} 