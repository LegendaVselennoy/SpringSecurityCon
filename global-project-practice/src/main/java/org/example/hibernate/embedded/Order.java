package org.example.hibernate.embedded;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.hibernate.otm.OrderLine;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "orderLines")
@ToString(exclude = "orderLines")
@Table(name = "orders", catalog = "test", schema = "test-engineer")
@AttributeOverrides(
        @AttributeOverride(
                name = "address.city",
                column = @Column(name = "city")
        )
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Address address;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "order")
    private Set<OrderLine> orderLines;
    //private Address billToAddress;
}
