package com.example.mypham.activity.user;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher; // Import thêm thư viện này
import android.view.View;
import android.widget.EditText; // Import thêm thư viện này
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypham.R;
import com.example.mypham.adapter.user.productAdapter;
import com.example.mypham.sqlite.DAO.GioHangDAO;
import com.example.mypham.sqlite.DAO.productDAO;
import com.example.mypham.model.product;
import com.example.mypham.sqlite.databaseHelper;

import java.util.ArrayList;

public class productActivity extends AppCompatActivity {

    private GridView gvProducts;
    private EditText edtSearch;
    private ArrayList<product> productList;
    private productAdapter adapter;
    private productDAO dao;

    private GioHangDAO gioHangDAO;
    ImageButton btnCart, btnOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Ánh xạ
        gvProducts = findViewById(R.id.gvProducts);
        btnCart = findViewById(R.id.btnCart);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        edtSearch = findViewById(R.id.edtSearch);

        productList = new ArrayList<>();
        dao = new productDAO(this);
        gioHangDAO = new GioHangDAO(this);

        createMockDataIfNeeded();

        adapter = new productAdapter(this, productList, new productAdapter.OnAddToCartListener() {
            @Override
            public void onAddToCartClick(product sanPhamDuocChon) {
                int maSP_DB = sanPhamDuocChon.getMaSanPham();
                String maSP_HienThi = sanPhamDuocChon.getMaSanPhamFormat();
                String tenSP = sanPhamDuocChon.getName();
                String giaSP = sanPhamDuocChon.getPrice();
                String anhSP = sanPhamDuocChon.getImage();

                boolean success = gioHangDAO.insertToCart(maSP_DB, tenSP, giaSP, anhSP);

                if(success) {
                    Toast.makeText(productActivity.this,
                            "Đã thêm " + maSP_HienThi + " - " + tenSP + " vào giỏ hàng!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        gvProducts.setAdapter(adapter);

        loadData();
        addEvents();
        setupSearch();
    }

    // HÀM XỬ LÝ TÌM KIẾM (REAL-TIME)
    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    loadData(); // Nếu xóa hết chữ, load lại toàn bộ
                } else {
                    ArrayList<product> searchResult = dao.searchSanPham(keyword);
                    adapter.updateList(searchResult); // Cập nhật List lập tức
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
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

    private void addEvents(){
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(productActivity.this, cartActivity.class);
                startActivity(intent);
            }
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(productActivity.this, orderHistoryActivity.class);
            startActivity(intent);
        });
    }
}