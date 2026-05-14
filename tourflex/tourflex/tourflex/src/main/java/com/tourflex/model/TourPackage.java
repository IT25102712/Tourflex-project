package com.tourflex.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tour_packages")
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String category;
    private String type;
    private int maxPeople;
    private String location;
    private int days;
    private double price;
    private String description;
    private String imageName;

    private String hotelName;
    private String transportMethods;
    private String meals;

    public TourPackage() {
    }

    public TourPackage(String name, String category, String type, int maxPeople, String location,
                       int days, double price, String description, String imageName,
                       String hotelName, String transportMethods, String meals) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.maxPeople = maxPeople;
        this.location = location;
        this.days = days;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
        this.hotelName = hotelName;
        this.transportMethods = transportMethods;
        this.meals = meals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getTransportMethods() {
        return transportMethods;
    }

    public void setTransportMethods(String transportMethods) {
        this.transportMethods = transportMethods;
    }

    public String getMeals() {
        return meals;
    }

    public void setMeals(String meals) {
        this.meals = meals;
    }
}