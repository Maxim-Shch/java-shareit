package ru.practicum.shareit.exceptions;

public class CommentRequestException extends RuntimeException {
    public CommentRequestException(String message) {
        super(message);
    }
}
