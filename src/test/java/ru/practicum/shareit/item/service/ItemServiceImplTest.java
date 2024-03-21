package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ItemRequestRepository requestRepository;
    @InjectMocks
    ItemServiceImpl itemService;

    private final User user = new User(1L, "user", "user@mail.ru");
    private final ItemDto itemDto = ItemDto.builder().id(1L).name("itemName").description("itemDesc")
            .available(true).requestId(1L).build();
    private final ItemRequest request = ItemRequest.builder().id(itemDto.getRequestId()).description("req desc")
            .build();

    @Test
    void testCreateItem() {
        Item item = ItemMapper.toItem(itemDto, user);
        item.setRequest(request);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(itemDto.getRequestId())).thenReturn(Optional.of(request));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto createdItem = itemService.addItem(user.getId(), itemDto);

        assertNotNull(createdItem);
        assertEquals(itemDto.getName(), createdItem.getName());
        assertEquals(itemDto.getDescription(), createdItem.getDescription());
        assertEquals(itemDto.getRequestId(), createdItem.getRequestId());

        verify(userRepository).findById(user.getId());
        verify(requestRepository).findById(itemDto.getRequestId());
        verify(itemRepository).save(item);
    }

    @Test
    void testCreateWithWrongUser() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addItem(100L, itemDto));

        verify(userRepository).findById(100L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void createItemWithWrongRequestId() {
        ItemDto itemDtoWithBadRequestId = ItemDto.builder().id(1L).name("itemName").description("itemDesc")
                .available(true).requestId(100L).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(itemDtoWithBadRequestId.getRequestId())).thenThrow(
                NotFoundException.class
        );

        assertThrows(NotFoundException.class, () ->
                itemService.addItem(user.getId(), itemDtoWithBadRequestId));

        verify(userRepository).findById(user.getId());
        verify(requestRepository).findById(100L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void testGetUserItems() {
        Long userId = user.getId();
        Item item = ItemMapper.toItem(itemDto, user);
        Item secondItem = Item.builder().id(1L).name("item2Name").description("item2Desc").available(true)
                .owner(user).build();

        List<Item> items = Arrays.asList(item, secondItem);
        int from = 0;
        int size = 10;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findByOwner(user, PageRequest.of(from, size))).thenReturn(items);
        when(commentRepository.findByItemOrderByIdAsc(item)).thenReturn(Collections.emptyList());
        when(commentRepository.findByItemOrderByIdAsc(secondItem)).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItem(item)).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItem(secondItem)).thenReturn(Collections.emptyList());

        Collection<ItemDto> userItems = itemService.getAllItemsByOwnerId(userId, from, size);

        assertNotNull(userItems);
        assertEquals(2, userItems.size());

        verify(userRepository).findById(userId);
        verify(itemRepository).findByOwner(user, PageRequest.of(0, size));
        verify(commentRepository, times(2)).findByItemOrderByIdAsc(any(Item.class));
        verify(bookingRepository, times(2)).findByItem(any(Item.class));
    }

    @Test
    void testGetUserItemsWithWrongUser() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getAllItemsByOwnerId(100L, 0, 10));

        verify(userRepository).findById(100L);
    }

    @Test
    void testGetItemById() {
        Item item = Item.builder().id(1L).name("item2Name").description("item2Desc").available(true)
                .owner(user).build();
        List<Comment> comments = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();

        ItemDto expectedItemDto = ItemMapper.toItemDto(item, comments, bookings);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(commentRepository.findByItemOrderByIdAsc(item)).thenReturn(comments);
        when(bookingRepository.findByItem(item)).thenReturn(bookings);

        ItemDto actualItemDto = itemService.getItemById(item.getId(), user.getId());

        assertEquals(expectedItemDto, actualItemDto);
        verify(itemRepository).findById(item.getId());
        verify(commentRepository).findByItemOrderByIdAsc(item);
        verify(bookingRepository).findByItem(item);
        verifyNoMoreInteractions(itemRepository, commentRepository, bookingRepository);
    }

    @Test
    void testGetItemByIdWithWrongItem() {
        when(itemRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(100L, 1L));

        verify(itemRepository).findById(100L);
    }

    @Test
    void testFindAllItems() {
        when(itemRepository.findAll()).thenReturn(List.of(ItemMapper.toItem(itemDto, user)));

        Collection<ItemDto> allDtoItems = itemService.findAll();

        assertFalse(allDtoItems.isEmpty());
        assertEquals(1, allDtoItems.size());
    }

    @Test
    void testFindAllItemsWithEmptyList() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<ItemDto> allDtoItems = itemService.findAll();

        assertTrue(allDtoItems.isEmpty());
    }

    @Test
    void testUpdateItem() {
        Item item =  Item.builder().id(1L).name("item2Name").description("item2Desc").available(true)
                .owner(user).build();
        Long itemId = item.getId();
        item.setRequest(request);

        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder().name("Updated Item").description("Updated Description")
                        .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto actualItemDto = itemService.updateItem(user.getId(), itemId, itemUpdateDto);

        assertEquals("Updated Item", actualItemDto.getName());
        assertEquals("Updated Description", actualItemDto.getDescription());
        verify(itemRepository).findById(itemId);
        verify(itemRepository).save(item);
        verifyNoMoreInteractions(itemRepository, commentRepository, bookingRepository, userRepository);
    }

    @Test
    void testUpdateItemWithUserNotOwner() {
        User userNotOwner = new User(2L, "user2", "user2@mail.ru");
        Item item =  Item.builder().id(1L).name("item2Name").description("item2Desc").available(true)
                .owner(user).build();
        Long itemId = item.getId();
        item.setRequest(request);

        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder().name("Updated Item").description("Updated Description")
                .build();

        when(userRepository.findById(userNotOwner.getId())).thenReturn(Optional.of(userNotOwner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(AccessException.class, () ->
                itemService.updateItem(userNotOwner.getId(), itemId, itemUpdateDto));
        verify(itemRepository).findById(itemId);
    }

    @Test
    void testDeleteItem() {
        Item item =  Item.builder().id(1L).name("item2Name").description("item2Desc").available(true)
                .owner(user).build();
        itemService.deleteItem(item.getId());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));

        verify(itemRepository, times(1)).deleteById(item.getId());
    }

    @Test
    void searchItem() {
        ItemDto itemDtoForSearch = ItemDto.builder().id(2L).name("Щетка для обуви").description("Хорошо чистит")
                .available(true).build();
        Item itemForSearch = Item.builder().id(2L).name("Щетка для обуви").description("Хорошо чистит")
                .available(true).build();

        when(itemRepository.search("щетка", PageRequest.of(0, 10, Sort.by("name")
                .ascending())))
                .thenReturn(List.of(itemForSearch));

        Collection<ItemDto> actualItems = itemService.searchItems(1L, "щетка", 0, 10);

        assertEquals(1, actualItems.size());
        assertTrue(actualItems.contains(itemDtoForSearch));

        verify(itemRepository).search("щетка", PageRequest.of(0, 10, Sort.by("name")
                .ascending()));
    }
}
