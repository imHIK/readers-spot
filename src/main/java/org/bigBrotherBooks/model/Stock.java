package org.bigBrotherBooks.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Stock {

    @EmbeddedId
    StockId stockId;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    private int quantity;

    public StockId getStockId() {
        return stockId;
    }

    public void setStockId(StockId stockId) {
        this.stockId = stockId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Embeddable
    public static class StockId {
        int warehouseId;
        int bookId;

        public StockId() {
        }

        public StockId(int warehouseId, int bookId) {
            this.warehouseId = warehouseId;
            this.bookId = bookId;
        }

        public int getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(int warehouseId) {
            this.warehouseId = warehouseId;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StockId stockId = (StockId) o;
            return warehouseId == stockId.warehouseId && bookId == stockId.bookId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(warehouseId, bookId);
        }
    }
}
