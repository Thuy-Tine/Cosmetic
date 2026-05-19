package com.example.mypham.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast; // Đã thêm import

import androidx.appcompat.app.AlertDialog; // Đã thêm import
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypham.R;
import com.example.mypham.model.product;
import com.example.mypham.sqlite.DAO.productDAO;
import com.example.mypham.adapter.admin.AdminProductAdapter; // Đã thêm import
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminProductActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ListView lvAdminProducts;
    private FloatingActionButton fabAddProduct;

    private productDAO dao;
    private ArrayList<product> productList;
    private AdminProductAdapter adapter; // ĐÃ THÊM BIẾN NÀY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        // 1. Ánh xạ
        btnBack = findViewById(R.id.btnBackAdmin);
        lvAdminProducts = findViewById(R.id.lvAdminProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        // 2. Khởi tạo dữ liệu
        dao = new productDAO(this);
        productList = new ArrayList<>();

        // 3. Xử lý sự kiện
        addEvents();

        // Ghi chú: Không cần gọi loadData() ở đây nữa vì onResume() sẽ tự động chạy ngay sau onCreate()
    }

    private void loadData() {
        productList.clear();
        productList.addAll(dao.getAllProducts());

        if (adapter == null) {
            adapter = new AdminProductAdapter(this, productList, new AdminProductAdapter.OnProductAdminActionListener() {
                @Override
                public void onEditClick(product sp) {
                    // Mở form SỬA và truyền dữ liệu
                    Intent intent = new Intent(AdminProductActivity.this, AddEditProductActivity.class);
                    intent.putExtra("ma_san_pham", sp.getMaSanPham());
                    intent.putExtra("ten_san_pham", sp.getName());
                    intent.putExtra("gia", sp.getPrice());
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(product sp) {
                    // XỬ LÝ XÓA
                    new AlertDialog.Builder(AdminProductActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm: " + sp.getName() + "?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                if (dao.deleteProduct(sp.getMaSanPham())) {
                                    Toast.makeText(AdminProductActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                                    loadData(); // Cập nhật lại UI lập tức
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            });
            lvAdminProducts.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // ĐÃ GOM CÁC SỰ KIỆN VÀO ĐÚNG HÀM NÀY
    private void addEvents() {
        // Nút trở về
        btnBack.setOnClickListener(v -> finish());

        // Nút Thêm mới sản phẩm
        fabAddProduct.setOnClickListener(v -> {
            // Mở form THÊM MỚI (không truyền dữ liệu)
            Intent intent = new Intent(AdminProductActivity.this, AddEditProductActivity.class);
            startActivity(intent);
        });
    }

    // THÊM HÀM NÀY: Để khi từ Form Thêm/Sửa quay lại, danh sách tự động làm mới
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}