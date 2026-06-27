package com.example.afinal.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.afinal.databinding.ItemBannerBinding;
import com.example.afinal.model.Food;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Food> bannerFoods;

    public BannerAdapter(List<Food> bannerFoods) {
        this.bannerFoods = bannerFoods;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBannerBinding binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Food food = bannerFoods.get(position % bannerFoods.size()); // 无限轮播逻辑
        holder.binding.tvBannerTitle.setText(food.getName());
        Glide.with(holder.itemView.getContext())
                .load(food.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.ivBanner);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE; // 模拟无限循环
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ItemBannerBinding binding;

        public BannerViewHolder(ItemBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
