package org.bigBrotherBooks.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.model.LoginRequest;
import org.bigBrotherBooks.model.User;

@Singleton
public class AuthService {

    UserService userService;
    TokenService tokenService;

    @Inject
    public AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public String authenticate(LoginRequest loginRequest) {
        User user = userService.getUserById(loginRequest.getUserName());
        if (user == null || !checkPassword(loginRequest.getPassword(), user.getPassword())) {
            return null;
        }
        return tokenService.generateToken(user.getUserName(), user.getRoles());
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return BcryptUtil.matches(password, hashedPassword);
    }

    public static String encryptPassword(String password) {
        return BcryptUtil.bcryptHash(password);
    }

}
