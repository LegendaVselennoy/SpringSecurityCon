package org.example.demosecurity.custom;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


import java.util.Collection;

// Настройка объекта аутентификации путем расширения класса JwtAuthenticationToken
public class CustomAuthentication extends JwtAuthenticationToken {

    // Добавление произвольного поля "приоритет"
    private final String priority;

    public CustomAuthentication(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String priority) {
        super(jwt, authorities);
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }
}
