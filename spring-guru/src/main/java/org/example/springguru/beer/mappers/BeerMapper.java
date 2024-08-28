package org.example.springguru.beer.mappers;

import org.example.springguru.beer.model.dto.BeerDTO;
import org.example.springguru.beer.model.entities.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDTO(Beer beer);
}