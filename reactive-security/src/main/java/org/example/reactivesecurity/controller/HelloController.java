package org.example.reactivesecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @GetMapping("/hello")
    // Использует @PreAuthorize для ограничения доступа к методу
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Mono<String> hello(Mono<Authentication> authentication) {
        Mono<String> message=authentication.map(a->"Hello "+a.getName());
        return message;
    }

}