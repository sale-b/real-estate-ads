package com.fon.controller;

import com.fon.entity.User;
import com.fon.repository.UserRepository;
import com.fon.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

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
        user.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), String.format("http://auth-server:9000/registrationConfirm?token=%s&redirectUrl=%s", user.getVerificationToken(), URLDecoder.decode(redirectUrl, "UTF-8")));
        return "registration-confirmation";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, @RequestParam("redirectUrl") String redirectUrl, Model model) {

        Optional<User> user = userRepository.findByVerificationToken(token);
        String message = "";
        if (user.isPresent()) {
            if (redirectUrl == null || redirectUrl.isEmpty()){
                redirectUrl = "/login";
            }
            if (user.get().isActive()) {
                message = "E-mail je već potvrđen!";
            } else {
                user.get().setActive(true);
                userRepository.save(user.get());
                message = "E-mail potvrda je uspešno završena!";
            }
        } else {
            message = "Neuspešna potvrda e-mail adrese!";
            redirectUrl = "/register";
        }
        model.addAttribute("confirmationMessage", message);
        model.addAttribute("redirectUrl", redirectUrl);
        return "email-confirmation";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String showLogoutPage() {
        return "logout";
    }

    @GetMapping("/logoutSuccess")
    public String customLogout(HttpServletRequest request, HttpServletResponse response) {
        // Get the Spring Authentication object of the current request.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // In case you are not filtering the users of this request url.
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:http://client-server:8081";
    }
}