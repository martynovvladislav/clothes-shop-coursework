package edu.mirea.clothes_shop.repository;

import edu.mirea.clothes_shop.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByAmountGreaterThan(int amount);
}
