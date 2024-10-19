package org.example.hibernate.joined;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "piano", catalog = "test", schema = "test-engineer")
public class Piano extends Instrument {

    private Integer number;
}
