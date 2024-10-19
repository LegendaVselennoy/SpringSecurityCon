package org.example.hibernate.joined;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "electric_guitar", catalog = "test", schema = "test-engineer")
public class ElectricGuitar extends Guitar{

    private Integer numberOfElectricGuitars;
}
