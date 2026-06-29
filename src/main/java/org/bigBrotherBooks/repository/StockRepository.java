package org.bigBrotherBooks.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Stock;

import java.util.List;

@Transactional
@Singleton
public class StockRepository implements PanacheRepositoryBase<Stock, Stock.StockId> {

    public List<Stock> findAvailable(int warehouseId, int bookId) {
        return list("stockId.warehouseId = ?1 and stockId.bookId = ?2 and quantity > 0", warehouseId, bookId);
    }
}
