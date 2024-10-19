package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Customers;
import org.example.exception.DuplicateResourceException;
import org.example.service.CustomerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    void registerCustomers(@RequestBody Customers customers) {
        if (customerService.existsCustomersByEmailService(customers.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already taken"
            );
        }
        customerService.insertCustomer(customers);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable("customerId") Long customerId, @RequestBody Customers customer) {
        customerService.updateCustomer(customerId, customer);
    }
}
