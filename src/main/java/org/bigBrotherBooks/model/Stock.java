package org.bigBrotherBooks.model;

import jakarta.persistence.*;
import org.bigBrotherBooks.configModels.BookCondition;

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

    @MapsId("bookCondition")
    @Column
    @Enumerated(EnumType.STRING)
    private BookCondition condition;

    @Column
    private int quantity;

    public Stock() {
    }

    public Stock(StockId stockId, int quantity) {
        this.stockId = stockId;
        this.quantity = quantity;
    }

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

    public BookCondition getCondition() {
        return condition;
    }

    public void setCondition(BookCondition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", warehouse=" + warehouse +
                ", book=" + book +
                ", condition=" + condition +
                ", quantity=" + quantity +
                '}';
    }

    @Embeddable
    public static class StockId {
        int warehouseId;
        int bookId;
        @Enumerated(EnumType.STRING)
        BookCondition bookCondition;

        public StockId() {
        }

        public StockId(int warehouseId, int bookId, BookCondition bookCondition) {
            this.warehouseId = warehouseId;
            this.bookId = bookId;
            this.bookCondition = bookCondition;
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

        public BookCondition getCondition() {
            return bookCondition;
        }

        public void setCondition(BookCondition bookCondition) {
            this.bookCondition = bookCondition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StockId stockId = (StockId) o;
            return warehouseId == stockId.warehouseId && bookId == stockId.bookId && bookCondition == stockId.bookCondition;
        }

        @Override
        public int hashCode() {
            return Objects.hash(warehouseId, bookId, bookCondition);
        }
    }
}
