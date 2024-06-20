package org.bigBrotherBooks.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.model.Book;

import java.util.List;

@Singleton
public class BookRepository implements PanacheRepositoryBase<Book, Long> {
    public List<Book> findBulk(List<Integer> bookIds){
        return list("bookId in ?1", bookIds);
    }
}
