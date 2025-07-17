package com.simpleshoestore.utils;

import com.simpleshoestore.models.CartItem;
import com.simpleshoestore.models.Shoe;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Shoe shoe, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getShoe().getId() == shoe.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(shoe, quantity));
    }

    public void removeFromCart(int shoeId) {
        cartItems.removeIf(item -> item.getShoe().getId() == shoeId);
    }

    public void updateQuantity(int shoeId, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(shoeId);
            return;
        }
        for (CartItem item : cartItems) {
            if (item.getShoe().getId() == shoeId) {
                item.setQuantity(newQuantity);
                break;
            }
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getTotalItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }
}