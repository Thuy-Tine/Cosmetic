package com.example.mypham.sqlite.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mypham.model.product;
import com.example.mypham.sqlite.databaseHelper;

import java.util.ArrayList;

public class productDAO {
    private Context context;
    private databaseHelper dbHelper;

    public productDAO(Context context) {
        this.context = context;
        dbHelper = new databaseHelper(context);
    }

    public ArrayList<product> getAllProducts() {
        ArrayList<product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String sql = "SELECT sp.ten_san_pham, sp.gia, " +
                "(SELECT ha.duong_dan_anh FROM HinhAnhSanPham ha " +
                "WHERE ha.ma_san_pham = sp.ma_san_pham " +
                "ORDER BY ha.thu_tu_hien_thi ASC LIMIT 1) AS duong_dan_anh " +
                "FROM SanPham sp " +
                "WHERE sp.dang_hoat_dong = 1"; // Chỉ hiển thị sản phẩm đang bán

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                // Ánh xạ đúng tên cột từ câu truy vấn SQL
                String image = cursor.getString(cursor.getColumnIndexOrThrow("duong_dan_anh"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("ten_san_pham"));

                // Cột 'gia' trong SQLite là REAL (Double). Ép kiểu về long rồi chuyển String để mất số .0 phía sau
                double priceValue = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                String price = String.valueOf((long) priceValue);

                // Add vào list, phòng hờ sản phẩm chưa có ảnh thì truyền chuỗi rỗng để Glide không báo lỗi crash
                list.add(new product(image != null ? image : "", name, price));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close(); // Đóng kết nối để tránh rò rỉ bộ nhớ

        return list;
    }
}