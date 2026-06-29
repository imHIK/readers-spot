package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.configModels.BookCondition;
import org.bigBrotherBooks.configModels.Status;
import org.bigBrotherBooks.dto.RentRequestDTO;
import org.bigBrotherBooks.dto.StockDTO;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.RentRequest;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.model.Warehouse;
import org.bigBrotherBooks.constants.GlobalConstants;
import org.bigBrotherBooks.repository.RentRequestRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class RequestService {

    private final RentRequestRepository rentRequestRepository;
    private final UserService userService;
    private final WarehouseService warehouseService;
    private final BookService bookService;

    @Inject
    public RequestService(RentRequestRepository rentRequestRepository, UserService userService, WarehouseService warehouseService, BookService bookService) {
        this.rentRequestRepository = rentRequestRepository;
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.bookService = bookService;
    }

    public RentRequest getRentRequestById(int requestId) {
        return rentRequestRepository.findById((long) requestId);
    }

    public RentRequestDTO getRentRequestDTO(int requestId) {
        RentRequest rentRequest = getRentRequestById(requestId);
        return mapToRentRequestDTO(rentRequest);
    }

    @Transactional
    public boolean makeRequest(String userName, RentRequestDTO rentRequestDTO) {
        RentRequest rentRequest = new RentRequest();
        mapToRentRequest(rentRequestDTO, rentRequest);
        User user = userService.getUserById(userName);
        Book book = bookService.getBookById(rentRequestDTO.getBookId());
        Warehouse warehouse = warehouseService.getWarehouseById(rentRequestDTO.getWarehouseId());
        if (user == null || book == null || warehouse == null) {
            return false;
        }
        rentRequest.setBook(book);
        rentRequest.setWarehouse(warehouse);
        if (rentRequest.getRequestTime() == null) {
            rentRequest.setRequestTime(System.currentTimeMillis());
        }
        if (rentRequest.getStatus() == null) {
            rentRequest.setStatus(Status.REQUESTED);
        }
        user.addRentRequest(rentRequest);
        warehouse.addRentRequest(rentRequest);
        rentRequestRepository.persist(rentRequest);
        return true;
    }

    @Transactional
    public boolean updateRequest(RentRequestDTO rentRequestDTO) {
        RentRequest rentRequest = getRentRequestById(rentRequestDTO.getReqId());
        if (rentRequest == null) {
            return false;
        }
        mapToRentRequest(rentRequestDTO, rentRequest);
        return true;
    }

    public boolean removeRequest(int requestId) {
        RentRequest rentRequest = getRentRequestById(requestId);
        if (rentRequest == null) {
            return false;
        }
        rentRequestRepository.delete(rentRequest);
        return true;
    }

    @Transactional
    public boolean processRequest(int requestId) {
        RentRequest rentRequest = getRentRequestById(requestId);
        if (rentRequest == null) {
            return false;
        }
        if (rentRequest.getStatus() == Status.ISSUED || rentRequest.getStatus() == Status.RETURNED) {
            return false;   // already issued or completed
        }
        int warehouseId = rentRequest.getWarehouse().getWarehouseId();
        int bookId = rentRequest.getBook().getBookId();
        BookCondition condition = rentRequest.getIssueCondition();
        if (condition == null) {
            condition = warehouseService.pickAvailableCondition(warehouseId, bookId);
            if (condition == null) {
                return false;   // no copy available in any condition
            }
            rentRequest.setIssueCondition(condition);
        }
        if (warehouseService.removeWarehouseStock(warehouseId, new StockDTO(bookId, condition, 1))) {
            rentRequest.setIssueTime(System.currentTimeMillis());
            rentRequest.setStatus(Status.ISSUED);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean processReturn(int requestId, BookCondition returnCondition) {
        RentRequest rentRequest = getRentRequestById(requestId);
        if (rentRequest == null) {
            return false;
        }
        if (rentRequest.getStatus() != Status.ISSUED) {
            return false;   // only an issued book can be returned
        }
        int warehouseId = rentRequest.getWarehouse().getWarehouseId();
        int bookId = rentRequest.getBook().getBookId();
        if (returnCondition == null) {
            returnCondition = rentRequest.getIssueCondition();
        }
        if (warehouseService.addWarehouseStock(warehouseId, List.of(new StockDTO(bookId, returnCondition, 1)))) {
            rentRequest.setReturnTime(System.currentTimeMillis());
            rentRequest.setReturnCondition(returnCondition);
            rentRequest.setStatus(Status.RETURNED);
            rentRequest.setPrice(calculateRentPrice(rentRequest));
            return true;
        }
        return false;
    }

    /**
     * Basic rent price: 10% of the book's list price plus a flat late fee per day
     * held beyond the rent deadline. Returns 0 when prices/times are unavailable.
     */
    private long calculateRentPrice(RentRequest rentRequest) {
        Double bookPrice = rentRequest.getBook() != null ? rentRequest.getBook().getPrice() : null;
        long base = bookPrice == null ? 0L : Math.round(bookPrice * 0.10);
        long lateFee = 0L;
        Long issueTime = rentRequest.getIssueTime();
        Long returnTime = rentRequest.getReturnTime();
        if (issueTime != null && returnTime != null) {
            long held = returnTime - issueTime;
            if (held > GlobalConstants.RENT_DEADLINE) {
                long overdueDays = (held - GlobalConstants.RENT_DEADLINE) / TimeUnit.DAYS.toMillis(1);
                lateFee = overdueDays * 5L;   // flat fee of 5 per overdue day
            }
        }
        return base + lateFee;
    }

    private void mapToRentRequest(RentRequestDTO rentRequestDTO, RentRequest rentRequest) {
        rentRequest.setStatus(rentRequestDTO.getStatus());
        rentRequest.setRequestTime(rentRequestDTO.getRequestTime());
        rentRequest.setIssueTime(rentRequestDTO.getIssueTime());
        rentRequest.setReturnTime(rentRequestDTO.getReturnTime());
        rentRequest.setIssueCondition(rentRequestDTO.getIssueCondition());
        rentRequest.setReturnCondition(rentRequestDTO.getReturnCondition());
        rentRequest.setPrice(rentRequestDTO.getPrice());
    }

    private RentRequestDTO mapToRentRequestDTO(RentRequest rentRequest) {
        RentRequestDTO rentRequestDTO = new RentRequestDTO();
        rentRequestDTO.setReqId(rentRequest.getReqId());
        rentRequestDTO.setUserName(rentRequest.getUser().getUserName());
        rentRequestDTO.setWarehouseId(rentRequest.getWarehouse().getWarehouseId());
        rentRequestDTO.setBookId(rentRequest.getBook().getBookId());
        rentRequestDTO.setStatus(rentRequest.getStatus());
        rentRequestDTO.setRequestTime(rentRequest.getRequestTime());
        rentRequestDTO.setIssueTime(rentRequest.getIssueTime());
        rentRequestDTO.setReturnTime(rentRequest.getReturnTime());
        rentRequestDTO.setIssueCondition(rentRequest.getIssueCondition());
        rentRequestDTO.setReturnCondition(rentRequest.getReturnCondition());
        rentRequestDTO.setPrice(rentRequest.getPrice());
        return rentRequestDTO;
    }


}
