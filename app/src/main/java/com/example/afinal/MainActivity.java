package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.afinal.adapter.BannerAdapter;
import com.example.afinal.adapter.FoodAdapter;
import com.example.afinal.databinding.ActivityMainBinding;
import com.example.afinal.model.Food;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FoodAdapter.OnFoodClickListener {

    private ActivityMainBinding binding;
    private FoodAdapter adapter;
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if logged in
        if (DataManager.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initBanner();
    }

    private void initBanner() {
        List<Food> allFoods = DataManager.getInstance().getFoods();
        List<Food> bannerFoods = new ArrayList<>();
        // 选取前4个作为轮播图
        for (int i = 0; i < Math.min(4, allFoods.size()); i++) {
            bannerFoods.add(allFoods.get(i));
        }

        BannerAdapter bannerAdapter = new BannerAdapter(bannerFoods);
        binding.bannerPager.setAdapter(bannerAdapter);

        // 设置初始位置在中间，实现左右无限滑动感
        binding.bannerPager.setCurrentItem(bannerFoods.size() * 100, false);

        // 自动轮播逻辑
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.bannerPager.getCurrentItem();
                binding.bannerPager.setCurrentItem(currentItem + 1, true);
                bannerHandler.postDelayed(this, 3000); // 3秒切换一次
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    private void initView() {
        binding.rvFood.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(DataManager.getInstance().getFoods(), this);
        binding.rvFood.setAdapter(adapter);

        binding.fabCart.setOnClickListener(v -> {
            if (DataManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "购物车还是空的哦", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CartActivity.class));
            }
        });
    }

    @Override
    public void onAddClick(Food food) {
        DataManager.getInstance().addToCart(food);
        Toast.makeText(this, food.getName() + " 已加入购物车", Toast.LENGTH_SHORT).show();
    }
}
