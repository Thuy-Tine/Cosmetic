package com.example.mypham.sqlite.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mypham.model.cartItem;
import com.example.mypham.sqlite.databaseHelper;

import java.util.ArrayList;

public class GioHangDAO {
    private databaseHelper dbHelper;

    public GioHangDAO(Context context) {
        dbHelper = new databaseHelper(context);
    }

    // 1. Lấy toàn bộ danh sách món hàng trong giỏ
    public ArrayList<cartItem> getAllCartItems() {
        ArrayList<cartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GioHang", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ma_gio_hang"));
                int masp = cursor.getInt(cursor.getColumnIndexOrThrow("ma_san_pham"));
                String ten = cursor.getString(cursor.getColumnIndexOrThrow("ten_san_pham"));
                double gia = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                String anh = cursor.getString(cursor.getColumnIndexOrThrow("duong_dan_anh"));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow("so_luong"));
                int checked = cursor.getInt(cursor.getColumnIndexOrThrow("is_checked"));

                list.add(new cartItem(id, masp, ten, gia, anh, qty, checked == 1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 2. Cập nhật số lượng sản phẩm (+ / -)
    public void updateQuantity(int maGioHang, int soLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("so_luong", soLuong);
        db.update("GioHang", v, "ma_gio_hang = ?", new String[]{String.valueOf(maGioHang)});
        db.close();
    }

    // 3. Cập nhật trạng thái tick chọn của 1 món hàng
    public void updateCheckStatus(int maGioHang, boolean isChecked) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("is_checked", isChecked ? 1 : 0);
        db.update("GioHang", v, "ma_gio_hang = ?", new String[]{String.valueOf(maGioHang)});
        db.close();
    }

    // 4. Tick chọn tất cả hoặc bỏ tick tất cả
    public void checkAll(boolean isChecked) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("is_checked", isChecked ? 1 : 0);
        db.update("GioHang", v, null, null);
        db.close();
    }

    // 5. Xóa 1 món hàng khỏi giỏ (Bấm nút thùng rác)
    public void deleteCartItem(int maGioHang) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("GioHang", "ma_gio_hang = ?", new String[]{String.valueOf(maGioHang)});
        db.close();
    }

    // 6. Thêm sản phẩm vào giỏ hàng từ trang danh sách sản phẩm
    public boolean insertToCart(int maSanPham, String tenSP, String gia, String hinhAnh) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ma_san_pham", maSanPham);
        values.put("ten_san_pham", tenSP);
        values.put("gia", gia);
        values.put("so_luong", 1); // Mặc định bấm nút (+) là thêm 1 món
        values.put("duong_dan_anh", hinhAnh);
        values.put("is_checked", 1); // Mặc định được đánh dấu tick chọn mua

        long result = db.insert("GioHang", null, values);
        db.close();

        return result != -1;
    }

    // 7. HÀM MỚI: Xóa tất cả các item đã được tick chọn (Gọi sau khi thanh toán thành công)
    public void deleteCheckedItems() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("GioHang", "is_checked = 1", null);
        db.close();
    }
}