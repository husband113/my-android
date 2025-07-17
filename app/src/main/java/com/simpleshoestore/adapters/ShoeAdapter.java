package com.simpleshoestore.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.simpleshoestore.R;
import com.simpleshoestore.models.Shoe;
import com.simpleshoestore.utils.CartManager;
import com.simpleshoestore.utils.ImageUtils;
import com.bumptech.glide.Glide;
import java.util.List;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder> {

    private Context context;
    private List<Shoe> shoes;
    private CartManager cartManager;

    public ShoeAdapter(Context context, List<Shoe> shoes) {
        this.context = context;
        this.shoes = shoes;
        this.cartManager = CartManager.getInstance();
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoe, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Shoe shoe = shoes.get(position);
        holder.bind(shoe);
    }

    @Override
    public int getItemCount() {
        return shoes.size();
    }

    public void updateShoes(List<Shoe> newShoes) {
        this.shoes = newShoes;
        notifyDataSetChanged();
    }

    class ShoeViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivShoe;
        private TextView tvName, tvBrand, tvPrice, tvStock;
        private Button btnAddToCart;

        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShoe = itemView.findViewById(R.id.iv_shoe);
            tvName = itemView.findViewById(R.id.tv_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStock = itemView.findViewById(R.id.tv_stock);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }

        public void bind(Shoe shoe) {
            tvName.setText(shoe.getName());
            tvBrand.setText(shoe.getBrand());
            tvPrice.setText("￥" + String.format("%.2f", shoe.getPrice()));
            tvStock.setText("库存：" + shoe.getStockQuantity());

            // 加载图片
            loadShoeImage(shoe.getImageUrl());

            btnAddToCart.setOnClickListener(v -> {
                if (shoe.getStockQuantity() > 0) {
                    cartManager.addToCart(shoe, 1);
                    Toast.makeText(context, "已添加到购物车", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "商品已售完", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(v -> showShoeDetail(shoe));
        }

        private void loadShoeImage(String imageUrl) {
            if (imageUrl.startsWith("http")) {
                // 网络图片，使用Glide加载
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.default_shoe)
                        .error(R.drawable.default_shoe)
                        .into(ivShoe);
            } else if (!imageUrl.isEmpty()) {
                // 本地图片
                Bitmap bitmap = ImageUtils.loadImageFromStorage(imageUrl);
                if (bitmap != null) {
                    ivShoe.setImageBitmap(bitmap);
                } else {
                    ivShoe.setImageResource(R.drawable.default_shoe);
                }
            } else {
                // 默认图片
                ivShoe.setImageResource(R.drawable.default_shoe);
            }
        }

        private void showShoeDetail(Shoe shoe) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_shoe_detail, null);

            ImageView ivDetailShoe = dialogView.findViewById(R.id.iv_detail_shoe);
            TextView tvDetailName = dialogView.findViewById(R.id.tv_detail_name);
            TextView tvDetailBrand = dialogView.findViewById(R.id.tv_detail_brand);
            TextView tvDetailPrice = dialogView.findViewById(R.id.tv_detail_price);
            TextView tvDetailDescription = dialogView.findViewById(R.id.tv_detail_description);
            Button btnDetailAddToCart = dialogView.findViewById(R.id.btn_detail_add_to_cart);

            // 加载详情图片
            if (shoe.getImageUrl().startsWith("http")) {
                Glide.with(context)
                        .load(shoe.getImageUrl())
                        .placeholder(R.drawable.default_shoe)
                        .error(R.drawable.default_shoe)
                        .into(ivDetailShoe);
            } else if (!shoe.getImageUrl().isEmpty()) {
                Bitmap bitmap = ImageUtils.loadImageFromStorage(shoe.getImageUrl());
                if (bitmap != null) {
                    ivDetailShoe.setImageBitmap(bitmap);
                } else {
                    ivDetailShoe.setImageResource(R.drawable.default_shoe);
                }
            } else {
                ivDetailShoe.setImageResource(R.drawable.default_shoe);
            }

            tvDetailName.setText(shoe.getName());
            tvDetailBrand.setText(shoe.getBrand());
            tvDetailPrice.setText("￥" + String.format("%.2f", shoe.getPrice()));
            tvDetailDescription.setText(shoe.getDescription());

            btnDetailAddToCart.setOnClickListener(v -> {
                if (shoe.getStockQuantity() > 0) {
                    cartManager.addToCart(shoe, 1);
                    Toast.makeText(context, "已添加到购物车", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "商品已售完", Toast.LENGTH_SHORT).show();
                }
            });

            new android.app.AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setNegativeButton("关闭", null)
                    .show();
        }
    }
}