package org.bigBrotherBooks.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Author;

@Transactional
@Singleton
public class AuthorRepository implements PanacheRepositoryBase<Author, Long> {

}
