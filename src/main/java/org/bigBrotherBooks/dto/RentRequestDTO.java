package org.bigBrotherBooks.dto;

import org.bigBrotherBooks.configModels.BookCondition;
import org.bigBrotherBooks.configModels.Status;

public class RentRequestDTO {

    private int reqId;
    private String userName;
    private int bookId;
    private int warehouseId;
    private Status status;
    private Long requestTime;
    private Long issueTime;
    private Long returnTime;
    private BookCondition issueCondition;
    private BookCondition returnCondition;
    private Long price;

    public RentRequestDTO() {
    }

    public RentRequestDTO(int reqId, String userName, int bookId, int warehouseId, Status status, Long requestTime, Long issueTime, Long returnTime, BookCondition issueCondition, BookCondition returnCondition, Long price) {
        this.reqId = reqId;
        this.userName = userName;
        this.bookId = bookId;
        this.warehouseId = warehouseId;
        this.status = status;
        this.requestTime = requestTime;
        this.issueTime = issueTime;
        this.returnTime = returnTime;
        this.issueCondition = issueCondition;
        this.returnCondition = returnCondition;
        this.price = price;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public Long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Long issueTime) {
        this.issueTime = issueTime;
    }

    public Long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Long returnTime) {
        this.returnTime = returnTime;
    }

    public BookCondition getIssueCondition() {
        return issueCondition;
    }

    public void setIssueCondition(BookCondition issueCondition) {
        this.issueCondition = issueCondition;
    }

    public BookCondition getReturnCondition() {
        return returnCondition;
    }

    public void setReturnCondition(BookCondition returnCondition) {
        this.returnCondition = returnCondition;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "RentRequestDTO{" +
                "reqId=" + reqId +
                ", userName='" + userName + '\'' +
                ", bookId=" + bookId +
                ", warehouseId=" + warehouseId +
                ", status=" + status +
                ", requestTime=" + requestTime +
                ", issueTime=" + issueTime +
                ", returnTime=" + returnTime +
                ", issueCondition=" + issueCondition +
                ", returnCondition=" + returnCondition +
                ", price=" + price +
                '}';
    }
}
