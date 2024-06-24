package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.UserProfileUpdateDTO;
import org.bigBrotherBooks.model.User;

@Singleton
public class UserService {

    private UserRepository userRepo;

    @Inject
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserById(String userName) {
        return userRepo.find("userName", userName).firstResult();
    }

    @Transactional
    public void saveUser(User user) {
        userRepo.persist(user);
    }

    @Transactional
    public boolean deleteUser(String userName) {
        User user = getUserById(userName);
        if(user == null) {
            return false;
        }
        userRepo.delete(user);
        return true;
    }

    @Transactional
    public User updateUser(User user) {
        User existingUser = getUserById(user.getUserName());
        if (existingUser == null) {
            return null;
        }
        mapUserDetails(user, existingUser);   // transactional operation, so changes in the table without update call
        return existingUser;
    }

    public boolean updateProfile(UserProfileUpdateDTO userProfileUpdateDTO) {
        User user = getUserById(userProfileUpdateDTO.getUsername());
        if(user == null) {
            return false;
        }
        user.setName(userProfileUpdateDTO.getName());
        user.setEmail(userProfileUpdateDTO.getEmail());
        user.setPhone(userProfileUpdateDTO.getPhone());
        user.setAddress(userProfileUpdateDTO.getAddress());
        saveUser(user);
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
        existingUser.setFavoriteBooks(user.getFavoriteBooks());
        existingUser.setFavoriteAuthors(user.getFavoriteAuthors());
        existingUser.setFollowing(user.getFollowing());
        existingUser.setFollowedBy(user.getFollowedBy());
        existingUser.setRentRequests(user.getRentRequests());
    }


}
