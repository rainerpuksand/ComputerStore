package com.example.computerstore.entity;

import javax.persistence.*;

@Entity
@Table(name="computer")
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "price")
    private double price;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "in_stock")
    private String inStock;

    public Computer(String manufacturer, String modelName, double price, String imagePath, String inStock) {
        this.manufacturer = manufacturer;
        this.modelName = modelName;
        this.price = price;
        this.inStock = inStock;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Computer() {
    }

    @Override
    public String toString() {
        return  "Manufacturer: " + manufacturer +
                ", Model: " + modelName +
                ", price: " + price + "$" +
                ", image url: " + imagePath +
                ", in stock: " + inStock;
    }
}
