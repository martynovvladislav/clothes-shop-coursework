package edu.mirea.clothes_shop.model.entity;

import edu.mirea.clothes_shop.exception.NoSuchItemInOrderException;
import edu.mirea.clothes_shop.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "`order`")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderItem> items;

    public void addItem(Item item) {
        for (var orderItem : items) {
            if (orderItem.getItem().equals(item) && orderItem.getOrder().equals(this)) {
                orderItem.setAmount(orderItem.getAmount() + 1);
                return;
            }
        }
        OrderItem orderItem = new OrderItem(this, item);
        items.add(orderItem);
        item.getOrders().add(orderItem);
    }

    public void removeItem(Item item) {
        for (var orderItem : items) {
            if (orderItem.getItem().equals(item) && orderItem.getOrder().equals(this)) {
                if (orderItem.getAmount() > 1) {
                    orderItem.setAmount(orderItem.getAmount() - 1);
                } else {
                    items.remove(orderItem);
                    item.getOrders().remove(orderItem);
                    orderItem.setItem(null);
                    orderItem.setOrder(null);
                }
                return;
            }
        }
        throw new NoSuchItemInOrderException("Order does not contain that item");
    }
}
