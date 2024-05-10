package edu.mirea.clothes_shop.repository;

import edu.mirea.clothes_shop.model.entity.Order;
import edu.mirea.clothes_shop.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getOrdersByUserUserId(Long userId);
    Optional<Order> getOrderByUser_UserIdAndStatus(Long userId, OrderStatus orderStatus);
}
