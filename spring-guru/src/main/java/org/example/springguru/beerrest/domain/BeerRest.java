package org.example.springguru.beerrest.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "spring-guru",name = "beer_rest")
public class BeerRest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36,columnDefinition = "varchar", nullable = false,updatable = false)
    private UUID id;
    @Version
    private Long version;
    private String beerName;
    @Enumerated(value = EnumType.STRING)
    private BeerStyleEnum beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdData;
    @UpdateTimestamp
    private Timestamp lastModifiedData;

}
