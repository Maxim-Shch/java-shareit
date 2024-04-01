package ru.practicum.shareit_gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit_gateway.booking.dto.BookingDtoIn;
import ru.practicum.shareit_gateway.booking.dto.StateOfBookingRequest;
import ru.practicum.shareit_gateway.error.BookingStateException;
import ru.practicum.shareit_gateway.validate.BookingTimeValidator;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;
    private final BookingTimeValidator bookingTimeValidator;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid BookingDtoIn bookingDtoIn) {
        bookingTimeValidator.validateBookingTime(bookingDtoIn.getStart(), bookingDtoIn.getEnd());
        log.info("Пользователь с id={} создал бронирование на вещь с id={}", userId, bookingDtoIn.getItemId());
        return bookingClient.addBooking(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        if (approved) {
            log.info("Пользователь с id={} подтвердил бронирование с id={}", userId, bookingId);
        } else {
            log.info("Пользователь с id={} отклонил бронирование с id={}", userId, bookingId);
        }
        return bookingClient.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingInfo(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Получение информации о бронировании: {} ", bookingId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "state", defaultValue = "ALL")String stateParam,
                                                  @RequestParam(value = "from", required = false, defaultValue = "0")
                                                  final Integer from,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10")
                                                  final Integer size) {
        if (from < 0 || size < 0) {
            throw new IllegalArgumentException("Значение from и size не могут быть меньше 0");
        }

        StateOfBookingRequest state = StateOfBookingRequest.from(stateParam)
                .orElseThrow(() -> new BookingStateException("Unknown state: " + stateParam));
        log.info("Получены бронирования со статусом {} пользователя с id={}", state, userId);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(value = "state", defaultValue = "ALL")
                                                          String stateParam,
                                                          @RequestParam(value = "from", required = false,
                                                                  defaultValue = "0")
                                                          final Integer from,
                                                          @RequestParam(value = "size", required = false,
                                                                  defaultValue = "10")
                                                          final Integer size) {
        if (from < 0 || size < 0) {
            throw new IllegalArgumentException("Значение from и size не могут быть меньше 0");
        }

        StateOfBookingRequest state = StateOfBookingRequest.from(stateParam)
                .orElseThrow(() -> new BookingStateException("Unknown state: " + stateParam));
        log.info("Получены бронирования со статусом {} вещей пользователя с id={}", state, userId);
        return bookingClient.getBookingsForUserItems(userId, state, from, size);
    }
}
