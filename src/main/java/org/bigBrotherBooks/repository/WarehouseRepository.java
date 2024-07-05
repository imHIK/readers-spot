package org.bigBrotherBooks.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Warehouse;

import java.util.List;

@Transactional
@Singleton
public class WarehouseRepository implements PanacheRepositoryBase<Warehouse, Long> {

    // get warehouses by city
    public List<Warehouse> findByCity(String city) {
        return list("city", city);
    }

}
