package com.simpleshoestore.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.simpleshoestore.R;
import com.simpleshoestore.models.CartItem;
import com.simpleshoestore.models.Shoe;
import com.simpleshoestore.utils.CartManager;
import com.simpleshoestore.utils.ImageUtils;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private CartManager cartManager;
    private OnCartUpdateListener updateListener;

    // 回调接口
    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    // 构造方法
    public CartAdapter(Context context, List<CartItem> cartItems, OnCartUpdateListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartManager = CartManager.getInstance();
        this.updateListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载购物车项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item); // 绑定数据
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // 购物车项视图持有者
    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivShoe;
        private TextView tvName, tvBrand, tvPrice, tvQuantity, tvTotalPrice;
        private Button btnDecrease, btnIncrease;
        private ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // 绑定控件
            ivShoe = itemView.findViewById(R.id.iv_shoe);
            tvName = itemView.findViewById(R.id.tv_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        // 绑定购物车项数据
        public void bind(CartItem item) {
            Shoe shoe = item.getShoe(); // 获取商品信息

            // 设置文本信息
            tvName.setText(shoe.getName());
            tvBrand.setText(shoe.getBrand());
            tvPrice.setText("￥" + String.format("%.2f", shoe.getPrice()));
            tvQuantity.setText(String.valueOf(item.getQuantity()));
            tvTotalPrice.setText("￥" + String.format("%.2f", item.getTotalPrice()));

            // 加载商品图片（核心修复：添加图片加载逻辑）
            loadShoeImage(shoe.getImageUrl());

            // 减少数量按钮
            btnDecrease.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity >= 0) {
                    cartManager.updateQuantity(shoe.getId(), newQuantity);
                    item.setQuantity(newQuantity);
                    updateUI(item);
                    updateListener.onCartUpdated();
                }
            });

            // 增加数量按钮
            btnIncrease.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() + 1;
                cartManager.updateQuantity(shoe.getId(), newQuantity);
                item.setQuantity(newQuantity);
                updateUI(item);
                updateListener.onCartUpdated();
            });

            // 移除商品按钮
            btnRemove.setOnClickListener(v -> {
                cartManager.removeFromCart(shoe.getId());
                cartItems.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                updateListener.onCartUpdated();
            });
        }

        // 更新数量和总价UI
        private void updateUI(CartItem item) {
            tvQuantity.setText(String.valueOf(item.getQuantity()));
            tvTotalPrice.setText("￥" + String.format("%.2f", item.getTotalPrice()));
        }

        // 加载商品图片（支持网络、本地和资源图片）
        private void loadShoeImage(String imageUrl) {
            // 打印图片路径用于调试
            Log.d("CartImageLoad", "加载图片路径: " + imageUrl);

            if (imageUrl == null || imageUrl.isEmpty()) {
                // 无图片路径时显示默认图
                ivShoe.setImageResource(R.drawable.default_shoe);
                return;
            }

            try {
                // 1. 尝试加载drawable资源图片（如R.drawable.ig1）
                int resId = Integer.parseInt(imageUrl);
                ivShoe.setImageResource(resId);
            } catch (NumberFormatException e) {
                // 2. 不是资源ID，尝试网络或本地图片
                if (imageUrl.startsWith("http")) {
                    // 网络图片：使用Glide加载
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.default_shoe) // 加载中占位图
                            .error(R.drawable.default_shoe)     // 加载失败图
                            .into(ivShoe);
                } else {
                    // 本地图片：从存储加载
                    Bitmap bitmap = ImageUtils.loadImageFromStorage(imageUrl);
                    if (bitmap != null) {
                        ivShoe.setImageBitmap(bitmap);
                    } else {
                        // 本地图片加载失败，显示默认图
                        ivShoe.setImageResource(R.drawable.default_shoe);
                    }
                }
            }
        }
    }
}