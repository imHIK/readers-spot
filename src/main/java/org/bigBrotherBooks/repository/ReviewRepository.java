package org.bigBrotherBooks.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Review;

@Transactional
@Singleton
public class ReviewRepository implements PanacheRepositoryBase<Review, Review.ReviewId> {

}
