package org.bigBrotherBooks.dto;

public class AuthorDTO {

    private int authorId;
    private String name;
    private String about;

    public AuthorDTO() {
    }

    public AuthorDTO(int authorId, String name, String about) {
        this.authorId = authorId;
        this.name = name;
        this.about = about;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "authorId=" + authorId +
                ", name='" + name + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
