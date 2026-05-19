package com.example.mypham.sqlite.DAO;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mypham.sqlite.databaseHelper;

public class TaiKhoanDAO {

    databaseHelper dbHelper;

    public TaiKhoanDAO(Context context) {
        dbHelper = new databaseHelper(context);
    }

    // =========================
    // ĐĂNG KÝ
    // =========================
    public boolean register(String email,
                            String tenDangNhap,
                            String matKhau){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("ten_dang_nhap", tenDangNhap);
        values.put("mat_khau_bam", matKhau);
        values.put("vai_tro", "KHACH_HANG");
        values.put("da_xac_thuc", 1);

        long result =
                db.insert("TaiKhoan",
                        null,
                        values);

        db.close();

        return result != -1;
    }

    // =========================
    // KIỂM TRA LOGIN
    // =========================
    public String checkLogin(String tenDangNhap, String matKhau){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String vaiTro = null;

        Cursor cursor = db.rawQuery(
                "SELECT vai_tro FROM TaiKhoan WHERE ten_dang_nhap=? AND mat_khau_bam=?",
                new String[]{tenDangNhap, matKhau}
        );

        if (cursor.moveToFirst()) {
            // Lấy dữ liệu cột vai_tro (nằm ở vị trí index 0 do câu SELECT)
            vaiTro = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return vaiTro; // Trả về "ADMIN", "KHACH_HANG" hoặc null
    }

    // =========================
    // CHECK USER TỒN TẠI
    // =========================
    public boolean isUsernameExists(String tenDangNhap){

        SQLiteDatabase db =
                dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM TaiKhoan " +
                        "WHERE ten_dang_nhap=?",
                new String[]{tenDangNhap}
        );

        boolean result =
                cursor.getCount() > 0;

        cursor.close();
        db.close();

        return result;
    }
}