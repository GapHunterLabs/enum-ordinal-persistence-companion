import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
class Order {

    // Flagged: no explicit EnumType -- persists as ORDINAL by default.
    @Enumerated
    private OrderStatus status;

    // Not flagged: explicit STRING.
    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;
}

enum OrderStatus { PENDING, SHIPPED, DELIVERED }
enum ShippingMethod { STANDARD, EXPRESS }
