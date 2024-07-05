package org.example.demosecurity.converter;

import org.example.demosecurity.custom.CustomAuthentication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, CustomAuthentication> {

    @Override
    public CustomAuthentication convert(Jwt source) {
        List<GrantedAuthority> authorities = List.of(() -> "read");
        // Получение значений приоритета из настраиваемого утверждения маркера
        String priority = String.valueOf(source.getClaims().get("priority"));
        // Установка значения приоритета в объекте аутентификации
        return new CustomAuthentication(source,
                authorities,
                priority);
    }
}
