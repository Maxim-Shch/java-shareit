package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@Validated
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private CommentService commentService;

    private final ItemDto itemDto = ItemDto.builder().id(1L).name("itemName")
            .description("itemDesc").available(true).build();
    private final CommentDto commentDto = CommentDto.builder().id(1L).text("test comment").item(itemDto)
            .authorName("authorName").created(LocalDateTime.now()).build();

    @Test
    void createItem() throws Exception {
        when(itemService.addItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));

        verify(itemService).addItem(anyLong(), any(ItemDto.class));
    }

    @Test
    void updateItem() throws Exception {
        itemDto.setDescription("Updated Item Description");
        when(itemService.updateItem(anyLong(),anyLong(), any(ItemUpdateDto.class)))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));

        assertEquals("Updated Item Description", itemDto.getDescription());
        verify(itemService).updateItem(anyLong(), anyLong(), any(ItemUpdateDto.class));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService).getItemById(anyLong(), anyLong());
    }

    @Test
    void getUserItems() throws Exception {
        when(itemService.getAllItemsByOwnerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));

        verify(itemService).getAllItemsByOwnerId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getUserItemsWithIncorrectPageParameters() throws Exception {
        int invalidPage = -1;
        int invalidSize = -10;

        when(itemService.getAllItemsByOwnerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .param("page", String.valueOf(invalidPage))
                        .param("size", String.valueOf(invalidSize))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).getAllItemsByOwnerId(anyLong(), eq(invalidPage), eq(invalidSize));
    }

    @Test
    void searchItem() throws Exception {
        ItemDto dtoForSearch = ItemDto.builder().id(2L).name("Щетка для обуви")
                .description("Хорошо чистит").available(true)
                .build();
        when(itemService.searchItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(dtoForSearch));

        mockMvc.perform(get("/items/search")
                        .param("text", "щетка")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dtoForSearch.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(dtoForSearch.getName())))
                .andExpect(jsonPath("$[0].description", is(dtoForSearch.getDescription())))
                .andExpect(jsonPath("$[0].available", is(dtoForSearch.getAvailable())));

        verify(itemService).searchItems(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void searchItemWithIncorrectParameter() throws Exception {
        ItemDto dtoForSearch = ItemDto.builder().id(2L).name("Щетка для обуви")
                .description("Хорошо чистит").available(true)
                .build();
        when(itemService.searchItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(dtoForSearch));

        mockMvc.perform(get("/items/search")
                        .param("text", "")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(itemService, never()).searchItems(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    void searchItemWithIncorrectPageParameters() throws Exception {
        int invalidPage = -1;
        int invalidSize = -10;

        ItemDto dtoForSearch = ItemDto.builder().id(2L).name("Щетка для обуви")
                .description("Хорошо чистит").available(true)
                .build();
        when(itemService.searchItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(dtoForSearch));

        mockMvc.perform(get("/items/search")
                        .param("text", "щетка")
                        .param("page", String.valueOf(invalidPage))
                        .param("size", String.valueOf(invalidSize)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).searchItems(anyLong(), anyString(), eq(invalidPage), eq(invalidSize));
    }

    @Test
    void createItemComment() throws Exception {
        CommentShortDto shortDto = CommentShortDto.builder().id(1L).text("test comment").itemId(1L)
                .authorName("authorName").created(LocalDateTime.now()).build();
        CommentDto commentDto = CommentDto.builder().id(1L).text("test comment").item(itemDto)
                .authorName("authorName").created(shortDto.getCreated()).build();

        when(commentService.addNewComment(any(CommentShortDto.class), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(shortDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(notNullValue())));

        verify(commentService).addNewComment(any(CommentShortDto.class), anyLong(), anyLong());
    }
}