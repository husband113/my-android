package com.simpleshoestore.models;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private List<CartItem> items;
    private double totalAmount;
    private Date orderDate;
    private String status;
    private String paymentMethod;

    public Order(String orderId, List<CartItem> items, double totalAmount, String paymentMethod) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = new Date();
        this.status = "已完成";
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}