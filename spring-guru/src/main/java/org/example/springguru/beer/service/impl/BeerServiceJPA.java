package org.example.springguru.beer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.springguru.beer.model.BeerStyle;
import org.example.springguru.beer.model.dto.BeerDTO;
import org.example.springguru.beer.model.entities.Beer;
import org.example.springguru.beer.repository.BeerRepository;
import org.example.springguru.beer.service.BeerService;
import org.example.springguru.beer.mappers.BeerMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageName, Integer pageSize) {
//        return beerRepository.findAll()
//                .stream()
//                .map(beerMapper::beerToBeerDTO)
//                .collect(Collectors.toList());
        PageRequest pageRequest = buildPageRequest(pageName, pageSize);
        Page<Beer> beerPage;
        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeerByName(beerName);
        }else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle);
        }else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeerByNameAndStyle(beerName,beerStyle);
        }
        else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        if (showInventory !=null && !showInventory) {
             beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }
        return beerPage.map(beerMapper::beerToBeerDTO);
//        return beerPage
//                .stream()
//                .map(beerMapper::beerToBeerDTO)
//                .collect(Collectors.toList());
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null  && pageSize > 0) {
            queryPageNumber = pageNumber - 1;
        }else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            }else {
                queryPageSize = pageSize;
            }
        }
        Sort sort = Sort.by(Sort.Order.asc("beerName"));
        return PageRequest.of(queryPageNumber, queryPageSize,sort);
    }

    private Page<Beer> listBeerByNameAndStyle(String beerName, BeerStyle beerStyle) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%",beerStyle, null);
    }

    public Page<Beer> listBeersByStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle, null);
    }

    public Page<Beer> listBeerByName(String name) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + name + "%", null);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {

        return Optional.of(beerMapper.beerToBeerDTO(
                beerRepository.save(beerMapper.beerDtoToBeer(beer))
        ));

//        AtomicReference<Optional<BeerDTO>>atomicReference = new AtomicReference<>();
//
//        beerRepository.findById(beerId).ifPresentOrElse(foundBeer ->{
//            foundBeer.setBeerName(beer.getBeerName());
//            foundBeer.setBeerStyle(beer.getBeerStyle());
//            foundBeer.setUpc(beer.getUpc());
//            foundBeer.setPrice(beer.getPrice());
//            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
//            foundBeer.setVersion(beer.getVersion());
//            beerRepository.save(foundBeer);
//            atomicReference.set(Optional.of(beerMapper
//                    .beerToBeerDTO(beerRepository.save(foundBeer))));
//        },() -> atomicReference.set(Optional.empty()));
//        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public void patchById(UUID beerId, BeerDTO beer) {

    }
}