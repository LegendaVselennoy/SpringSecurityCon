package com.demo.service;

import com.demo.model.Book;
import com.demo.model.Order;
import com.demo.model.OrderStatus;
import com.demo.repository.OrderRepository;
import com.demo.service.book.BookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(
                        buildRejectedOrder(isbn, quantity)
                )
                .flatMap(orderRepository::save);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(),book.title() + "-" + book.author(),
                book.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(
            String bookIsbn, int quantity
    ) {
        return Order.of(bookIsbn,null,null,quantity, OrderStatus.REJECTED);
    }
}