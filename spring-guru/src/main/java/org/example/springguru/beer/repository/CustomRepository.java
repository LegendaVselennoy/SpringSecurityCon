package org.example.springguru.beer.repository;

import org.example.springguru.beer.model.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomRepository extends JpaRepository<Customer, UUID> {
}
