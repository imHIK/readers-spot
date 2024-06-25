package org.bigBrotherBooks.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.User;

import java.util.List;

@Transactional
@Singleton
public class UserRepository implements PanacheRepositoryBase<User, Long> {
    public List<User> findBulk(List<String> userNames) {
        return list("userName in ?1", userNames);
    }
}
