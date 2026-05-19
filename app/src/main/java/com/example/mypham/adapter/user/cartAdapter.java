package com.example.mypham.adapter.user;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.mypham.R;
import com.example.mypham.model.cartItem;
import java.util.ArrayList;

public class cartAdapter extends ArrayAdapter<cartItem> {
    private Activity context;
    private ArrayList<cartItem> dsCart;
    private OnCartActionListener actionListener;

    public interface OnCartActionListener {
        void onQuantityChanged(int maGioHang, int newQty);
        void onItemChecked(int maGioHang, boolean isChecked);
        void onItemDeleted(int maGioHang);
    }

    public cartAdapter(Activity context, ArrayList<cartItem> dsCart, OnCartActionListener actionListener) {
        super(context, R.layout.activity_item_cart, dsCart);
        this.context = context;
        this.dsCart = dsCart;
        this.actionListener = actionListener;
    }

    private static class ViewHolder {
        CheckBox cbItem;
        ImageView imgItem;
        TextView tvName, tvPrice, tvQty, btnMinus, btnPlus;
        ImageButton btnDelete;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.activity_item_cart, parent, false);

            holder = new ViewHolder();
            holder.cbItem = row.findViewById(R.id.cbCartItem);
            holder.imgItem = row.findViewById(R.id.imgCartItem);
            holder.tvName = row.findViewById(R.id.tvCartItemName);
            holder.tvPrice = row.findViewById(R.id.tvCartItemPrice);
            holder.tvQty = row.findViewById(R.id.tvCartItemQty);
            holder.btnMinus = row.findViewById(R.id.btnMinus);
            holder.btnPlus = row.findViewById(R.id.btnPlus);
            holder.btnDelete = row.findViewById(R.id.btnDeleteCartItem);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        cartItem item = dsCart.get(position);

        if (item != null) {
            holder.tvName.setText(item.getTenSanPham());
            holder.tvPrice.setText(String.format("%,.0f đ", item.getGia()));
            holder.tvQty.setText(String.valueOf(item.getSoLuong()));

            holder.cbItem.setOnCheckedChangeListener(null);
            holder.cbItem.setChecked(item.isChecked());

            Glide.with(context).load(item.getDuongDanAnh())
                    .placeholder(R.drawable.ic_launcher_background).into(holder.imgItem);

            holder.cbItem.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onItemChecked(item.getMaGioHang(), holder.cbItem.isChecked());
                }
            });

            holder.btnMinus.setOnClickListener(v -> {
                if (item.getSoLuong() > 1 && actionListener != null) {
                    actionListener.onQuantityChanged(item.getMaGioHang(), item.getSoLuong() - 1);
                }
            });

            holder.btnPlus.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onQuantityChanged(item.getMaGioHang(), item.getSoLuong() + 1);
                }
            });

            holder.btnDelete.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onItemDeleted(item.getMaGioHang());
                }
            });
        }
        return row;
    }
}