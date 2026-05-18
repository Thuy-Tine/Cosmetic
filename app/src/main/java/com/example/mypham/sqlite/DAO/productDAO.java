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


        String sql = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, " +
                "(SELECT ha.duong_dan_anh FROM HinhAnhSanPham ha " +
                "WHERE ha.ma_san_pham = sp.ma_san_pham " +
                "ORDER BY ha.thu_tu_hien_thi ASC LIMIT 1) AS duong_dan_anh " +
                "FROM SanPham sp " +
                "WHERE sp.dang_hoat_dong = 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                // 2. ÁNH XẠ THÊM CỘT ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ma_san_pham"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("duong_dan_anh"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("ten_san_pham"));

                double priceValue = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                String price = String.valueOf((long) priceValue);

                // 3. SỬ DỤNG CONSTRUCTOR 4 THAM SỐ ĐỂ ĐẨY ID VÀO MODEL
                list.add(new product(id, image != null ? image : "", name, price));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}