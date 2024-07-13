package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.configModels.BookCondition;
import org.bigBrotherBooks.configModels.Status;
import org.bigBrotherBooks.dto.RentRequestDTO;
import org.bigBrotherBooks.dto.StockDTO;
import org.bigBrotherBooks.model.RentRequest;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.model.Warehouse;
import org.bigBrotherBooks.repository.RentRequestRepository;

import java.util.List;

@Singleton
public class RequestService {

    private final RentRequestRepository rentRequestRepository;
    private final UserService userService;
    private final WarehouseService warehouseService;

    @Inject
    public RequestService(RentRequestRepository rentRequestRepository, UserService userService, WarehouseService warehouseService) {
        this.rentRequestRepository = rentRequestRepository;
        this.userService = userService;
        this.warehouseService = warehouseService;
    }

    public RentRequest getRentRequestById(int requestId) {
        return rentRequestRepository.findById((long) requestId);
    }

    public RentRequestDTO getRentRequestDTO(int requestId) {
        RentRequest rentRequest = getRentRequestById(requestId);
        return mapToRentRequestDTO(rentRequest);
    }

    public boolean makeRequest(String userName, RentRequestDTO rentRequestDTO) {
        RentRequest rentRequest = new RentRequest();
        mapToRentRequest(rentRequestDTO, rentRequest);
        User user = userService.getUserById(userName);
        if (user == null) {
            return false;
        }
        Warehouse warehouse = warehouseService.getWarehouseById(rentRequestDTO.getWarehouseId());
        if (warehouse == null) {
            return false;
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
        int warehouseId = rentRequest.getWarehouse().getWarehouseId();
        int bookId = rentRequest.getBook().getBookId();
        BookCondition condition = rentRequest.getIssueCondition();
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
        int warehouseId = rentRequest.getWarehouse().getWarehouseId();
        int bookId = rentRequest.getBook().getBookId();
        if (warehouseService.addWarehouseStock(warehouseId, List.of(new StockDTO(bookId, returnCondition, 1)))) {
            rentRequest.setReturnTime(System.currentTimeMillis());
            rentRequest.setStatus(Status.RETURNED);

            // implement the payment logic here
            // check return condition and calculate the payment
            rentRequest.setPrice(0L);

            return true;
        }
        return false;
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
