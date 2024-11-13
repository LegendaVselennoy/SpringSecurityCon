package com.demo.repository;

import com.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @PreAuthorize("hasRole('ADMIN')")
    List<Customer> findByCustomerIdOrderByDesc(Long customerId);
    Optional<Customer> findByEmail(String email);
}