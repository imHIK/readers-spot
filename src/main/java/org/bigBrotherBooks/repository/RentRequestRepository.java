package org.bigBrotherBooks.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.RentRequest;

@Transactional
@Singleton
public class RentRequestRepository implements PanacheRepositoryBase<RentRequest, Long> {
}

