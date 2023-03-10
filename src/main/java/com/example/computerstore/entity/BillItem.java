package com.example.computerstore.entity;

import javax.persistence.*;

@Table(name = "bill_item")
@Entity
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "bill_id")
    private long billId;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private int totalPrice;

    public BillItem(long billId, String name, int quantity, int totalPrice) {
        this.billId = billId;
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public BillItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
