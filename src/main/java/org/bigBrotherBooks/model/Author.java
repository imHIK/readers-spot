package org.bigBrotherBooks.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a unique author entry.
 * Fields
 * authorId: int
 * name: String
 * about: String
 * books: Set<Book> -> one to many
 * fans: Set<User> -> many to many
 */


@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private int authorId;

    @Column(name = "name")
    private String name;

    @Column(name = "about")
    private String about;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books;

    @ManyToMany(mappedBy = "favoriteAuthors")
    private Set<User> fans;

    public Author() {
        books = new HashSet<>();
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Set<User> getFans() {
        return fans;
    }

    public void setFans(Set<User> fans) {
        this.fans = fans;
    }

    public void addFan(User user) {
        fans.add(user);
        user.getFavoriteAuthors().add(this);
    }

    public void removeFan(User user) {
        fans.remove(user);
        user.getFavoriteAuthors().remove(this);
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", name='" + name + '\'' +
                ", about='" + about + '\'' +
                ", books=" + books +
                ", fans=" + fans +
                '}';
    }
}
