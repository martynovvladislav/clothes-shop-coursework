package edu.mirea.clothes_shop.service;

import edu.mirea.clothes_shop.dto.ItemInOrderDto;
import edu.mirea.clothes_shop.dto.OrderDto;
import edu.mirea.clothes_shop.dto.UserDto;
import edu.mirea.clothes_shop.dto.UserWithOrdersDto;
import edu.mirea.clothes_shop.exception.UserDoesNotExistException;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getUserId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserWithOrdersDto getUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserDoesNotExistException("There is no user with that id");
        }
        User user = userRepository.findById(userId).get();
        UserDto info = new UserDto(
                userId,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
        return new UserWithOrdersDto(
                info,
                user.getOrders().stream()
                        .map(order -> new OrderDto(
                               order.getOrderId(),
                               order.getStatus(),
                               userId,
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
                        ))
                        .toList()
        );
    }

    @Transactional
    public void grantUser(Long userId, UserRole newRole) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserDoesNotExistException("There is no user with that id");
        }
        User user = userRepository.findById(userId).get();
        user.setRole(newRole);
        userRepository.save(user);
    }
}
