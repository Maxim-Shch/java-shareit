package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired //данная аннотация автоматически связывает бины (компоненты)
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    //POST /items - добавление новой вещи пользователем
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        log.info("Добавление новой вещи пользователем: {}", userId);
        return itemService.addItem(userId, itemDto);
    }

    //PATCH /items/{itemId} - Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Редактирование вещи по ID: {}", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    //GET /items/{itemId} - Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId) {
        log.info("Просмотр информации о конкретной вещи по её ID: {}", itemId);
        return itemService.getItemById(itemId);
    }

    //GET /items - Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой
    @GetMapping
    public List<ItemDto> getAllItemsByOwnerId(@RequestHeader ("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой: {}", userId);
        return itemService.getAllItemsByOwnerId(userId);
    }

    // /items/search?text={text} - Поиск вещи потенциальным арендатором.
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader ("X-Sharer-User-Id") Long userId,
                                     @RequestParam("text") String text) {
        log.info("Поиск вещи потенциальным арендатором: {}", text);
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemService.searchItems(userId, text);
        }
    }
}
