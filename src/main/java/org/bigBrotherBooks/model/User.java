package org.bigBrotherBooks.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Fields
 * userName: String
 * password: String
 * name: String
 * email: String
 * phone: String
 * address: String
 * isAdmin: boolean  (not reflecting in db)
 * isDeleted: boolean
 * favoriteBooks: Set<Book>  -> many to many
 * favoriteAuthors: Set<Author> -> many to many
 * following: Set<User>  -> many to many
 * followedBy: Set<User>  -> many to many
 * rentRequests: List<Integer>   ->  later change to List<RentRequest>
 */

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

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToMany
    @JoinTable(
            name = "favorite_books",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> favoriteBooks;

    @ManyToMany
    @JoinTable(
            name = "favorite_authors",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> favoriteAuthors;

    @ManyToMany
    @JoinTable(
            name = "following",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "following_user_name")
    )
    private Set<User> following;

    @ManyToMany(mappedBy = "following")
    private Set<User> followedBy;


    @OneToMany(mappedBy = "user")
    private Set<RentRequest> rentRequests;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    public User() {
        favoriteBooks = new HashSet<>();
        favoriteAuthors = new HashSet<>();
        following = new HashSet<>();
        followedBy = new HashSet<>();
        reviews = new HashSet<>();
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Set<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(Set<Book> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public void addFavoriteBook(Book book) {
        favoriteBooks.add(book);
        book.getFans().add(this);
    }

    public void removeFavoriteBook(Book book) {
        favoriteBooks.remove(book);
        book.getFans().remove(this);
    }

    public Set<Author> getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(Set<Author> favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

    public void addFavoriteAuthor(Author author) {
        favoriteAuthors.add(author);
        author.getFans().add(this);
    }

    public void removeFavoriteAuthor(Author author) {
        favoriteAuthors.remove(author);
        author.getFans().remove(this);
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public void addFollowing(User userToFollow) {
        following.add(userToFollow);
        userToFollow.getFollowedBy().add(this);
    }

    public void removeFollowing(User userToUnfollow) {
        following.remove(userToUnfollow);
        userToUnfollow.getFollowedBy().remove(this);
    }

    public Set<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(Set<User> followedBy) {
        this.followedBy = followedBy;
    }

    public void addFollowedBy(User user) {
        followedBy.add(user);
        user.getFollowing().add(this);
    }

    public void removeFollowedBy(User user) {
        followedBy.remove(user);
        user.getFollowing().remove(this);
    }

    public Set<RentRequest> getRentRequests() {
        return rentRequests;
    }

    public void setRentRequests(Set<RentRequest> rentRequests) {
        this.rentRequests = rentRequests;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setUser(this);
    }

    public void removeReview(Review review) {
        if (reviews.contains(review)) {
            reviews.remove(review);
            review.setUser(null);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isAdmin=" + isAdmin +
                ", isDeleted=" + isDeleted +
                ", favoriteBooks=" + favoriteBooks +
                ", favoriteAuthors=" + favoriteAuthors +
                ", following=" + following +
                ", followedBy=" + followedBy +
                ", rentRequests=" + rentRequests +
                '}';
    }

}
