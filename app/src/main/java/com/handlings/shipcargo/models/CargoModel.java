package com.handlings.shipcargo.models;

public class CargoModel {
    int id ;
    String cargoName;
    String pickUp;
    String vehicleType;
    String destination;
    String createdTime;
    String Price;
    String locationDescription;
    String phone;
    Boolean accepted;
    Boolean arrived;

    public CargoModel(int id, String cargoName, String pickUp, String vehicleType, String destination, String createdTime, String price, String locationDescription, String phone, Boolean accepted, Boolean arrived) {
        this.id = id;
        this.cargoName = cargoName;
        this.pickUp = pickUp;
        this.vehicleType = vehicleType;
        this.destination = destination;
        this.createdTime = createdTime;
        Price = price;
        this.locationDescription = locationDescription;
        this.phone = phone;
        this.accepted = accepted;
        this.arrived = arrived;
    }

    public Boolean getArrived() {
        return arrived;
    }

    public void setArrived(Boolean arrived) {
        this.arrived = arrived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
