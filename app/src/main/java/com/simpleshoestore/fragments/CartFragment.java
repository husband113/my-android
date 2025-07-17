package com.simpleshoestore.fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.simpleshoestore.R;
import com.simpleshoestore.adapters.CartAdapter;
import com.simpleshoestore.utils.CartManager;
import com.simpleshoestore.utils.QRCodeGenerator;
import java.util.UUID;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvTotalAmount, tvItemCount;
    private Button btnCheckout, btnClearCart;
    private CartAdapter adapter;
    private CartManager cartManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartManager = CartManager.getInstance();
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        updateCartInfo();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_cart);
        tvTotalAmount = view.findViewById(R.id.tv_total_amount);
        tvItemCount = view.findViewById(R.id.tv_item_count);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        btnClearCart = view.findViewById(R.id.btn_clear_cart);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(getContext(), cartManager.getCartItems(), this::updateCartInfo);
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnCheckout.setOnClickListener(v -> showPaymentDialog());
        btnClearCart.setOnClickListener(v -> showClearCartDialog());
    }

    private void updateCartInfo() {
        int itemCount = cartManager.getTotalItemCount();
        double totalAmount = cartManager.getTotalAmount();

        tvItemCount.setText("共 " + itemCount + " 件商品");
        tvTotalAmount.setText("￥" + String.format("%.2f", totalAmount));

        btnCheckout.setEnabled(itemCount > 0);
        adapter.notifyDataSetChanged();
    }

    private void showPaymentDialog() {
        if (cartManager.getTotalItemCount() == 0) {
            Toast.makeText(getContext(), "购物车为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = "ORD" + System.currentTimeMillis();
        double amount = cartManager.getTotalAmount();
        String paymentData = QRCodeGenerator.generatePaymentData(amount, orderId);
        Bitmap qrBitmap = QRCodeGenerator.generateQRCode(paymentData, 300, 300);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_payment, null);
        ImageView ivQrCode = dialogView.findViewById(R.id.iv_qr_code);
        TextView tvPaymentInfo = dialogView.findViewById(R.id.tv_payment_info);

        ivQrCode.setImageBitmap(qrBitmap);
        tvPaymentInfo.setText("订单金额：￥" + String.format("%.2f", amount) +
                "\n订单号：" + orderId);

        new AlertDialog.Builder(getContext())
                .setTitle("扫码支付")
                .setView(dialogView)
                .setPositiveButton("支付完成", (dialog, which) -> {
                    cartManager.clearCart();
                    updateCartInfo();
                    Toast.makeText(getContext(), "支付成功！", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showClearCartDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("清空购物车")
                .setMessage("确定要清空购物车吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    cartManager.clearCart();
                    updateCartInfo();
                    Toast.makeText(getContext(), "购物车已清空", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartInfo();
    }
}