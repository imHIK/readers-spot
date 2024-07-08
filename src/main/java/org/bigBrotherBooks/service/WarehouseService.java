package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.StockDTO;
import org.bigBrotherBooks.dto.WarehouseDTO;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.Stock;
import org.bigBrotherBooks.model.Warehouse;
import org.bigBrotherBooks.repository.StockRepository;
import org.bigBrotherBooks.repository.WarehouseRepository;

import java.util.List;

@Singleton
public class WarehouseService {

    WarehouseRepository warehouseRepo;
    BookService bookService;
    StockRepository stockRepo;

    @Inject
    public WarehouseService(WarehouseRepository warehouseRepo, BookService bookService, StockRepository stockRepo) {
        this.warehouseRepo = warehouseRepo;
        this.bookService = bookService;
        this.stockRepo = stockRepo;
    }

    @Transactional
    public Warehouse getWarehouseById(int id) {
        return warehouseRepo.findById((long) id);
    }

    public WarehouseDTO getWarehouseDTO(int id) {
        Warehouse warehouse = getWarehouseById(id);
        return mapToWarehouseDTO(warehouse);
    }


    @Transactional
    public void saveWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = new Warehouse();
        mapToWarehouse(warehouseDTO, warehouse);
        warehouseRepo.persist(warehouse);
    }

    @Transactional
    public boolean deleteWarehouse(int id) {
        Warehouse warehouse = getWarehouseById(id);
        if (warehouse == null) {
            return false;
        }
        warehouseRepo.delete(warehouse);
        return true;
    }

    @Transactional
    public boolean updateWarehouse(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = getWarehouseById(warehouseDTO.getWarehouseId());
        if (warehouse == null) {
            return false;
        }
        mapToWarehouse(warehouseDTO, warehouse);
        return true;
    }

    public List<Warehouse> getWarehousesByCity(String city) {
        return warehouseRepo.findByCity(city);
    }

    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseRepo.findAll().stream().map(WarehouseService::mapToWarehouseDTO).toList();
    }


    @Transactional
    public boolean addWarehouseStock(int warehouseId, List<StockDTO> stockDTOList) {
        Warehouse warehouse = getWarehouseById(warehouseId);
        if (warehouse == null) {
            return false;
        }
        for (StockDTO stockDTO : stockDTOList) {
            Stock.StockId stockId = new Stock.StockId(warehouseId, stockDTO.getBookId(), stockDTO.getCondition());
            Stock stock = getStock(stockId);
            if (stock == null) {
                stock = new Stock(stockId, stockDTO.getQuantity());
                Book book = bookService.getBookById(stockDTO.getBookId());
                if (book == null) {
                    return false;
                }
                book.addWarehouseStock(stock);
                warehouse.addBookStock(stock);
                stockRepo.persist(stock);
            } else {
                stock.setQuantity(stock.getQuantity() + stockDTO.getQuantity());
            }
        }
        return true;
    }

    @Transactional
    public boolean removeWarehouseStock(int warehouseId, StockDTO stockDTO) {
        Stock stock = getStock(new Stock.StockId(warehouseId, stockDTO.getBookId(), stockDTO.getCondition()));
        if (stock == null) {
            return false;
        }
        stock.setQuantity(stock.getQuantity() - stockDTO.getQuantity());
        if (stock.getQuantity() == 0) {
            stockRepo.delete(stock);
        }
        return true;
    }

    @Transactional
    public List<StockDTO> getWarehouseStock(int warehouseId) {
        Warehouse warehouse = getWarehouseById(warehouseId);
        if (warehouse == null) {
            return null;
        }
        return warehouse.getBookStock().stream().map(WarehouseService::mapToStockDTO).toList();
    }

    @Transactional
    public Stock getStock(Stock.StockId stockId) {
        return stockRepo.findById(stockId);
    }


    public static WarehouseDTO mapToWarehouseDTO(Warehouse warehouse) {
        return new WarehouseDTO(warehouse.getWarehouseId(), warehouse.getName(), warehouse.getCity(), warehouse.getArea(), warehouse.getAddress(), warehouse.getPhone(), warehouse.getEmail(), warehouse.getManagerName(), warehouse.getRating());
    }

    private void mapToWarehouse(WarehouseDTO warehouseDTO, Warehouse warehouse) {
        warehouse.setName(warehouseDTO.getName());
        warehouse.setCity(warehouseDTO.getCity());
        warehouse.setArea(warehouseDTO.getArea());
        warehouse.setAddress(warehouseDTO.getAddress());
        warehouse.setPhone(warehouseDTO.getPhone());
        warehouse.setEmail(warehouseDTO.getEmail());
        warehouse.setManagerName(warehouseDTO.getManagerName());
        warehouse.setRating(warehouseDTO.getRating());
    }

    public static StockDTO mapToStockDTO(Stock stock) {
        return new StockDTO(stock.getStockId().getBookId(), stock.getStockId().getCondition(), stock.getQuantity());
    }


}
