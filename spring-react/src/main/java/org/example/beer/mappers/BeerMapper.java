package org.example.beer.mappers;

import org.example.beer.domain.Beer;
import org.example.beer.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer fromBeerDTOToBeer(BeerDTO beerDTO);
    BeerDTO fromBeerToBeerDTO(Beer beer);
}
