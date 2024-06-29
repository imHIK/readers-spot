package org.bigBrotherBooks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class WarehouseDTO {

    private int warehouseId;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "City cannot be null")
    private String city;
    @NotNull(message = "Area cannot be null")
    private String area;
    @NotNull(message = "Address cannot be null")
    private String address;
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Valid phone number is required")
    private String phone;
    @Email(message = "Invalid email")
    public String email;
    private String managerName;
    private Double rating;

    public WarehouseDTO() {
    }

    public WarehouseDTO(int warehouseId, String name, String city, String area, String address, String phone, String email, String managerName, Double rating) {
        this.warehouseId = warehouseId;
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

    @Override
    public String toString() {
        return "WarehouseDTO{" +
                "warehouseId=" + warehouseId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", managerName='" + managerName + '\'' +
                ", rating=" + rating +
                '}';
    }
}
