package org.example.springguru.beer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springguru.beer.model.dto.BeerDTO;
import org.example.springguru.beer.service.BeerService;
import org.example.springguru.beer.service.impl.BeerServiceImpl;
import org.example.springguru.config.SpringSecConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
class BeerControllerTest {

    public static final String BEER_PATH = "/beer/";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor =
            jwt().jwt(jwt -> {
                jwt.claims(claims -> {
                            claims.put("scope","message-read");
                            claims.put("scope","message-write");
                        })
                        .subject("messaging-client")
                        .notBefore(Instant.now().minusSeconds(5L));
            });

      @Autowired
      MockMvc mockMvc;
      @Autowired
      ObjectMapper mapper;
      @MockBean
      BeerService beerService;
      BeerServiceImpl beerServiceImpl;
      @Captor
      ArgumentCaptor<UUID> uuidArgumentCaptor;
      @Captor
      ArgumentCaptor<BeerDTO> beerArgumentCaptor;

      @BeforeEach
      void setUp() {
          beerServiceImpl = new BeerServiceImpl();
      }

    @Test
    void getBeerId() {

        BeerDTO testBeer = beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(0);

//        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        given(beerService.getBeerById(testBeer.getBeerId())).willReturn(Optional.of(testBeer));

        try {
            mockMvc.perform(get(BEER_PATH + testBeer.getBeerId())
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
    void getBeerByIdNotFound() throws Exception {

          given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

          mockMvc.perform(get(BEER_PATH + UUID.randomUUID()))
                  .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewBeer() throws Exception {

        BeerDTO beer=beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(0);
        beer.setVersion(null);
        beer.setBeerId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(1));

        mockMvc.perform(post("/beer")
                        .with(httpBasic("user", "password"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer =
                beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BEER_PATH + beer.getBeerId())
                       // .with(httpBasic("user", "password"))
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getBeerId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer =
                beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(0);

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BEER_PATH + beer.getBeerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

//        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
//          verify(beerService).deleteById(any());
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getBeerId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
          BeerDTO beer=beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(0);

          given(beerService.updateBeerById(any(),any())).willReturn(Optional.of(beer));

          mockMvc.perform(put(BEER_PATH+beer.getBeerId())
                  .accept(MediaType.APPLICATION_JSON)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(mapper.writeValueAsString(beer)))
                  .andExpect(status().isNoContent());

          verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testListBeers() {

        given(beerService.listBeers(any(),any(),any(), 1, 25))
                .willReturn(beerServiceImpl.listBeers(null,null,false, 1, 25));

        try {
            mockMvc.perform(get("/beer")
                            .with(httpBasic(USERNAME,PASSWORD))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.length()",is(3)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateBeerNullBeerName() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.listBeers(null,null,null, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .with(httpBasic(USERNAME,PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}