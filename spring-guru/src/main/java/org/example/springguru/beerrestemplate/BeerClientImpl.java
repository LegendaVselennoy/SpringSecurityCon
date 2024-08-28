package org.example.springguru.beerrestemplate;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.example.springguru.beerrestemplate.client.BeerClient;
import org.example.springguru.beerrestemplate.model.BeerDTORestam;
import org.example.springguru.beerrestemplate.model.RestPageImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;
//    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/rest";
    private static final String GET_BEER_BY_ID_PATH = "/rest/{beerId}";

    @Override
    public Page<BeerDTORestam> listBeers(String beerName) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }

        ResponseEntity<RestPageImpl> response =
                restTemplate.getForEntity(uriComponentsBuilder.toUriString(), RestPageImpl.class);

//        ResponseEntity<String> stringResponse =
//                restTemplate.getForEntity(BASE_URL+GET_BEER_PATH, String.class);

//        ResponseEntity<Map>mapResponse =
//                restTemplate.getForEntity(BASE_URL+GET_BEER_PATH, Map.class);
//        ResponseEntity<JsonNode>jsonResponse =
//                restTemplate.getForEntity(BASE_URL+GET_BEER_PATH, JsonNode.class);
//
//        jsonResponse.getBody().findPath("content")
//                .elements()
//                .forEachRemaining(node ->{
//                    System.out.println(node.get("beerName").asText());
//                });
//
//        System.out.println(stringResponse.getBody());
        return response.getBody();
    }

    @Override
    public BeerDTORestam getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(GET_BEER_BY_ID_PATH,BeerDTORestam.class,beerId);
    }

    @Override
    public BeerDTORestam createBeer(BeerDTORestam newDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<BeerDTORestam> response = restTemplate.postForEntity(GET_BEER_PATH,newDto,BeerDTORestam.class);
        URI uri = restTemplate.postForLocation(GET_BEER_PATH,newDto);
        return restTemplate.getForObject(uri.getPath(),BeerDTORestam.class);
    }

    @Override
    public BeerDTORestam updateBeer(BeerDTORestam beerDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(GET_BEER_BY_ID_PATH,beerDto,beerDto.getBeerId());
        return getBeerById(beerDto.getBeerId());
    }

    @Override
    public void deleteBeer(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(GET_BEER_BY_ID_PATH,beerId);
    }
}
