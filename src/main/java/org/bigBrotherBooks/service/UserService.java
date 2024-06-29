package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.configModels.CustomMap;
import org.bigBrotherBooks.dto.*;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.User;

import java.util.List;


@Singleton
public class UserService {

    private final UserRepository userRepo;
    private final BookService bookService;
    private final AuthorService authorService;

    @Inject
    public UserService(UserRepository userRepo, BookService bookService, AuthorService authorService) {
        this.userRepo = userRepo;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    public User getUserById(String userName) {
        return userRepo.findById(userName);
    }

    public UserDTO getUserDTO(String userName) {
        User user = getUserById(userName);
        return mapToUserDTO(user);
    }

    public void saveUser(UserDTO userDTO) {
        User user = new User();
        mapToUser(userDTO, user);
        userRepo.persist(user);
    }

    public boolean deleteUser(String userName) {
        User user = getUserById(userName);
        if (user == null) {
            return false;
        }
        userRepo.deleteById(userName);
        return true;
    }

    @Transactional
    public boolean updateUser(UserDTO userDTO) {
        User user = getUserById(userDTO.getUserName());
        if (user == null) {
            return false;
        }
        mapToUser(userDTO, user);   // not mapping the relational fields, need to think here
        return true;
    }

    public List<User> getUsers(List<String> userNames) {
        return userRepo.findBulk(userNames);
    }

    public List<UserDTO> getUserDTOs(List<String> userNames) {
        List<User> users = getUsers(userNames);
        return users.stream().map(UserService::mapToUserDTO).toList();
    }

    public List<User> getAllUsers() {
        return userRepo.listAll();
    }

    public List<UserDTO> getAllUserDTOs() {
        List<User> users = getAllUsers();
        return users.stream().map(UserService::mapToUserDTO).toList();
    }

    @Transactional
    public CustomMap getFullUser(String userName) {
        User user = getUserById(userName);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = mapToUserDTO(user);
        List<BookDTO> bookDTOs = user.getFavoriteBooks().stream().map(BookService::mapToBookDTO).toList();
        List<AuthorDTO> authorDTOs = user.getFavoriteAuthors().stream().map(AuthorService::mapToAuthorDTO).toList();
        return CustomMap.of(userDTO, bookDTOs, authorDTOs);
    }

    @Transactional
    public boolean updateProfile(String username, UserProfileUpdateDTO userProfileUpdateDTO) {
        User user = getUserById(username);
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
            book = bookService.getBookById(bookId);
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
    public List<ReviewDTO> getReviews(String userName) {
        User user = getUserById(userName);
        return user.getReviews().stream().map(ReviewService::mapToReviewDTO).toList();
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

    public static UserDTO mapToUserDTO(User user) {
        return new UserDTO(user.getUserName(), user.getPassword(), user.getName(), user.getEmail(), user.getPhone(), user.getAddress(), user.isAdmin(), user.isDeleted());
    }

    private void mapToUser(UserDTO userDTO, User user) {
        user.setUserName(userDTO.getUserName());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setAdmin(userDTO.isAdmin());
        user.setDeleted(userDTO.isDeleted());
    }

}
