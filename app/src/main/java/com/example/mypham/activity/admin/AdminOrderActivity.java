package com.example.mypham.activity.admin;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mypham.R;
import com.example.mypham.adapter.admin.AdminOrderAdapter;
import com.example.mypham.model.hoaDon;
import com.example.mypham.sqlite.DAO.HoaDonDAO;
import java.util.ArrayList;

public class AdminOrderActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ListView lvOrders;

    private HoaDonDAO hoaDonDAO;
    private ArrayList<hoaDon> pendingOrders;
    private AdminOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        btnBack = findViewById(R.id.btnBackAdminOrder);
        lvOrders = findViewById(R.id.lvAdminOrders);
        hoaDonDAO = new HoaDonDAO(this);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPendingOrders();
    }

    private void loadPendingOrders() {
        // Tái sử dụng hàm getAllPendingOrders() sẵn có của bạn
        pendingOrders = hoaDonDAO.getAllPendingOrders();

        if (pendingOrders.isEmpty()) {
            Toast.makeText(this, "Không có đơn hàng nào cần duyệt!", Toast.LENGTH_SHORT).show();
        }

        adapter = new AdminOrderAdapter(this, pendingOrders, maHoaDon -> {
            new AlertDialog.Builder(AdminOrderActivity.this)
                    .setTitle("Xác nhận Duyệt Đơn")
                    .setMessage("Sau khi duyệt, doanh thu sẽ được cộng. Bạn chắc chắn chứ?")
                    .setPositiveButton("Duyệt", (dialog, which) -> {
                        // Gọi hàm confirmReceived sẵn có của bạn để chuyển trạng_thái = 1
                        hoaDonDAO.confirmReceived(maHoaDon);
                        Toast.makeText(AdminOrderActivity.this, "Đã duyệt đơn hàng!", Toast.LENGTH_SHORT).show();
                        loadPendingOrders(); // Tự động làm mới danh sách
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        lvOrders.setAdapter(adapter);
    }
}