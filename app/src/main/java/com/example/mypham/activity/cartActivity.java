package com.example.mypham.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mypham.R;
import com.example.mypham.adapter.cartAdapter;
import com.example.mypham.model.cartItem;
import com.example.mypham.sqlite.DAO.GioHangDAO;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class cartActivity extends AppCompatActivity {

    private ListView lvCartItems;
    private CheckBox cbSelectAll;
    private TextView tvTotalPrice, tvCartTitle;
    private MaterialButton btnCheckout;
    private ImageButton btnBack;

    private ArrayList<cartItem> cartList;
    private cartAdapter adapter;
    private GioHangDAO gioHangDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        lvCartItems = findViewById(R.id.lvCartItems);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        tvTotalPrice = findViewById(R.id.tvCartTotalPrice);
        tvCartTitle = findViewById(R.id.tvCartTitle);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBackCart);

        gioHangDAO = new GioHangDAO(this);
        cartList = new ArrayList<>();

        adapter = new cartAdapter(this, cartList, new cartAdapter.OnCartActionListener() {
            @Override
            public void onQuantityChanged(int maGioHang, int newQty) {
                gioHangDAO.updateQuantity(maGioHang, newQty);
                refreshCart();
            }

            @Override
            public void onItemChecked(int maGioHang, boolean isChecked) {
                gioHangDAO.updateCheckStatus(maGioHang, isChecked);
                refreshCart();
            }

            @Override
            public void onItemDeleted(int maGioHang) {
                gioHangDAO.deleteCartItem(maGioHang);
                refreshCart();
            }
        });
        lvCartItems.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        // Chọn tất cả / Bỏ chọn tất cả chuẩn Shopee
        cbSelectAll.setOnClickListener(v -> {
            gioHangDAO.checkAll(cbSelectAll.isChecked());
            refreshCart();
        });

        btnCheckout.setOnClickListener(v -> {
            int selectedCount = 0;
            for (cartItem item : cartList) {
                if (item.isChecked()) selectedCount++;
            }
            if (selectedCount > 0) {
                Toast.makeText(this, "Tiến hành thanh toán cho " + selectedCount + " sản phẩm!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng chọn ít nhất 1 sản phẩm để mua hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        refreshCart();
    }

    private void refreshCart() {
        cartList.clear();
        cartList.addAll(gioHangDAO.getAllCartItems());

        double totalMoney = 0;
        int checkedItemsCount = 0;
        int totalItemsInCart = cartList.size();

        for (cartItem item : cartList) {
            if (item.isChecked()) {
                totalMoney += (item.getGia() * item.getSoLuong());
                checkedItemsCount++;
            }
        }

        // Đổ thông tin số liệu thời gian thực lên giao diện thanh toán dưới đáy
        tvTotalPrice.setText(String.format("%,.0f đ", totalMoney));
        tvCartTitle.setText("Giỏ hàng (" + totalItemsInCart + ")");
        btnCheckout.setText("Mua hàng (" + checkedItemsCount + ")");

        // Cập nhật trạng thái tự động cho checkbox "Chọn tất cả"
        if (totalItemsInCart > 0 && checkedItemsCount == totalItemsInCart) {
            cbSelectAll.setChecked(true);
        } else {
            cbSelectAll.setChecked(false);
        }

        adapter.notifyDataSetChanged();
    }
}