package org.bigBrotherBooks;

import org.bigBrotherBooks.dto.UserDTO;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void mapToUserDTO_doesNotExposePasswordHash() {
        User user = new User();
        user.setUserName("alice");
        user.setPassword("$2a$12$hashedSecretValue");
        user.setName("Alice");
        user.setRoles(Set.of("USER"));

        UserDTO dto = UserService.mapToUserDTO(user);

        assertNotNull(dto);
        assertEquals("alice", dto.getUserName());
        assertNull(dto.getPassword());
    }
}
