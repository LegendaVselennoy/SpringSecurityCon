package org.example.repository;

import org.example.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customers,Long> {
    boolean existsCustomersByEmail(String email);
    boolean existsCustomersById(Long id);
}
