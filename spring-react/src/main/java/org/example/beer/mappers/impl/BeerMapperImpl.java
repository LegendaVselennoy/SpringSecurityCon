package org.example.beer.mappers.impl;

import org.example.beer.domain.Beer;
import org.example.beer.mappers.BeerMapper;
import org.example.beer.model.BeerDTO;

//@Component
public class BeerMapperImpl implements BeerMapper {

    @Override
    public Beer fromBeerDTOToBeer(BeerDTO dto) {
        if (dto == null) {
            return null;
        }

        Beer.BeerBuilder beer=Beer.builder();

        beer.id(dto.getId());
        beer.beerName(dto.getBeerName());
        beer.beerStyle(dto.getBeerStyle());
        beer.upc(dto.getUpc());
        beer.quantityOnHand(dto.getQuantityOnHand());
        beer.price(dto.getPrice());
        beer.createdDate(dto.getCreatedDate());
        beer.lastModifiedDate(dto.getLastModifiedDate());

        return beer.build();
    }

    @Override
    public BeerDTO fromBeerToBeerDTO(Beer beer) {
        if (beer == null) {
            return null;
        }

        BeerDTO.BeerDTOBuilder beerDTO = BeerDTO.builder();

        beerDTO.id(beer.getId());
        beerDTO.beerName(beer.getBeerName());
        beerDTO.beerStyle(beer.getBeerStyle());
        beerDTO.upc(beer.getUpc());
        beerDTO.quantityOnHand(beer.getQuantityOnHand());
        beerDTO.price(beer.getPrice());
        beerDTO.createdDate(beer.getCreatedDate());
        beerDTO.lastModifiedDate(beer.getLastModifiedDate());

        return beerDTO.build();
    }
}
