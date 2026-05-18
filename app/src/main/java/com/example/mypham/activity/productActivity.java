package com.example.mypham.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast; // Thêm import này
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypham.R;
import com.example.mypham.adapter.productAdapter;
import com.example.mypham.sqlite.DAO.productDAO;
import com.example.mypham.model.product;
import com.example.mypham.sqlite.databaseHelper;

import java.util.ArrayList;

public class productActivity extends AppCompatActivity {

    private GridView gvProducts;
    private ArrayList<product> productList;
    private productAdapter adapter;
    private productDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        gvProducts = findViewById(R.id.gvProducts);

        productList = new ArrayList<>();


        dao = new productDAO(this);


        createMockDataIfNeeded();


        adapter = new productAdapter(this, productList, new productAdapter.OnAddToCartListener() {
            @Override
            public void onAddToCartClick(product sanPhamDuocChon) {

                String tenSP = sanPhamDuocChon.getName();
                String giaSP = sanPhamDuocChon.getPrice();


                Toast.makeText(productActivity.this,
                        "Đã thêm " + tenSP + " (Giá: " + giaSP + "đ) vào giỏ hàng!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        gvProducts.setAdapter(adapter);


        loadData();
    }

    private void loadData() {
        productList.clear();
        productList.addAll(dao.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    private void createMockDataIfNeeded() {
        databaseHelper dbHelper = new databaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM SanPham", null);
        if (cursor.getCount() == 0) {
            ContentValues dmValues = new ContentValues();
            dmValues.put("ten_danh_muc", "Son Môi");
            dmValues.put("slug", "son-moi");
            db.insert("DanhMuc", null, dmValues);

            ContentValues thValues = new ContentValues();
            thValues.put("ten_thuong_hieu", "Dior");
            db.insert("ThuongHieu", null, thValues);

            long maSP = dbHelper.insertSanPham(
                    1, 1,
                    "Son Dior Velvet", "son-dior-velvet", "Son lì cao cấp Dior",
                    "Vitamin E", "Thoa trực tiếp lên môi",
                    850000, 500000, 10, 20, 1
            );

            if (maSP != -1) {
                ContentValues imgValues = new ContentValues();
                imgValues.put("ma_san_pham", maSP);
                imgValues.put("duong_dan_anh", "https://vn-test-11.slatic.net/p/3b5ec1e217c468e833446820ff56edff.jpg");
                imgValues.put("thu_tu_hien_thi", 1);
                db.insert("HinhAnhSanPham", null, imgValues);
            }
        }
        cursor.close();
        db.close();
    }
}