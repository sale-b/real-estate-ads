package com.fon.controller;

import com.fon.entity.User;
import com.fon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, String confirmPassword, String redirectUrl) throws UnsupportedEncodingException {
        if (!Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(user.getEmail()).matches()) {
            return "redirect:/register?error=invalid_email_format";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            // Handle password mismatch
            return "redirect:/register?error=password_mismatch";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:" + URLDecoder.decode(redirectUrl, "UTF-8");
    }
}