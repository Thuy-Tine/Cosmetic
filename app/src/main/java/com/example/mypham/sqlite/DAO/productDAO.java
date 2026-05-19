package com.example.mypham.sqlite.DAO;

import android.content.ContentValues; // Đã thêm để phục vụ Thêm, Sửa, Xóa
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

    // ==========================================
    // CÁC HÀM XEM VÀ TÌM KIẾM DÀNH CHO KHÁCH HÀNG & ADMIN
    // ==========================================

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

    public ArrayList<product> searchSanPham(String keyword) {
        ArrayList<product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT sp.ma_san_pham, sp.ten_san_pham, sp.gia, " +
                "th.ten_thuong_hieu, ha.duong_dan_anh " +
                "FROM SanPham sp " +
                "INNER JOIN ThuongHieu th ON sp.ma_thuong_hieu = th.ma_thuong_hieu " +
                "LEFT JOIN HinhAnhSanPham ha ON sp.ma_san_pham = ha.ma_san_pham AND ha.thu_tu_hien_thi = 0 " +
                "WHERE sp.ten_san_pham LIKE ? OR th.ten_thuong_hieu LIKE ? " +
                "GROUP BY sp.ma_san_pham";

        String searchParam = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(sql, new String[]{searchParam, searchParam});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ma_san_pham"));
                String tenSP = cursor.getString(cursor.getColumnIndexOrThrow("ten_san_pham"));

                // Ép kiểu giá tiền về String để khớp với Constructor của Model
                double gia = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                String priceStr = String.valueOf((long) gia);

                int anhIndex = cursor.getColumnIndex("duong_dan_anh");
                String duongDanAnh = (anhIndex != -1 && !cursor.isNull(anhIndex)) ? cursor.getString(anhIndex) : "";

                // Gọi đúng Constructor 4 tham số: ID, Image, Name, Price
                list.add(new product(id, duongDanAnh, tenSP, priceStr));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return list;
    }

    // ==========================================
    // CÁC HÀM THÊM, SỬA, XÓA DÀNH CHO ADMIN
    // ==========================================

    // 1. THÊM SẢN PHẨM
    public boolean insertProduct(int maDanhMuc, int maThuongHieu, String tenSP, String slug, double gia, int soLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ma_danh_muc", maDanhMuc);
        values.put("ma_thuong_hieu", maThuongHieu);
        values.put("ten_san_pham", tenSP);
        values.put("slug", slug);
        values.put("gia", gia);
        values.put("so_luong_ton", soLuong);
        values.put("dang_hoat_dong", 1); // 1: Đang bán

        long result = db.insert("SanPham", null, values);
        db.close();
        return result != -1;
    }

    // 2. SỬA SẢN PHẨM
    public boolean updateProduct(int maSanPham, String tenSP, double giaMoi, int soLuongMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten_san_pham", tenSP);
        values.put("gia", giaMoi);
        values.put("so_luong_ton", soLuongMoi);

        int rowsAffected = db.update("SanPham", values, "ma_san_pham = ?", new String[]{String.valueOf(maSanPham)});
        db.close();
        return rowsAffected > 0;
    }

    // 3. XÓA SẢN PHẨM (Soft Delete - Không xóa hẳn khỏi DB để giữ lịch sử hóa đơn)
    public boolean deleteProduct(int maSanPham) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dang_hoat_dong", 0); // 0: Ngừng kinh doanh (Ẩn khỏi danh sách)

        int rowsAffected = db.update("SanPham", values, "ma_san_pham = ?", new String[]{String.valueOf(maSanPham)});
        db.close();
        return rowsAffected > 0;
    }
    // Lấy mã danh mục và mã thương hiệu của 1 sản phẩm cụ thể phục vụ chức năng SỬA
    public int[] getCategoryAndBrandIds(int maSanPham) {
        int[] ids = new int[]{-1, -1}; // [0]: ma_danh_muc, [1]: ma_thuong_hieu
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ma_danh_muc, ma_thuong_hieu FROM SanPham WHERE ma_san_pham = ?",
                new String[]{String.valueOf(maSanPham)});
        if (cursor.moveToFirst()) {
            ids[0] = cursor.getInt(0);
            ids[1] = cursor.getInt(1);
        }
        cursor.close();
        db.close();
        return ids;
    }
}