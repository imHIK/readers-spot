package org.bigBrotherBooks.configModels;

import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.dto.BookDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomMapTest {

    @Test
    void of_keysBySimpleClassName() {
        AuthorDTO author = new AuthorDTO(1, "Frank", "bio");
        CustomMap map = CustomMap.of(author);
        assertSame(author, map.get("AuthorDTO"));
    }

    @Test
    void of_keysListsByElementClassName() {
        List<BookDTO> books = List.of(new BookDTO());
        CustomMap map = CustomMap.of(books);
        assertEquals(books, map.get("BookDTO"));
    }

    @Test
    void of_ignoresEmptyCollections() {
        CustomMap map = CustomMap.of(List.of());
        assertTrue(map.getMap().isEmpty());
    }
}
