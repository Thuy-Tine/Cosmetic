package com.example.mypham.activity.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypham.R;
import com.example.mypham.sqlite.DAO.GioHangDAO;
import com.example.mypham.sqlite.DAO.HoaDonDAO;
import com.google.android.material.button.MaterialButton;

public class checkoutActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvItems, tvTotal;
    private RadioGroup rgPayment;
    private MaterialButton btnPlaceOrder;
    private GioHangDAO gioHangDAO;
    private double totalPrice = 0;
    private int totalItems = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        gioHangDAO = new GioHangDAO(this);
        // Ánh xạ
        btnBack = findViewById(R.id.btnBackCheckout);
        tvItems = findViewById(R.id.tvCheckoutItems);
        tvTotal = findViewById(R.id.tvCheckoutTotal);
        rgPayment = findViewById(R.id.rgPaymentMethod);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // Nhận dữ liệu từ Giỏ hàng
        totalItems = getIntent().getIntExtra("TOTAL_ITEMS", 0);
        totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0);

        // Hiển thị lên UI
        tvItems.setText("Tổng số sản phẩm: " + totalItems);
        tvTotal.setText(String.format("%,.0f đ", totalPrice));

        // Sự kiện trở về
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện đặt hàng
        btnPlaceOrder.setOnClickListener(v -> processOrder());
    }

    private void processOrder() {
        int selectedId = rgPayment.getCheckedRadioButtonId();

        if (selectedId == R.id.rbBankTransfer) {
            showBankingInfoDialog();
        } else if (selectedId == R.id.rbCOD) {
            // Truyền phương thức thanh toán vào
            completeOrder("COD");
        }
    }

    private void showBankingInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin chuyển khoản")
                .setMessage("Ngân hàng: Vietcombank\n" +
                        "STK: 0123456789\n" +
                        "Chủ tài khoản: NGUYEN VAN A\n\n" +
                        "Nội dung CK: THANHTOAN " + System.currentTimeMillis() + "\n" +
                        "Số tiền: " + String.format("%,.0f đ", totalPrice))
                .setPositiveButton("Đã chuyển khoản", (dialog, which) -> {
                    Toast.makeText(this, "Đơn hàng đang chờ xác nhận thanh toán!", Toast.LENGTH_LONG).show();
                    completeOrder("Chuyển khoản"); // Gọi hàm completeOrder để lưu vào DB
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void completeOrder(String method) {
        // 1. Khởi tạo HoaDonDAO
        HoaDonDAO dao = new HoaDonDAO(this);

        // 2. Sử dụng thanhToanDonHang (Tự động: Tạo hóa đơn -> Copy chi tiết -> Xóa giỏ)
        boolean isSuccess = dao.thanhToanDonHang(totalPrice, method);

        if (isSuccess) {
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

            // 3. Quay về trang chủ và xóa lịch sử trang giỏ hàng/thanh toán
            Intent intent = new Intent(checkoutActivity.this, productActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Lỗi thanh toán! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}