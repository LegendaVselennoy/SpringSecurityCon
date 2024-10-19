package org.example.hibernate.tableperclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dog", catalog = "test", schema = "test-engineer")
public class Dog extends Animal {

    private String breed;
}
