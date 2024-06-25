package org.bigBrotherBooks.model;

import jakarta.persistence.*;
import org.bigBrotherBooks.configModels.Condition;
import org.bigBrotherBooks.configModels.Status;

@Entity
public class RentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reqId;

    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column
    private Status status;

    @Column
    private Long requestTime;

    @Column
    private Long issueTime;

    @Column
    private Long returnTime;

    @Column
    private Condition issueCondition;

    @Column
    private Condition returnCondition;

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
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

    public Condition getIssueCondition() {
        return issueCondition;
    }

    public void setIssueCondition(Condition issueCondition) {
        this.issueCondition = issueCondition;
    }

    public Condition getReturnCondition() {
        return returnCondition;
    }

    public void setReturnCondition(Condition returnCondition) {
        this.returnCondition = returnCondition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return "RentRequest{" +
                "reqId=" + reqId +
                ", user=" + user +
                ", book=" + book +
                ", warehouse=" + warehouse +
                ", status=" + status +
                ", requestTime=" + requestTime +
                ", issueTime=" + issueTime +
                ", returnTime=" + returnTime +
                ", issueCondition=" + issueCondition +
                ", returnCondition=" + returnCondition +
                '}';
    }
}
