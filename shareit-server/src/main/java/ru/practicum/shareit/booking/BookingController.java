package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@Validated
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut createNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody final BookingDtoIn bookingDtoIn) {
        return bookingService.createBooking(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId,
                                        @RequestParam Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBooking(@PathVariable final Long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);

    }

    @GetMapping
    public Collection<BookingDtoOut> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "ALL")String state,
                                                     @RequestParam(value = "from", required = false,
                                                             defaultValue = "0")
                                                  final Integer from,
                                                     @RequestParam(value = "size", required = false,
                                                             defaultValue = "10")
                                                  final Integer size) {
        return bookingService.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoOut> getBookingsForUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @RequestParam(defaultValue = "ALL")String state,
                                                             @RequestParam(value = "from", required = false,
                                                                     defaultValue = "0")
                                                          final Integer from,
                                                             @RequestParam(value = "size", required = false,
                                                                     defaultValue = "10")
                                                          final Integer size) {
        return bookingService.getBookingsForUserItems(userId, state, from, size);
    }
}
