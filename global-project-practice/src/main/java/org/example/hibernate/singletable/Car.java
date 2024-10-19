package org.example.hibernate.singletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("car")
public class Car extends Vehicle {

    private String trimLevel;
}
