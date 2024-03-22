package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.Constants;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createNewRequest(@RequestHeader(Constants.X_SHARER_USER_ID) final Long userId,
                                           @RequestBody ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isBlank()) {
            throw new BadRequestException("Описание запроса не может быть пустым!");
        }
        return requestService.addNewRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> getUserRequests(@RequestHeader(Constants.X_SHARER_USER_ID) final Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader(Constants.X_SHARER_USER_ID) final Long userId,
                                                     @RequestParam(value = "from", defaultValue = "0")
                                                     final Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10")
                                                     final Integer size) {
        if (from < 0) {
            throw new BadRequestException("Некорректно переданный параметр запроса");
        }
        return requestService.getAllRequestsForAllUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(Constants.X_SHARER_USER_ID) final Long userId,
                                         @PathVariable final Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}