package org.example.beer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.beer.mappers.BeerMapper;
import org.example.beer.model.BeerDTO;
import org.example.beer.repository.BeerRepository;
import org.example.beer.service.BeerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.fromBeerDTOToBeer(beerDTO))
                .map(beerMapper::fromBeerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    foundBeer.setBeerName(beerDTO.getBeerName());
                    foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    foundBeer.setPrice(beerDTO.getPrice());
                    foundBeer.setUpc(beerDTO.getUpc());
                    foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::fromBeerToBeerDTO);
    }

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .map(beerMapper::fromBeerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return beerRepository.findById(beerId)
                .map(beerMapper::fromBeerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if (StringUtils.hasText(beerDTO.getBeerName())) {
                        foundBeer.setBeerName(beerDTO.getBeerName());
                    }

                    if (StringUtils.hasText(beerDTO.getBeerStyle())) {
                        foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    }

                    if (beerDTO.getPrice() != null) {
                        foundBeer.setPrice(beerDTO.getPrice());
                    }

                    if (StringUtils.hasText(beerDTO.getUpc())) {
                        foundBeer.setUpc(beerDTO.getUpc());
                    }

                    if (beerDTO.getQuantityOnHand() != null) {
                        foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }

                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(beerMapper::fromBeerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return beerRepository.deleteById(beerId);
    }
}
