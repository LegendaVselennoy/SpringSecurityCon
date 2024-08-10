package org.example.springguru.beer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.springguru.beer.model.Beer;
import org.example.springguru.beer.service.BeerService;
import org.example.springguru.beer.service.impl.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

      @Autowired
      MockMvc mockMvc;
      @Autowired
      ObjectMapper mapper;
      @MockBean
      BeerService beerService;
      BeerServiceImpl beerServiceImpl;

      @BeforeEach
      void setUp() {
          beerServiceImpl = new BeerServiceImpl();
      }

    @Test
    void getBeerId() {

        Beer testBeer = beerServiceImpl.listBeers().get(0);

//        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        given(beerService.getBeerById(testBeer.getBeerId())).willReturn(testBeer);

        try {
            mockMvc.perform(get("/beer/"+testBeer.getBeerId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.beerId",is(testBeer.getBeerId().toString())))
                    .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateNewBeer() throws Exception {

        Beer beer=beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setBeerId(null);

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc.perform(post("/beer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void testUpdateBeer() throws Exception {
          Beer beer=beerServiceImpl.listBeers().get(0);

          mockMvc.perform(put("/beer/"+beer.getBeerId())
                  .accept(MediaType.APPLICATION_JSON)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(mapper.writeValueAsString(beer)))
                  .andExpect(status().isNoContent());

          verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testListBeers() {

        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        try {
            mockMvc.perform(get("/beer")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()",is(3)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}