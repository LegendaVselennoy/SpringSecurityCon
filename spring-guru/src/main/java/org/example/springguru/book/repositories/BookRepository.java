package org.example.springguru.book.repositories;

import org.example.springguru.book.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
