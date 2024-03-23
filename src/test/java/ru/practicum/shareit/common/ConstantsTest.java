package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstantsTest {

    @Test
    public void testXSharerUserIdConstant() {
        assertEquals("X-Sharer-User-Id", Constants.X_SHARER_USER_ID);
    }
}
