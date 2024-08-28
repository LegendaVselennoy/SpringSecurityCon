package org.example.springguru.beer.mappers.impl;

import org.example.springguru.beer.mappers.CustomerMapper;
import org.example.springguru.beer.model.dto.CustomerDTO;
import org.example.springguru.beer.model.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer customerDtoToCustomer(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.customerId(dto.getCustomerId());
        customer.name(dto.getName());
        customer.version(dto.getVersion());
        customer.createdDate(dto.getCreatedDate());
        customer.updateDate(dto.getUpdateDate());

        return customer.build();
    }

    @Override
    public CustomerDTO customerToCustomerDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDTO.CustomerDTOBuilder customerDTO = CustomerDTO.builder();

        customerDTO.customerId(customer.getCustomerId());
        customerDTO.name(customer.getName());
        customerDTO.version(customer.getVersion());
        customerDTO.createdDate(customer.getCreatedDate());
        customerDTO.updateDate(customer.getUpdateDate());

        return customerDTO.build();
    }
}
