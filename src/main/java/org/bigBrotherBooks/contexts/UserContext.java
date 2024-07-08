package org.bigBrotherBooks.contexts;

import jakarta.enterprise.context.RequestScoped;

import java.util.List;

public class UserContext {
    private String username;
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @RequestScoped
    public static class Provider {
        private UserContext userContext;

        public UserContext getUserContext() {
            return userContext;
        }

        public void setUserContext(UserContext userContext) {
            this.userContext = userContext;
        }
    }
}
