package edu.mirea.clothes_shop.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "OrderItem")
@Table(name = "order_item")
@Data
@NoArgsConstructor
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;

    @ManyToOne
    @MapsId("orderId")
    private Order order;

    @ManyToOne
    @MapsId("itemId")
    private Item item;

    private int amount;

    public OrderItem(Order order, Item item) {
        this.order = order;
        this.item = item;
        this.id = new OrderItemId(order.getOrderId(), item.getItemId());
        this.amount = 1;
    }
}
