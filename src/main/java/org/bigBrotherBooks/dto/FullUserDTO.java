package org.bigBrotherBooks.dto;

import java.util.List;

public class FullUserDTO {
    private UserDTO userDTO;
    private List<BookDTO> favoriteBooks;
    private List<AuthorDTO> favoriteAuthors;
    private List<UserDTO> following;
    private List<UserDTO> followedBy;
    private List<RentRequestDTO> rentRequests;
    private List<ReviewDTO> reviews;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<BookDTO> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<BookDTO> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public List<AuthorDTO> getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(List<AuthorDTO> favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

    public List<UserDTO> getFollowing() {
        return following;
    }

    public void setFollowing(List<UserDTO> following) {
        this.following = following;
    }

    public List<UserDTO> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<UserDTO> followedBy) {
        this.followedBy = followedBy;
    }

    public List<RentRequestDTO> getRentRequests() {
        return rentRequests;
    }

    public void setRentRequests(List<RentRequestDTO> rentRequests) {
        this.rentRequests = rentRequests;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "FullUserDTO{" +
                "userDTO=" + userDTO +
                ", favoriteBooks=" + favoriteBooks +
                ", favoriteAuthors=" + favoriteAuthors +
                ", following=" + following +
                ", followedBy=" + followedBy +
                ", rentRequests=" + rentRequests +
                ", reviews=" + reviews +
                '}';
    }
}
