package org.example.hibernate.msuper;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_header", catalog = "test", schema = "test-engineer")
public class OrderHeader extends BaseEntity {

    private String customerName;
}
