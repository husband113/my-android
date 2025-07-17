package com.simpleshoestore.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.simpleshoestore.R;
import com.simpleshoestore.models.Shoe;
import com.simpleshoestore.utils.ImageUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddShoeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static List<Shoe> shoesList = new ArrayList<>();
    private ImageView ivShoeImage;
    private static ImageView ivshoe;
    private EditText etName, etBrand, etPrice, etDescription, etStock;
    private Button btnSelectImage, btnSave, btnCancel;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shoe);
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        ivShoeImage = findViewById(R.id.iv_shoe_image);
        etName = findViewById(R.id.et_name);
        etBrand = findViewById(R.id.et_brand);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        etStock = findViewById(R.id.et_stock);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupClickListeners() {
        btnSelectImage.setOnClickListener(v -> selectImage());
        btnSave.setOnClickListener(v -> saveShoe());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivShoeImage.setImageBitmap(bitmap);
                selectedImagePath = ImageUtils.saveImageToInternalStorage(this, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveShoe() {
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (name.isEmpty() || brand.isEmpty() || priceStr.isEmpty() || description.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            Shoe newShoe = new Shoe(
                    (int) System.currentTimeMillis(),
                    name,
                    brand,
                    price,
                    selectedImagePath,
                    description,
                    stock
            );
            addShoe(newShoe);
            Toast.makeText(this, "鞋子上架成功", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的价格和库存", Toast.LENGTH_SHORT).show();
        }
    }

    // 初始化10个鞋子样品
    public static void initializeDefaultShoes() {
        if (shoesList.isEmpty()) {
            // 1. Nike Air Jordan 1
            shoesList.add(new Shoe(1, "Air Jordan 1", "Nike", 1299.0,
                    "https://imgservice.suning.cn/uimg1/b2c/image/1LkpyLQlc3AZZiqJEX8WOQ.png",
                    "经典篮球鞋，高帮设计，复古风格", 50));

            // 2. Adidas Ultraboost 22
            shoesList.add(new Shoe(2, "Ultraboost 22", "Adidas", 1199.0,
                    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fi3%2F3900242952%2FO1CN01MocfVR1Xg4CmWF9to_%21%213900242952.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1755087334&t=a1d0fb0b25ae732193609740f43dbc06",
                    "专业跑步鞋，Boost中底，缓震透气", 30));

            // 3. Converse Chuck Taylor
            shoesList.add(new Shoe(3, "Chuck Taylor", "Converse", 499.0,
                    "https://www.converse.com/dw/image/v2/BCZC_PRD/on/demandware.static/-/Sites-cnv-master-catalog/default/dwd2d1a49e/images/a_107/M9160_A_107X1.jpg",
                    "经典帆布鞋，百搭款式，舒适耐穿", 80));

            // 4. Nike Air Max 97
            shoesList.add(new Shoe(4, "Air Max 97", "Nike", 1499.0,
                    "https://miaobi-lite.bj.bcebos.com/miaobi/5mao/b%27LV8xNzM1NzAzOTQwLjg0MDEwMzY%3D%27/0.png?authorization=bce-auth-v1%2FALTAKmda7zOvhZVbRzBLewvCMU%2F2025-01-01T03%3A59%3A00Z%2F-1%2F%2F7feaa89ae7ba0b722711e6d13f749f236764d191d4991b41050a458fc533cdfe","复古跑鞋，全掌气垫，金属质感", 45));

            // 5. Adidas Stan Smith
            shoesList.add(new Shoe(5, "Stan Smith", "Adidas", 799.0,
                    "https://imgcache.dealmoon.com/thumbimg.dealmoon.com/us2408/dealmoon/045/6bd/34b/40e222043128f3634a78dbbx840x840x35.jpg_480_480_2_2317.jpg",
                    "经典板鞋，简约设计，真皮材质", 60));

            // 6. Vans Sk8-Hi
            shoesList.add(new Shoe(6, "Sk8-Hi", "Vans", 699.0,
                    "https://img.vxxsfxxs.com/wp-content/uploads/2019/03/OG-SK8-HI-BLACK-NAYV-3_2048x.jpg",
                    "高帮滑板鞋，帆布材质，耐磨鞋底", 35));

            // 7. New Balance 990v6
            shoesList.add(new Shoe(7, "New Balance 990v6", "New Balance", 1799.0,
                    "https://t13.baidu.com/it/u=3848221756,3327677132&fm=224&app=112&f=JPEG?w=500&h=500",
                    "高端跑鞋，美国制造，缓震支撑", 25));

            // 8. Reebok Classic Leather
            shoesList.add(new Shoe(8, "Classic Leather", "Reebok", 599.0,
                    "https://imgcache.dealmoon.com/thumbimg.dealmoon.com/dealmoon/290/0f1/aed/e87fdded04abf8a3e5d2778.jpg_600_0_3_821d.jpg",
                    "复古休闲鞋，皮革鞋面，舒适鞋垫", 50));

            // 9. Reebok Club C 85
            shoesList.add(new Shoe(9, "Club C 85", "Reebok", 649.0,
                    "https://imgcache.dealmoon.com/thumbimg.dealmoon.com/dealmoon/cc8/15f/3e4/8276f1367a2eb124b10ae27.jpg_600_0_3_d3a4.jpg",
                    "经典网球鞋，简约设计，百搭风格", 40));

            // 10. Nike Lunar Force 1
            shoesList.add(new Shoe(10, "Lunar Force 1", "Nike", 999.0,
                    "https://a.vpimg3.com/upload/merchandise/pdc/762/469/2979488842591469762/3/629970-400-2.jpg",
                    "空军一号衍生款，露娜鞋垫，舒适缓震", 30));
        }
    }

    public static void addShoe(Shoe shoe) {
        shoesList.add(shoe);
    }

    public static List<Shoe> getAllShoes() {
        return new ArrayList<>(shoesList);
    }
}