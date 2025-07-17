package com.simpleshoestore.utils;

import com.simpleshoestore.models.Order;
import com.simpleshoestore.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
        // 添加一些模拟订单
        addMockOrders();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(0, order); // 添加到列表开头
    }

    public List<Order> getUserOrders() {
        return new ArrayList<>(orders);
    }

    private void addMockOrders() {
        // 添加一些模拟历史订单
        List<CartItem> mockItems1 = new ArrayList<>();
        List<CartItem> mockItems2 = new ArrayList<>();

        // 这里可以添加一些模拟数据
    }
}