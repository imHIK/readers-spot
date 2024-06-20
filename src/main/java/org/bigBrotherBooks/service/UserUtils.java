package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.model.User;

@Singleton
public class UserUtils {

    private UserRepository userRepo;

    @Inject
    public UserUtils(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserById(String userName) {
        return userRepo.find("userName", userName).firstResult();
    }

    public void saveUser(User user) {
        userRepo.persist(user);
    }

    public boolean deleteUser(String userName) {
        User user = getUserById(userName);
        if(user == null) {
            return false;
        }
        userRepo.delete(user);
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


}
