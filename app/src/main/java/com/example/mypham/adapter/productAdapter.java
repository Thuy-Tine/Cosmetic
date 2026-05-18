package com.example.mypham.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton; // Bắt buộc import
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.mypham.R;
import com.example.mypham.model.product;
import java.util.ArrayList;

public class productAdapter extends ArrayAdapter<product> {

    public interface OnAddToCartListener {
        void onAddToCartClick(product sanPhamDuocChon);
    }

    private Activity context;
    private ArrayList<product> dsSP;
    private OnAddToCartListener cartListener;

    public productAdapter(Activity context, ArrayList<product> dsSP, OnAddToCartListener listener) {
        super(context, R.layout.activity_item_product, dsSP);
        this.context = context;
        this.dsSP = dsSP;
        this.cartListener = listener;
    }

    private static class ViewHolder {
        ImageView img;
        TextView txtSP;
        TextView txtGia;
        ImageButton btnAddCart;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.activity_item_product, parent, false);

            holder = new ViewHolder();
            holder.img = row.findViewById(R.id.imageProduct);
            holder.txtSP = row.findViewById(R.id.textName);
            holder.txtGia = row.findViewById(R.id.textPrice);
            holder.btnAddCart = row.findViewById(R.id.buttonAddCart); // ÁNH XẠ NÚT BẤM

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        product ds = dsSP.get(position);

        if (ds != null) {
            holder.txtSP.setText(ds.getName());
            holder.txtGia.setText(ds.getPrice() + " đ");

            Glide.with(context)
                    .load(ds.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.img);


            holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartListener != null) {
                        cartListener.onAddToCartClick(ds); // Bắn tín hiệu sang Activity
                    }
                }
            });
        }

        return row;
    }
}