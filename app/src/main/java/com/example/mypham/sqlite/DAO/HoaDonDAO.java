package com.example.mypham.sqlite.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.mypham.model.hoaDon;
import com.example.mypham.sqlite.databaseHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HoaDonDAO {
    private databaseHelper dbHelper;

    public HoaDonDAO(Context context) {
        dbHelper = new databaseHelper(context);
    }


    public boolean thanhToanDonHang(double tongTien, String phuongThuc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); // Bắt đầu Transaction bảo vệ dữ liệu
        try {

            String ngayLap = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            ContentValues hdValues = new ContentValues();
            hdValues.put("ngay_lap", ngayLap);
            hdValues.put("tong_tien", tongTien);
            hdValues.put("phuong_thuc_thanh_toan", phuongThuc);
            hdValues.put("trang_thai", 0);

            long maHoaDon = db.insert("HoaDon", null, hdValues);
            if (maHoaDon == -1) return false;


            String insertChiTiet = "INSERT INTO ChiTietHoaDon (ma_hoa_don, ma_san_pham, ten_san_pham, gia, so_luong, duong_dan_anh) " +
                    "SELECT " + maHoaDon + ", ma_san_pham, ten_san_pham, gia, so_luong, duong_dan_anh " +
                    "FROM GioHang WHERE is_checked = 1";
            db.execSQL(insertChiTiet);


            db.delete("GioHang", "is_checked = 1", null);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi sẽ tự động Rollback, không lưu bậy bạ
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Lấy danh sách đơn đang chờ (trang_thai = 0)
    public ArrayList<hoaDon> getAllPendingOrders() {
        ArrayList<hoaDon> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HoaDon WHERE trang_thai = 0 ORDER BY ma_hoa_don DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ma_hoa_don"));
                double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow("tong_tien"));
                int trangThai = cursor.getInt(cursor.getColumnIndexOrThrow("trang_thai"));

                // Lấy thêm phương thức thanh toán từ DB
                String phuongThuc = cursor.getString(cursor.getColumnIndexOrThrow("phuong_thuc_thanh_toan"));

                // Đưa vào Model
                list.add(new hoaDon(id, tongTien, trangThai, phuongThuc));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Cập nhật trạng thái Đã nhận hàng (Ghi doanh thu)
    public void confirmReceived(int maHoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trang_thai", 1);
        db.update("HoaDon", values, "ma_hoa_don = ?", new String[]{String.valueOf(maHoaDon)});
        db.close();
    }
    public double getDoanhThuTheoPhuongThuc(String phuongThucThanhToan) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double doanhThu = 0;

        // Chỉ cộng tiền (SUM) những đơn đã xác nhận (trang_thai = 1) và khớp phương thức
        String sql = "SELECT SUM(tong_tien) FROM HoaDon WHERE trang_thai = 1 AND phuong_thuc_thanh_toan = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{phuongThucThanhToan});

        if (cursor.moveToFirst()) {
            doanhThu = cursor.getDouble(0);
        }
        cursor.close();
        db.close();

        return doanhThu;
    }
    // Lấy chuỗi chi tiết các món hàng của 1 đơn hàng (Ví dụ: "- Son MAC (SL: 2)")
    public String getChiTietDonHangString(int maHoaDon) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder chiTiet = new StringBuilder();

        Cursor cursor = db.rawQuery("SELECT ten_san_pham, so_luong FROM ChiTietHoaDon WHERE ma_hoa_don = ?",
                new String[]{String.valueOf(maHoaDon)});

        if(cursor.moveToFirst()){
            do{
                chiTiet.append("- ")
                        .append(cursor.getString(0))
                        .append(" (SL: ")
                        .append(cursor.getInt(1))
                        .append(")\n");
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chiTiet.toString().trim(); // Xóa khoảng trắng thừa ở cuối
    }
}