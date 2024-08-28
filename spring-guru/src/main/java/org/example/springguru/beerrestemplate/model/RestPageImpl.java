package org.example.springguru.beerrestemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
public class RestPageImpl<BeerDTORestam> extends PageImpl<BeerDTORestam>{

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(@JsonProperty("content") List<BeerDTORestam> content,
                        @JsonProperty("number") int page,
                        @JsonProperty("size") int size,
                        @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page,size), total);
    }

    public RestPageImpl(List<BeerDTORestam> content) {
        super(content);
    }
}
