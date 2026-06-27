package com.example.afinal;

import com.example.afinal.model.CartItem;
import com.example.afinal.model.Food;
import com.example.afinal.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<User> users = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();
    private User currentUser;

    private DataManager() {
        // 使用更稳定的图源链接
        foods.add(new Food("经典牛肉堡", "多汁牛肉配新鲜生菜", 28.0,
                "https://images.unsplash.com/photo-1571091718767-18b5b1457add?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("香辣鸡腿堡", "劲辣鸡腿肉，香脆可口", 25.0,
                "https://images.unsplash.com/photo-1610614819513-58e34989848b?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("芝士披萨", "浓郁芝士，拉丝诱惑", 45.0,
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("意式肉酱面", "经典配方，意犹未尽", 32.0,
                "https://images.unsplash.com/photo-1551183053-bf91a1d81141?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("炸鸡拼盘", "外酥里嫩，分享美味", 58.0,
                "https://images.unsplash.com/photo-1562967914-608f82629710?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("鲜榨橙汁", "新鲜橙子，VC满满", 15.0,
                "https://images.unsplash.com/photo-1557800636-894a64c1696f?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("冰爽可乐", "夏日必备，清爽透心", 6.0,
                "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("提拉米苏", "香醇咖啡，丝滑口感", 22.0,
                "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("凯撒沙拉", "健康轻食，美味无负担", 26.0,
                "https://images.unsplash.com/photo-1550304943-4f24f54ddde9?q=80&w=400&h=400&auto=format&fit=crop"));
        foods.add(new Food("香脆薯条", "金黄诱人，外脆里糯", 12.0,
                "https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?q=80&w=400&h=400&auto=format&fit=crop"));
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void registerUser(String username, String password) {
        users.add(new User(username, password));
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addToCart(Food food) {
        for (CartItem item : cartItems) {
            if (item.getFood().getName().equals(food.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(food, 1));
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
