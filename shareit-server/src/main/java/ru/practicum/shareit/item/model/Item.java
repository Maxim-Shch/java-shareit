package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //уникальный идентификатор вещи;
    @Column(name = "name")
    private String name; //краткое наименование вещи;
    @Column(name = "description")
    private String description; //развёрнутое описание;
    @Column(name = "is_available")
    private Boolean available; //статус о том, доступна или нет вещь для аренды;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; //владелец вещи;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request; //если вещь была создана по запросу другого пользователя,
    //то в этом поле будет храниться ссылка на соответствующий запрос.
}
