package com.example.mypham.activity.user;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mypham.R;
import com.example.mypham.adapter.user.orderHistoryAdapter;
import com.example.mypham.model.hoaDon;
import com.example.mypham.sqlite.DAO.HoaDonDAO;
import java.util.ArrayList;

public class orderHistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ListView lvOrders;
    private ArrayList<hoaDon> orderList;
    private orderHistoryAdapter adapter;
    private HoaDonDAO hoaDonDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        btnBack = findViewById(R.id.btnBackHistory);
        lvOrders = findViewById(R.id.lvOrders);

        hoaDonDAO = new HoaDonDAO(this);
        orderList = new ArrayList<>();

        adapter = new orderHistoryAdapter(this, orderList, new orderHistoryAdapter.OnOrderActionListener() {
            @Override
            public void onConfirmReceived(int maHoaDon) {
                // 1. Cập nhật trạng thái hóa đơn sang đã nhận (trang_thai = 1) để ghi tiền vào doanh thu
                hoaDonDAO.confirmReceived(maHoaDon);
                Toast.makeText(orderHistoryActivity.this, "Đã xác nhận nhận hàng & ghi nhận hóa đơn thành công!", Toast.LENGTH_SHORT).show();


                refreshOrderList();
            }
        });

        lvOrders.setAdapter(adapter);
        btnBack.setOnClickListener(v -> finish());

        refreshOrderList();
    }

    private void refreshOrderList() {
        orderList.clear();
        orderList.addAll(hoaDonDAO.getAllPendingOrders());
        adapter.notifyDataSetChanged();
    }
}