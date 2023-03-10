package com.example.computerstore.entity;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="item_id")
    private long itemId;

    @Column(name="user_id")
    private long userId;

    public CartItem() {
    }

    public CartItem(long id, long itemId, long userId){
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
    }

    public CartItem(long itemId, long userId){
        this.userId = userId;
        this.itemId = itemId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
