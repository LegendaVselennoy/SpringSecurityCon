package com.demo.model;

public record Book(
        String isbn,
        String title,
        String author,
        Double price
) {
}