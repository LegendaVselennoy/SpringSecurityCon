package org.example.hibernate.joined;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "guitar", catalog = "test", schema = "test-engineer")
public class Guitar extends Instrument{

    private Integer numberOfGuitars;
}
