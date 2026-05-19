package com.example.mypham.sqlite.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.mypham.sqlite.databaseHelper;

public class ThongKeDAO {
    private databaseHelper dbHelper;

    public ThongKeDAO(Context context) {
        dbHelper = new databaseHelper(context);
    }

    // 1. Tính tổng doanh thu từ các hóa đơn đã giao thành công (trang_thai = 1)
    public double getTongDoanhThu() {
        double total = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(tong_tien) FROM HoaDon WHERE trang_thai = 1", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    // 2. Đếm tổng số đơn hàng đang chờ xử lý (trang_thai = 0)
    public int getSoDonHangDangCho() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM HoaDon WHERE trang_thai = 0", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // 3. Đếm tổng số sản phẩm đang hoạt động kinh doanh (dang_hoat_dong = 1)
    public int getTongSoSanPham() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM SanPham WHERE dang_hoat_dong = 1", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}