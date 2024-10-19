package org.example.hibernate.otm;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.hibernate.embedded.Order;

@Entity
@Getter
@Setter
@Table(name = "order_line", catalog = "test", schema = "test-engineer")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantityOrdered;
    @ManyToOne
    private Order order;
}
