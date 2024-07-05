package org.example.clientoauth.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    // Внедрение сведений об аутентификации в параметр метода
    public String home(OAuth2AuthenticationToken authentication) {
        return "index.html";
    }

}
