package org.bigBrotherBooks.service;

import io.smallrye.mutiny.tuples.Tuple3;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.dto.UserDTO;
import org.bigBrotherBooks.dto.UserProfileUpdateDTO;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.User;

import java.util.List;

@Singleton
public class UserService {

    private UserRepository userRepo;
    private BookService bookService;
    private AuthorService authorService;

    @Inject
    public UserService(UserRepository userRepo, BookService bookService, AuthorService authorService) {
        this.userRepo = userRepo;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Transactional
    public User getUserById(String userName) {
        return userRepo.find("userName", userName).firstResult();
    }

    @Transactional
    public UserDTO getUserDTO(String userName) {
        User user = getUserById(userName);
        return mapToUserDTO(user);
    }

    @Transactional
    public void saveUser(User user) {
        userRepo.persist(user);
    }

    @Transactional
    public boolean deleteUser(String userName) {
        User user = getUserById(userName);
        if (user == null) {
            return false;
        }
        userRepo.delete(user);
        return true;
    }

    @Transactional
    public boolean updateUser(User user) {
        User existingUser = getUserById(user.getUserName());
        if (existingUser == null) {
            return false;
        }
        mapUserDetails(user, existingUser);   // not mapping the relational fields, need to think here
        return true;
    }

    public List<User> getUsers(List<String> userNames) {
        return userRepo.findBulk(userNames);
    }

    public List<UserDTO> getUserDTOs(List<String> userNames) {
        List<User> users = getUsers(userNames);
        return users.stream().map(this::mapToUserDTO).toList();
    }

    public List<User> getAllUsers() {
        return userRepo.listAll();
    }

    public List<UserDTO> getAllUserDTOs() {
        List<User> users = getAllUsers();
        return users.stream().map(this::mapToUserDTO).toList();
    }

    @Transactional
    public Object getFullUser(String userName) {
        User user = getUserById(userName);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = mapToUserDTO(user);
        List<BookDTO> bookDTOs = user.getFavoriteBooks().stream().map(b -> bookService.mapToBookDTO(b)).toList();
        List<AuthorDTO> authorDTOs = user.getFavoriteAuthors().stream().map(a -> authorService.mapToAuthorDTO(a)).toList();
        return Tuple3.of(userDTO, bookDTOs, authorDTOs);
    }

    @Transactional
    public boolean updateProfile(UserProfileUpdateDTO userProfileUpdateDTO) {
        User user = getUserById(userProfileUpdateDTO.getUsername());
        if (user == null) {
            return false;
        }
        user.setName(userProfileUpdateDTO.getName());
        user.setEmail(userProfileUpdateDTO.getEmail());
        user.setPhone(userProfileUpdateDTO.getPhone());
        user.setAddress(userProfileUpdateDTO.getAddress());
        return true;
    }

    @Transactional
    public boolean modifyFavoriteBook(String userName, int bookId, boolean isAdded) {
        User user = getUserById(userName);
        Book book;
        if (isAdded) {
            book = bookService.getBook(bookId);
        } else {
            book = user.getFavoriteBooks().stream().filter(b -> b.getBookId() == bookId).findFirst().orElse(null);
        }
        if (user == null || book == null) {
            return false;
        }
        if (isAdded) user.addFavoriteBook(book);       // also adds user in book-fans
        else user.removeFavoriteBook(book);           // also removes user from book-fans
        return true;
    }

    @Transactional
    public boolean modifyFavoriteAuthor(String userName, int authorId, boolean isAdded) {
        User user = getUserById(userName);
        Author author;
        if (isAdded) {
            author = authorService.getAuthor(authorId);
        } else {
            author = user.getFavoriteAuthors().stream().filter(a -> a.getAuthorId() == authorId).findFirst().orElse(null);
        }

        if (user == null || author == null) {
            return false;
        }
        if (isAdded) user.addFavoriteAuthor(author);  // also adds user in author-fans
        else user.removeFavoriteAuthor(author);      // also removes user from author-fans
        return true;
    }

    @Transactional
    public boolean followUser(String userName, String followingUserName, boolean isStarting) {
        User user = getUserById(userName);
        User followingUser;
        if (isStarting) {
            followingUser = getUserById(followingUserName);
        } else {
            followingUser = user.getFollowing().stream().filter(u -> u.getUserName().equals(followingUserName)).findFirst().orElse(null);
        }
        if (user == null || followingUser == null) {
            return false;
        }
        if (isStarting) user.addFollowing(followingUser);       // also adds user in following-followers
        else user.removeFollowing(followingUser);              // also removes user from following-followers
        return true;
    }

    public User getDummyUser() {
        User user = new User();
        user.setUserName("dummy_user");
        user.setPassword("password");
        user.setName("Dummy User");
        user.setEmail("abc@example.com");
        user.setPhone("1234567890");
        return user;
    }

    private void mapUserDetails(User user, User existingUser) {
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        existingUser.setDeleted(user.isDeleted());
        existingUser.setAdmin(user.isAdmin());
//        existingUser.setFavoriteBooks(user.getFavoriteBooks());
//        existingUser.setFavoriteAuthors(user.getFavoriteAuthors());
//        existingUser.setFollowing(user.getFollowing());
//        existingUser.setFollowedBy(user.getFollowedBy());
//        existingUser.setRentRequests(user.getRentRequests());
    }

    private UserDTO mapToUserDTO(User user) {
        return new UserDTO(user.getUserName(), user.getPassword(), user.getName(), user.getEmail(), user.getPhone(), user.getAddress(), user.isAdmin(), user.isDeleted());
    }


}
