package org.example.springguru.beer.mappers.impl;

import org.example.springguru.beer.mappers.BeerMapper;
import org.example.springguru.beer.model.dto.BeerDTO;
import org.example.springguru.beer.model.entities.Beer;
import org.springframework.stereotype.Component;

@Component
public class BeerMapperImpl implements BeerMapper {

    @Override
    public Beer beerDtoToBeer(BeerDTO dto) {
        if (dto == null) {
            return null;
        }

        Beer.BeerBuilder beer=Beer.builder();

        beer.beerId(dto.getBeerId());
        beer.version(dto.getVersion());
        beer.beerName(dto.getBeerName());
        beer.beerStyle(dto.getBeerStyle());
        beer.upc(dto.getUpc());
        beer.quantityOnHand(dto.getQuantityOnHand());
        beer.price(dto.getPrice());
        beer.createdDate(dto.getCreatedDate());
        beer.updateDate(dto.getUpdateDate());

        return beer.build();
    }

    @Override
    public BeerDTO beerToBeerDTO(Beer beer) {
        if (beer == null) {
            return null;
        }

        BeerDTO.BeerDTOBuilder beerDTO = BeerDTO.builder();

        beerDTO.beerId(beer.getBeerId());
        beerDTO.version(beer.getVersion());
        beerDTO.beerName(beer.getBeerName());
        beerDTO.beerStyle(beer.getBeerStyle());
        beerDTO.upc(beer.getUpc());
        beerDTO.quantityOnHand(beer.getQuantityOnHand());
        beerDTO.price(beer.getPrice());
        beerDTO.createdDate(beer.getCreatedDate());
        beerDTO.updateDate(beer.getUpdateDate());

        return beerDTO.build();
    }
}
