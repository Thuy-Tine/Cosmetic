package com.example.mypham.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mypham.R;
import com.example.mypham.activity.user.loginActivity;
import com.example.mypham.sqlite.DAO.ThongKeDAO;
import com.google.android.material.button.MaterialButton;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvRevenue, tvPendingOrders, tvTotalProducts;
    private MaterialButton btnManageProducts, btnLogout;
    private ThongKeDAO thongKeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // 1. Ánh xạ các View từ Layout
        tvRevenue = findViewById(R.id.tvRevenue);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnLogout = findViewById(R.id.btnLogout);

        // 2. Khởi tạo đối tượng xử lý dữ liệu
        thongKeDAO = new ThongKeDAO(this);

        // 3. Cài đặt các sự kiện Click điều hướng công việc
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tự động làm mới số liệu thống kê thời gian thực mỗi khi màn hình hiển thị
        updateStatistics();
    }

    private void updateStatistics() {
        double revenue = thongKeDAO.getTongDoanhThu();
        int pendingOrders = thongKeDAO.getSoDonHangDangCho();
        int totalProducts = thongKeDAO.getTongSoSanPham();

        // Định dạng hiển thị tiền tệ (Ví dụ: 1,850,000 đ) giống chuẩn trang mua hàng
        tvRevenue.setText(String.format("%,.0f đ", revenue));
        tvPendingOrders.setText(String.valueOf(pendingOrders));
        tvTotalProducts.setText(String.valueOf(totalProducts));
    }

    private void addEvents() {
        // Chuyển sang trang Quản lý sản phẩm của Admin
        btnManageProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminProductActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện đăng xuất tài khoản quản trị
        btnLogout.setOnClickListener(v -> {
            // Xóa luồng và đưa Admin về màn hình đăng nhập ban đầu
            Intent intent = new Intent(AdminDashboardActivity.this, loginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Nếu bạn muốn nhấp thẳng vào chữ "Đơn chờ" để nhảy qua:
        tvPendingOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminOrderActivity.class);
            startActivity(intent);
        });
    }
}