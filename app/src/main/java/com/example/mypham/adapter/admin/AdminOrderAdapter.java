package com.example.mypham.adapter.admin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.mypham.R;
import com.example.mypham.model.hoaDon;
import com.example.mypham.sqlite.DAO.HoaDonDAO;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class AdminOrderAdapter extends ArrayAdapter<hoaDon> {
    private Activity context;
    private ArrayList<hoaDon> orderList;
    private HoaDonDAO hoaDonDAO;
    private OnApproveListener listener;

    public interface OnApproveListener {
        void onApprove(int maHoaDon);
    }

    public AdminOrderAdapter(Activity context, ArrayList<hoaDon> orderList, OnApproveListener listener) {
        super(context, R.layout.activity_item_admin_order, orderList);
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
        this.hoaDonDAO = new HoaDonDAO(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.activity_item_admin_order, parent, false);
        }

        hoaDon order = orderList.get(position);

        TextView tvCode = row.findViewById(R.id.tvAdminOrderCode);
        TextView tvMethod = row.findViewById(R.id.tvAdminOrderMethod);
        TextView tvItems = row.findViewById(R.id.tvAdminOrderItems);
        TextView tvTotal = row.findViewById(R.id.tvAdminOrderTotal);
        MaterialButton btnApprove = row.findViewById(R.id.btnApproveOrder);

        if (order != null) {
            tvCode.setText("Mã đơn: " + order.getMaHoaDonFormat());
            tvTotal.setText(String.format("%,.0f đ", order.getTongTien()));
            tvMethod.setText(order.getPhuongThucThanhToan());

            // Gọi hàm mới tạo để lấy danh sách món hàng khách mua
            String items = hoaDonDAO.getChiTietDonHangString(order.getMaHoaDon());
            tvItems.setText(items);

            btnApprove.setOnClickListener(v -> {
                if (listener != null) listener.onApprove(order.getMaHoaDon());
            });
        }
        return row;
    }
}