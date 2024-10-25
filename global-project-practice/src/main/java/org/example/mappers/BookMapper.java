package org.example.mappers;

import org.example.dto.BookDto;
import org.example.entity.Book;
import org.mapstruct.Mapper;

@Mapper
public interface    BookMapper {

    Book bookFromDto(BookDto bookDto);
    BookDto bookDtoFromBook(Book book);
}