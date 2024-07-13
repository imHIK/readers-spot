package org.bigBrotherBooks.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int warehouseId;

    @Column
    private String name;

    @Column
    private String city;

    @Column
    private String area;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String managerName;

    @Column
    private Double rating;

    @OneToMany(mappedBy = "warehouse")
    private Set<RentRequest> rentRequests;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stock> bookStock;

    public Warehouse() {
    }

    public Warehouse(String name, String city, String area, String address, String phone, String email, String managerName, Double rating) {
        this.name = name;
        this.city = city;
        this.area = area;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.managerName = managerName;
        this.rating = rating;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<RentRequest> getRentRequests() {
        return rentRequests;
    }

    public void setRentRequests(Set<RentRequest> rentRequests) {
        this.rentRequests = rentRequests;
    }

    public void addRentRequest(RentRequest rentRequest) {
        rentRequests.add(rentRequest);
        rentRequest.setWarehouse(this);
    }

    public void removeRentRequest(RentRequest rentRequest) {
        if (rentRequests.contains(rentRequest)) {
            rentRequests.remove(rentRequest);
            rentRequest.setWarehouse(null);
        }
    }

    public Set<Stock> getBookStock() {
        return bookStock;
    }

    public void setBookStock(Set<Stock> bookStock) {
        this.bookStock = bookStock;
    }

    public void addBookStock(Stock stock) {
        bookStock.add(stock);
        stock.setWarehouse(this);
    }

    public void removeBookStock(Stock stock) {
        if (bookStock.contains(stock)) {
            bookStock.remove(stock);
            stock.setWarehouse(null);
        }
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseId=" + warehouseId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", managerName='" + managerName + '\'' +
                ", rating=" + rating +
                ", rentRequests=" + rentRequests +
                ", bookStock=" + bookStock +
                '}';
    }
}
