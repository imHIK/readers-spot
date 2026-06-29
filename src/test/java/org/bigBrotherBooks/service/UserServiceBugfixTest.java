package org.bigBrotherBooks.service;

import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.repository.AuthorRepository;
import org.bigBrotherBooks.repository.BookRepository;
import org.bigBrotherBooks.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the null-handling fixes in UserService. Repositories are
 * replaced with lightweight in-memory fakes (no database / CDI required).
 */
class UserServiceBugfixTest {

    private UserService newServiceWith(Map<String, User> users) {
        UserRepository userRepo = new UserRepository() {
            @Override
            public User findById(String id) {
                return users.get(id);
            }
        };
        BookService bookService = new BookService(new BookRepository());
        AuthorService authorService = new AuthorService(new AuthorRepository(), bookService);
        return new UserService(userRepo, bookService, authorService);
    }

    @Test
    void modifyFavoriteBook_unknownUser_returnsFalseInsteadOfNpe() {
        UserService service = newServiceWith(new HashMap<>());
        // The 'remove' branch used to dereference a null user before the null-check.
        assertFalse(service.modifyFavoriteBook("ghost", 1, false));
        assertFalse(service.modifyFavoriteBook("ghost", 1, true));
    }

    @Test
    void modifyFavoriteAuthor_unknownUser_returnsFalseInsteadOfNpe() {
        UserService service = newServiceWith(new HashMap<>());
        assertFalse(service.modifyFavoriteAuthor("ghost", 1, false));
        assertFalse(service.modifyFavoriteAuthor("ghost", 1, true));
    }

    @Test
    void followUser_rejectsSelfFollow() {
        Map<String, User> users = new HashMap<>();
        User alice = new User();
        alice.setUserName("alice");
        users.put("alice", alice);
        UserService service = newServiceWith(users);
        assertFalse(service.followUser("alice", "alice", true));
    }

    @Test
    void followUser_unknownUser_returnsFalse() {
        UserService service = newServiceWith(new HashMap<>());
        assertFalse(service.followUser("ghost", "someone", true));
    }

    @Test
    void getReviews_unknownUser_returnsNull() {
        UserService service = newServiceWith(new HashMap<>());
        assertNull(service.getReviews("ghost"));
    }
}
