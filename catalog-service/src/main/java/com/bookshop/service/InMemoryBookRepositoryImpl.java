package com.bookshop.service;

//@Repository
//public class InMemoryBookRepositoryImpl implements BookRepository {
//
//    private static final Map<String, BookDTO> books = new ConcurrentHashMap<>();
//
//    @Override
//    public Iterable<BookDTO> findAll() {
//        return books.values();
//    }
//
//    @Override
//    public Optional<BookDTO> findByIsbn(String isbn) {
//        return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
//    }
//
//    @Override
//    public boolean existsByIsbn(String isbn) {
//        return books.get(isbn) != null;
//    }
//
//    @Override
//    public BookDTO save(BookDTO book) {
//        books.put(book.isbn(), book);
//        return book;
//    }
//
//    @Override
//    public void deleteByIsbn(String isbn) {
//        books.remove(isbn);
//    }
//}
