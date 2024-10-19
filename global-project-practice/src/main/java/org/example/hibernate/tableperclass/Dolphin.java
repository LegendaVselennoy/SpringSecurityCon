package org.example.hibernate.tableperclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dolphin", catalog = "test", schema = "test-engineer")

public class Dolphin extends Animal{

    private Boolean hasSpots;
}
