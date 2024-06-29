package org.bigBrotherBooks.dto;

import jakarta.persistence.Enumerated;
import org.bigBrotherBooks.configModels.BookCondition;

public class StockDTO {

    private int bookId;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private BookCondition bookCondition;
    private int quantity;

    public StockDTO() {
    }

    public StockDTO(int bookId, BookCondition bookCondition, int quantity) {
        this.bookId = bookId;
        this.bookCondition = bookCondition;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "stockDTO{" +
                ", bookId=" + bookId +
                ", condition=" + bookCondition +
                ", quantity=" + quantity +
                '}';
    }
}
