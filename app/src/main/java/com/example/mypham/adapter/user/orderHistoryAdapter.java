package com.example.mypham.adapter.user;

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
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class orderHistoryAdapter extends ArrayAdapter<hoaDon> {
    private Activity context;
    private ArrayList<hoaDon> orderList;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onConfirmReceived(int maHoaDon);
    }

    public orderHistoryAdapter(Activity context, ArrayList<hoaDon> orderList, OnOrderActionListener listener) {
        super(context, R.layout.activity_item_order, orderList);
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    private static class ViewHolder {
        TextView tvCode, tvTotal, tvStatus; // THÊM tvStatus
        MaterialButton btnConfirm;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.activity_item_order, parent, false);

            holder = new ViewHolder();
            holder.tvCode = row.findViewById(R.id.tvOrderCode);
            holder.tvTotal = row.findViewById(R.id.tvOrderTotal);
            holder.tvStatus = row.findViewById(R.id.tvOrderStatus); // ÁNH XẠ
            holder.btnConfirm = row.findViewById(R.id.btnConfirmReceived);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        hoaDon order = orderList.get(position);

        if (order != null) {
            holder.tvCode.setText("Mã đơn: " + order.getMaHoaDonFormat());
            holder.tvTotal.setText(String.format("%,.0f đ", order.getTongTien()));

            // ==================================================
            // LOGIC KIỂM TRA PHƯƠNG THỨC THANH TOÁN
            // ==================================================
            if (order.getPhuongThucThanhToan() != null && order.getPhuongThucThanhToan().equals("COD")) {
                holder.tvStatus.setText("Chờ thanh toán (COD)");
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#E53935")); // Màu đỏ cho nổi bật
            } else {
                holder.tvStatus.setText("Đã thanh toán (CK)");
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#43A047")); // Màu xanh lá
            }

            // Nút bấm "Xác nhận đã nhận hàng" luôn giữ nguyên sự kiện
            holder.btnConfirm.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConfirmReceived(order.getMaHoaDon());
                }
            });
        }
        return row;
    }
}