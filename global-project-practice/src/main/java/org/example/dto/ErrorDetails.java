package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDetails{
    private String message;

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "message='" + message + '\'' +
                '}';
    }
}
