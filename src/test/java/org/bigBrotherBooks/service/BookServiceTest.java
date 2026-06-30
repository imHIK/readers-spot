package org.bigBrotherBooks.service;

import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.repository.BookRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    @Test
    void getBookDTO_unknownBook_returnsNull() {
        BookRepository repo = new BookRepository() {
            @Override
            public Book findById(Long id) {
                return null;
            }
        };
        BookService service = new BookService(repo);
        assertNull(service.getBookDTO(42));
    }

    @Test
    void getReviews_unknownBook_returnsNull() {
        BookRepository repo = new BookRepository() {
            @Override
            public Book findById(Long id) {
                return null;
            }
        };
        BookService service = new BookService(repo);
        assertNull(service.getReviews(42));
    }

    @Test
    void mapToBookDTO_isNullSafe() {
        assertNull(BookService.mapToBookDTO(null));
    }

    @Test
    void mapToBookDTO_copiesCoreFields() {
        Book book = new Book();
        book.setBookId(7);
        book.setName("Dune");
        book.setPrice(12.5);
        BookDTO dto = BookService.mapToBookDTO(book);
        assertEquals(7, dto.getBookId());
        assertEquals("Dune", dto.getName());
        assertEquals(12.5, dto.getPrice());
    }
}
