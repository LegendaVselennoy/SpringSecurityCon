package org.example.springguru.book.controller;

import lombok.RequiredArgsConstructor;
import org.example.springguru.book.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping()
    public String getBooks(Model model) {
        model.addAttribute("books",bookService.findAll());
        return "books";
    }
}
