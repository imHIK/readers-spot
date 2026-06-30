package org.bigBrotherBooks.service;

import org.bigBrotherBooks.configModels.BookCondition;
import org.bigBrotherBooks.dto.StockDTO;
import org.bigBrotherBooks.model.Stock;
import org.bigBrotherBooks.repository.StockRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseServiceTest {

    private WarehouseService serviceWithStock(Stock stock) {
        StockRepository stockRepo = new StockRepository() {
            @Override
            public Stock findById(Stock.StockId id) {
                return stock;
            }
        };
        return new WarehouseService(null, null, stockRepo);
    }

    @Test
    void removeWarehouseStock_insufficientQuantity_returnsFalse() {
        Stock stock = new Stock(new Stock.StockId(1, 1, BookCondition.NEW), 1);
        WarehouseService service = serviceWithStock(stock);
        assertFalse(service.removeWarehouseStock(1, new StockDTO(1, BookCondition.NEW, 2)));
        assertEquals(1, stock.getQuantity());   // unchanged - no negative stock
    }

    @Test
    void removeWarehouseStock_unknownStock_returnsFalse() {
        WarehouseService service = serviceWithStock(null);
        assertFalse(service.removeWarehouseStock(1, new StockDTO(1, BookCondition.NEW, 1)));
    }

    @Test
    void removeWarehouseStock_nonPositiveQuantity_returnsFalse() {
        Stock stock = new Stock(new Stock.StockId(1, 1, BookCondition.NEW), 5);
        WarehouseService service = serviceWithStock(stock);
        assertFalse(service.removeWarehouseStock(1, new StockDTO(1, BookCondition.NEW, 0)));
        assertEquals(5, stock.getQuantity());
    }

    @Test
    void removeWarehouseStock_sufficientQuantity_decrements() {
        Stock stock = new Stock(new Stock.StockId(1, 1, BookCondition.NEW), 5);
        WarehouseService service = serviceWithStock(stock);
        assertTrue(service.removeWarehouseStock(1, new StockDTO(1, BookCondition.NEW, 2)));
        assertEquals(3, stock.getQuantity());
    }
}
