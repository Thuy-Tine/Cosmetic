package com.example.mypham.adapter.admin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.mypham.R;
import com.example.mypham.model.product;
import java.util.ArrayList;

public class AdminProductAdapter extends ArrayAdapter<product> {
    private Activity context;
    private ArrayList<product> productList;
    private OnProductAdminActionListener listener;

    // Interface để truyền sự kiện ra ngoài Activity xử lý
    public interface OnProductAdminActionListener {
        void onEditClick(product sp);
        void onDeleteClick(product sp);
    }

    public AdminProductAdapter(Activity context, ArrayList<product> productList, OnProductAdminActionListener listener) {
        super(context, R.layout.activity_item_admin_product, productList);
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.activity_item_admin_product, parent, false);
        }

        product sp = productList.get(position);

        TextView tvName = row.findViewById(R.id.tvAdminProductName);
        TextView tvPrice = row.findViewById(R.id.tvAdminProductPrice);
        ImageButton btnEdit = row.findViewById(R.id.btnEditProduct);
        ImageButton btnDelete = row.findViewById(R.id.btnDeleteProduct);

        if (sp != null) {
            tvName.setText(sp.getMaSanPhamFormat() + " - " + sp.getName());
            tvPrice.setText(sp.getPrice() + " đ");

            btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(sp);
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(sp);
            });
        }
        return row;
    }
}