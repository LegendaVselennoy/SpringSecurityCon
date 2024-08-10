package org.example.springguru.book.service;

import org.example.springguru.book.entity.Book;

public interface BookService {

    Iterable<Book> findAll();
}
