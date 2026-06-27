package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import com.example.afinal.adapter.CartAdapter;
import com.example.afinal.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(v -> finish());
        }

        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(DataManager.getInstance().getCartItems());
        binding.rvCart.setAdapter(adapter);

        updateTotal();

        binding.btnCheckout.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String address = binding.etAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "请完整填写配送信息", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() < 11) {
                Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

            showPaymentDialog(name, phone, address);
        });
    }

    private void showPaymentDialog(String name, String phone, String address) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payment, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextView tvAmount = dialogView.findViewById(R.id.tvAmount);
        ImageView ivQRCode = dialogView.findViewById(R.id.ivQRCode);
        View btnConfirm = dialogView.findViewById(R.id.btnConfirmPayment);

        double total = DataManager.getInstance().getCartTotal();
        tvAmount.setText(String.format("￥%.2f", total));

        boolean isWechat = binding.rbWechat.isChecked();
        if (isWechat) {
            tvTitle.setText("微信支付确认");
            btnConfirm.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF07C160));
            // 使用微信支付模拟二维码
            Glide.with(this)
                    .load("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=WechatPay_Order_"
                            + System.currentTimeMillis())
                    .into(ivQRCode);
        } else {
            tvTitle.setText("支付宝支付确认");
            btnConfirm.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF1677FF));
            // 使用支付宝支付模拟二维码
            Glide.with(this)
                    .load("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=Alipay_Order_"
                            + System.currentTimeMillis())
                    .into(ivQRCode);
        }

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();

            // 跳转到成功页面
            Intent intent = new Intent(CartActivity.this, OrderSuccessActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("address", address);
            startActivity(intent);

            DataManager.getInstance().clearCart();
            finish();
        });

        dialog.show();
    }

    private void updateTotal() {
        double total = DataManager.getInstance().getCartTotal();
        binding.tvTotal.setText(String.format("合计: ￥%.2f", total));
    }
}
