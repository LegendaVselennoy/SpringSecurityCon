package org.example.springguru.book.repositories;

import org.example.springguru.book.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorRepository extends JpaRepository<Author, Long> {
}
