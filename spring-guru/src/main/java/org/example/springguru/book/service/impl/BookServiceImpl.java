package org.example.springguru.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.springguru.book.entity.Book;
import org.example.springguru.book.repositories.BookRepository;
import org.example.springguru.book.service.BookService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }
}
