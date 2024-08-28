package org.example.springguru.beer.mappers;

import org.example.springguru.beer.model.dto.CustomerDTO;
import org.example.springguru.beer.model.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
