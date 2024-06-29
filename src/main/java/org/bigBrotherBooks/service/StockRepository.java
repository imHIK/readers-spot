package org.bigBrotherBooks.service;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Stock;

@Transactional
@Singleton
public class StockRepository implements PanacheRepositoryBase<Stock, Stock.StockId> {
}
