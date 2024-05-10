package edu.mirea.clothes_shop.service;

import edu.mirea.clothes_shop.dto.AddItemRequestDto;
import edu.mirea.clothes_shop.dto.FillItemRequestDto;
import edu.mirea.clothes_shop.dto.ItemDto;
import edu.mirea.clothes_shop.exception.ItemDoesNotExistException;
import edu.mirea.clothes_shop.model.entity.Item;
import edu.mirea.clothes_shop.model.enums.ClothesBrand;
import edu.mirea.clothes_shop.model.enums.ClothesColor;
import edu.mirea.clothes_shop.model.enums.ClothesSize;
import edu.mirea.clothes_shop.model.enums.ClothesType;
import edu.mirea.clothes_shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void addItem(AddItemRequestDto addItemRequestDto) {
        Item item = new Item(
                addItemRequestDto.name(),
                addItemRequestDto.description(),
                addItemRequestDto.brand(),
                addItemRequestDto.type(),
                addItemRequestDto.size(),
                addItemRequestDto.color(),
                addItemRequestDto.amount(),
                addItemRequestDto.price(),
                addItemRequestDto.imgPath()
        );
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(item -> new ItemDto(
                        item.getItemId(),
                        item.getItemName(),
                        item.getDescription(),
                        item.getBrand(),
                        item.getType(),
                        item.getSize(),
                        item.getColor(),
                        item.getAmount(),
                        item.getPrice(),
                        item.getImgPath()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getItems() {
        return itemRepository.findAllByAmountGreaterThan(0).stream()
                .map(item -> new ItemDto(
                        item.getItemId(),
                        item.getItemName(),
                        item.getDescription(),
                        item.getBrand(),
                        item.getType(),
                        item.getSize(),
                        item.getColor(),
                        item.getAmount(),
                        item.getPrice(),
                        item.getImgPath()
                ))
                .toList();
    }

    @Transactional
    public void fillItem(FillItemRequestDto fillItemRequestDto) {
        Item item = itemRepository.findById(fillItemRequestDto.itemId()).get();
        item.setAmount(item.getAmount() + fillItemRequestDto.amount());
        itemRepository.save(item);
    }

    @Transactional
    public List<ItemDto> getFilteredItems(ClothesBrand brand, ClothesColor color, ClothesSize size, ClothesType type) {
        List<Item> items = itemRepository.findAll();
            items = items.stream()
                    .filter(item -> brand == null || item.getBrand().equals(brand))
                    .filter(item -> color == null || item.getColor().equals(color))
                    .filter(item -> size == null || item.getSize().equals(size))
                    .filter(item -> type == null || item.getType().equals(type))
                    .toList();
            return items.stream()
                    .map(item -> new ItemDto(
                            item.getItemId(),
                            item.getItemName(),
                            item.getDescription(),
                            item.getBrand(),
                            item.getType(),
                            item.getSize(),
                            item.getColor(),
                            item.getAmount(),
                            item.getPrice(),
                            item.getImgPath()
                    ))
                    .toList();
    }

    @Transactional(readOnly = true)
    public ItemDto getItem(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new ItemDoesNotExistException("Item not found");
        }
        Item item = itemRepository.findById(itemId).get();
        return new ItemDto(
                item.getItemId(),
                item.getItemName(),
                item.getDescription(),
                item.getBrand(),
                item.getType(),
                item.getSize(),
                item.getColor(),
                item.getAmount(),
                item.getPrice(),
                item.getImgPath()
        );
    }
}
