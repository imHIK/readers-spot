package org.bigBrotherBooks.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @Column(name = "user_name")
    private String userName;

    @Column
    private String password;  // password must be encrypted later

    @Column
    private String name;

    @Column
    private String email;  // email must be validated later

    @Column
    private String phone;  // phone must be validated later

    @Column
    private String address;

    @Column(name = "is_admin")
    private boolean isAdmin;

    private Set<Integer> favoriteBooks;

    private Set<Integer> favoriteAuthors;

    private Set<String> followingUsers;

    public User() {
        favoriteBooks = new HashSet<>();
        favoriteAuthors = new HashSet<>();
        followingUsers = new HashSet<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<Integer> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(Set<Integer> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public void addFavoriteBook(int bookId) {
        favoriteBooks.add(bookId);
    }

    public void removeFavoriteBook(int bookId) {
        favoriteBooks.remove(bookId);
    }

    public Set<Integer> getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(Set<Integer> favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

    public void addFavoriteAuthor(int authorId) {
        favoriteAuthors.add(authorId);
    }

    public void removeFavoriteAuthor(int authorId) {
        favoriteAuthors.remove(authorId);
    }

    public Set<String> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(Set<String> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public void followUser(String username) {
        followingUsers.add(username);
    }

    public void unfollowUser(String username) {
        followingUsers.remove(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "usernName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isAdmin=" + isAdmin +
                ", favoriteBooks=" + favoriteBooks +
                ", favoriteAuthors=" + favoriteAuthors +
                ", followingUsers=" + followingUsers +
                '}';
    }
}
