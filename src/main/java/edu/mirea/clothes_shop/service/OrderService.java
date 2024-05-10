package edu.mirea.clothes_shop.service;

import edu.mirea.clothes_shop.dto.ItemDto;
import edu.mirea.clothes_shop.dto.ItemInOrderDto;
import edu.mirea.clothes_shop.dto.OrderDto;
import edu.mirea.clothes_shop.exception.*;
import edu.mirea.clothes_shop.model.entity.Item;
import edu.mirea.clothes_shop.model.entity.Order;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.OrderStatus;
import edu.mirea.clothes_shop.repository.ItemRepository;
import edu.mirea.clothes_shop.repository.OrderRepository;
import edu.mirea.clothes_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new UserDoesNotExistException("User does not exist");
        }
        return orderRepository.getOrdersByUserUserId(userRepository.findByEmail(email).get().getUserId()).stream()
                .map(order -> new OrderDto(
                        order.getOrderId(),
                        order.getStatus(),
                        order.getUser().getUserId(),
                        order.getItems().stream()
                                .map(
                                        orderItem -> new ItemInOrderDto(
                                                orderItem.getItem().getItemId(),
                                                orderItem.getItem().getItemName(),
                                                orderItem.getItem().getDescription(),
                                                orderItem.getItem().getBrand(),
                                                orderItem.getItem().getType(),
                                                orderItem.getItem().getSize(),
                                                orderItem.getItem().getColor(),
                                                orderItem.getItem().getPrice(),
                                                orderItem.getAmount(),
                                                orderItem.getItem().getImgPath()
                                        )
                                ).toList()
                )).toList();
    }

    @Transactional
    public void addItemInCurrentOrder(String email, Long item_id) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new UserDoesNotExistException("User does not exist");
        }
        User user = userRepository.findByEmail(email).get();

        Optional<Order> orderOptional = orderRepository.getOrderByUser_UserIdAndStatus(user.getUserId(), OrderStatus.IN_PROCESS);
        Order currentOrder;
        if (orderOptional.isEmpty()) {
            currentOrder = new Order();
            currentOrder.setStatus(OrderStatus.IN_PROCESS);
            currentOrder.setUser(user);
            currentOrder.setItems(new ArrayList<>());
        } else {
            currentOrder = orderOptional.get();
        }

        if (itemRepository.findById(item_id).isEmpty()) {
            throw new ItemDoesNotExistException("No item with that ID");
        }

        Item item = itemRepository.findById(item_id).get();
        if (item.getAmount() == 0) {
            throw new NotEnoughItemsException("Item is not available currently");
        }
        item.setAmount(item.getAmount() - 1);
        currentOrder.addItem(item);
        orderRepository.save(currentOrder);
        itemRepository.save(item);
    }

    @Transactional
    public void deleteItemFromCurrentOrder(String email, Long item_id) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new UserDoesNotExistException("User does not exist");
        }
        User user = userRepository.findByEmail(email).get();

        Optional<Order> orderOptional = orderRepository.getOrderByUser_UserIdAndStatus(user.getUserId(), OrderStatus.IN_PROCESS);
        Order currentOrder;
        if (orderOptional.isEmpty()) {
            throw new NoSuchItemInOrderException("Order does not contain that item");
        } else {
            currentOrder = orderOptional.get();
        }

        if (itemRepository.findById(item_id).isEmpty()) {
            throw new ItemDoesNotExistException("No item with that ID");
        }
        Item item = itemRepository.findById(item_id).get();
        item.setAmount(item.getAmount() + 1);
        currentOrder.removeItem(item);
        orderRepository.save(currentOrder);
        itemRepository.save(item);
    }

    @Transactional
    public OrderDto getCurrentOrder(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new UserDoesNotExistException("User does not exist");
        }
        User user = userRepository.findByEmail(email).get();

        Optional<Order> orderOptional = orderRepository.getOrderByUser_UserIdAndStatus(user.getUserId(), OrderStatus.IN_PROCESS);
        Order currentOrder;
        if (orderOptional.isPresent()) {
            currentOrder = orderOptional.get();
        } else {
            currentOrder = new Order();
            currentOrder.setStatus(OrderStatus.IN_PROCESS);
            currentOrder.setUser(user);
            currentOrder.setItems(new ArrayList<>());
            orderRepository.save(currentOrder);
        }

        return new OrderDto(
                currentOrder.getOrderId(),
                currentOrder.getStatus(),
                currentOrder.getUser().getUserId(),
                currentOrder.getItems().stream()
                        .map(
                                orderItem -> new ItemInOrderDto(
                                        orderItem.getItem().getItemId(),
                                        orderItem.getItem().getItemName(),
                                        orderItem.getItem().getDescription(),
                                        orderItem.getItem().getBrand(),
                                        orderItem.getItem().getType(),
                                        orderItem.getItem().getSize(),
                                        orderItem.getItem().getColor(),
                                        orderItem.getItem().getPrice(),
                                        orderItem.getAmount(),
                                        orderItem.getItem().getImgPath()
                                )
                        ).toList()
        );
    }

    @Transactional
    public void makeOrder(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new UserDoesNotExistException("User does not exist");
        }
        User user = userRepository.findByEmail(email).get();

        Optional<Order> orderOptional = orderRepository.getOrderByUser_UserIdAndStatus(user.getUserId(), OrderStatus.IN_PROCESS);
        if (orderOptional.isPresent() && !orderOptional.get().getItems().isEmpty()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.ORDERED);
            orderRepository.save(order);
        } else {
            throw new OrderIsEmptyException("Order is empty");
        }
    }
}
