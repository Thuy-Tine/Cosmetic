package com.example.mypham.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {

    // ==============================
    // DATABASE
    // ==============================

    private static final String DATABASE_NAME = "COSMETIC.db";
    private static final int DATABASE_VERSION = 2;

    public databaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        // Bật FOREIGN KEY cho SQLite
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // =========================================================
        // TÀI KHOẢN
        // =========================================================

        db.execSQL(
                "CREATE TABLE TaiKhoan (" +
                        "ma_nguoi_dung INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "ten_dang_nhap TEXT UNIQUE NOT NULL, " +
                        "mat_khau_bam TEXT NOT NULL, " +

                        "vai_tro TEXT NOT NULL " +
                        "CHECK(vai_tro IN ('ADMIN','KHACH_HANG')), " +

                        "da_xac_thuc INTEGER DEFAULT 0 " +
                        "CHECK(da_xac_thuc IN (0,1)), " +

                        "ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")"
        );

        // =========================================================
        // HỒ SƠ QUẢN TRỊ VIÊN
        // =========================================================

        db.execSQL(
                "CREATE TABLE HoSoQuanTriVien (" +
                        "ma_nguoi_dung INTEGER PRIMARY KEY, " +
                        "ho_ten TEXT NOT NULL, " +
                        "so_dien_thoai TEXT, " +
                        "phong_ban TEXT, " +

                        "FOREIGN KEY(ma_nguoi_dung) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // =========================================================
        // HỒ SƠ KHÁCH HÀNG
        // =========================================================

        db.execSQL(
                "CREATE TABLE HoSoKhachHang (" +
                        "ma_nguoi_dung INTEGER PRIMARY KEY, " +
                        "ho_ten TEXT NOT NULL, " +
                        "so_dien_thoai TEXT, " +
                        "ngay_sinh TEXT, " +

                        "gioi_tinh TEXT " +
                        "CHECK(gioi_tinh IN ('NAM','NU','KHAC')), " +

                        "anh_dai_dien TEXT, " +

                        "diem_tich_luy INTEGER DEFAULT 0, " +

                        "loai_da TEXT, " +
                        "ngay_quet_da TIMESTAMP, " +

                        "FOREIGN KEY(ma_nguoi_dung) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // =========================================================
        // ĐỊA CHỈ
        // =========================================================

        db.execSQL(
                "CREATE TABLE DiaChi (" +
                        "ma_dia_chi INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ma_nguoi_dung INTEGER NOT NULL, " +

                        "ten_nguoi_nhan TEXT NOT NULL, " +
                        "so_dien_thoai TEXT NOT NULL, " +

                        "tinh_thanh TEXT NOT NULL, " +
                        "quan_huyen TEXT NOT NULL, " +
                        "phuong_xa TEXT NOT NULL, " +

                        "dia_chi_chi_tiet TEXT NOT NULL, " +

                        "mac_dinh INTEGER DEFAULT 0 " +
                        "CHECK(mac_dinh IN (0,1)), " +

                        "FOREIGN KEY(ma_nguoi_dung) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // =========================================================
        // DANH MỤC
        // =========================================================

        db.execSQL(
                "CREATE TABLE DanhMuc (" +
                        "ma_danh_muc INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ten_danh_muc TEXT NOT NULL, " +
                        "slug TEXT UNIQUE, " +

                        "anh_danh_muc TEXT, " +

                        "dang_hoat_dong INTEGER DEFAULT 1 " +
                        "CHECK(dang_hoat_dong IN (0,1)), " +

                        "tao_boi INTEGER, " +

                        "FOREIGN KEY(tao_boi) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung)" +
                        ")"
        );

        // =========================================================
        // THƯƠNG HIỆU
        // =========================================================

        db.execSQL(
                "CREATE TABLE ThuongHieu (" +
                        "ma_thuong_hieu INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ten_thuong_hieu TEXT UNIQUE NOT NULL, " +

                        "logo TEXT, " +
                        "quoc_gia TEXT, " +

                        "dang_hoat_dong INTEGER DEFAULT 1 " +
                        "CHECK(dang_hoat_dong IN (0,1))" +
                        ")"
        );

        // =========================================================
        // SẢN PHẨM
        // =========================================================

        db.execSQL(
                "CREATE TABLE SanPham (" +
                        "ma_san_pham INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ma_danh_muc INTEGER NOT NULL, " +
                        "ma_thuong_hieu INTEGER NOT NULL, " +

                        "ten_san_pham TEXT NOT NULL, " +
                        "slug TEXT UNIQUE, " +
                        "mo_ta TEXT, " +

                        "thanh_phan TEXT, " +
                        "huong_dan_su_dung TEXT, " +

                        "gia REAL NOT NULL, " +
                        "gia_nhap REAL DEFAULT 0, " +
                        "phan_tram_giam REAL DEFAULT 0, " +

                        "so_luong_ton INTEGER DEFAULT 0, " +

                        "dang_hoat_dong INTEGER DEFAULT 1 " +
                        "CHECK(dang_hoat_dong IN (0,1)), " +

                        "noi_bat INTEGER DEFAULT 0 " +
                        "CHECK(noi_bat IN (0,1)), " +

                        "FOREIGN KEY(ma_danh_muc) " +
                        "REFERENCES DanhMuc(ma_danh_muc), " +

                        "FOREIGN KEY(ma_thuong_hieu) " +
                        "REFERENCES ThuongHieu(ma_thuong_hieu)" +
                        ")"
        );

        // =========================================================
        // HÌNH ẢNH SẢN PHẨM
        // =========================================================

        db.execSQL(
                "CREATE TABLE HinhAnhSanPham (" +
                        "ma_hinh_anh INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ma_san_pham INTEGER NOT NULL, " +

                        "duong_dan_anh TEXT NOT NULL, " +
                        "thu_tu_hien_thi INTEGER DEFAULT 0, " +

                        "FOREIGN KEY(ma_san_pham) " +
                        "REFERENCES SanPham(ma_san_pham) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // =========================================================
        // LOẠI DA SẢN PHẨM
        // =========================================================

        db.execSQL(
                "CREATE TABLE LoaiDaSanPham (" +
                        "ma_san_pham INTEGER NOT NULL, " +
                        "loai_da TEXT NOT NULL, " +

                        "PRIMARY KEY(ma_san_pham, loai_da), " +

                        "FOREIGN KEY(ma_san_pham) " +
                        "REFERENCES SanPham(ma_san_pham) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // =========================================================
        // MÃ GIẢM GIÁ
        // =========================================================

        db.execSQL(
                "CREATE TABLE MaGiamGia (" +
                        "ma_giam_gia INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ma_code TEXT UNIQUE NOT NULL, " +

                        "loai_giam TEXT NOT NULL " +
                        "CHECK(loai_giam IN ('PERCENT','FIXED')), " +

                        "gia_tri REAL NOT NULL, " +
                        "don_hang_toi_thieu REAL DEFAULT 0, " +

                        "tong_so_luong INTEGER DEFAULT 0, " +
                        "da_su_dung INTEGER DEFAULT 0, " +

                        "ngay_bat_dau DATETIME, " +
                        "ngay_ket_thuc DATETIME, " +

                        "tao_boi INTEGER, " +

                        "FOREIGN KEY(tao_boi) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung)" +
                        ")"
        );

        // =========================================================
        // ĐƠN HÀNG
        // =========================================================

        db.execSQL(
                "CREATE TABLE DonHang (" +
                        "ma_don_hang INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ma_nguoi_dung INTEGER NOT NULL, " +
                        "ma_dia_chi INTEGER NOT NULL, " +
                        "ma_giam_gia INTEGER, " +

                        "tong_goc REAL NOT NULL, " +
                        "tong_giam REAL DEFAULT 0, " +
                        "tong_thanh_toan REAL NOT NULL, " +

                        "diem_su_dung INTEGER DEFAULT 0, " +
                        "giam_tu_diem REAL DEFAULT 0, " +

                        "phuong_thuc_thanh_toan TEXT, " +

                        "trang_thai_thanh_toan TEXT " +
                        "CHECK(trang_thai_thanh_toan IN ('CHO_THANH_TOAN','DA_THANH_TOAN','THAT_BAI')), " +

                        "trang_thai_don_hang TEXT " +
                        "CHECK(trang_thai_don_hang IN ('CHO_XAC_NHAN','DA_XAC_NHAN','DANG_GIAO','HOAN_THANH','DA_HUY')), " +

                        "xac_nhan_boi INTEGER, " +

                        "FOREIGN KEY(ma_nguoi_dung) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung), " +

                        "FOREIGN KEY(ma_dia_chi) " +
                        "REFERENCES DiaChi(ma_dia_chi), " +

                        "FOREIGN KEY(ma_giam_gia) " +
                        "REFERENCES MaGiamGia(ma_giam_gia), " +

                        "FOREIGN KEY(xac_nhan_boi) " +
                        "REFERENCES TaiKhoan(ma_nguoi_dung)" +
                        ")"
        );

        // =========================================================
        // CHI TIẾT ĐƠN HÀNG
        // =========================================================

        db.execSQL(
                "CREATE TABLE ChiTietDonHang (" +
                        "ma_chi_tiet INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        "ma_don_hang INTEGER NOT NULL, " +
                        "ma_san_pham INTEGER NOT NULL, " +

                        "ten_san_pham TEXT NOT NULL, " +

                        "don_gia REAL NOT NULL, " +
                        "phan_tram_giam REAL DEFAULT 0, " +

                        "so_luong INTEGER NOT NULL, " +
                        "thanh_tien REAL NOT NULL, " +

                        "FOREIGN KEY(ma_don_hang) " +
                        "REFERENCES DonHang(ma_don_hang) " +
                        "ON DELETE CASCADE, " +

                        "FOREIGN KEY(ma_san_pham) " +
                        "REFERENCES SanPham(ma_san_pham)" +
                        ")"
        );
        db.execSQL(
                "CREATE TABLE GioHang (" +
                        "ma_gio_hang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ma_san_pham INTEGER NOT NULL, " +
                        "ten_san_pham TEXT NOT NULL, " +
                        "gia REAL NOT NULL, " +
                        "duong_dan_anh TEXT, " +
                        "so_luong INTEGER NOT NULL DEFAULT 1, " +
                        "is_checked INTEGER DEFAULT 1" + // 1: Đang chọn mua, 0: Bỏ tích
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ChiTietDonHang");
        db.execSQL("DROP TABLE IF EXISTS DonHang");
        db.execSQL("DROP TABLE IF EXISTS MaGiamGia");
        db.execSQL("DROP TABLE IF EXISTS LoaiDaSanPham");
        db.execSQL("DROP TABLE IF EXISTS HinhAnhSanPham");
        db.execSQL("DROP TABLE IF EXISTS SanPham");
        db.execSQL("DROP TABLE IF EXISTS ThuongHieu");
        db.execSQL("DROP TABLE IF EXISTS DanhMuc");
        db.execSQL("DROP TABLE IF EXISTS DiaChi");
        db.execSQL("DROP TABLE IF EXISTS HoSoKhachHang");
        db.execSQL("DROP TABLE IF EXISTS HoSoQuanTriVien");
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");
        db.execSQL("DROP TABLE IF EXISTS GioHang");

        onCreate(db);
    }

    public long insertSanPham(
            int maDanhMuc,
            int maThuongHieu,
            String tenSanPham,
            String slug,
            String moTa,
            String thanhPhan,
            String huongDan,
            double gia,
            double giaNhap,
            double phanTramGiam,
            int soLuongTon,
            int noiBat
    ){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ma_danh_muc", maDanhMuc);
        values.put("ma_thuong_hieu", maThuongHieu);

        values.put("ten_san_pham", tenSanPham);
        values.put("slug", slug);
        values.put("mo_ta", moTa);

        values.put("thanh_phan", thanhPhan);
        values.put("huong_dan_su_dung", huongDan);

        values.put("gia", gia);
        values.put("gia_nhap", giaNhap);
        values.put("phan_tram_giam", phanTramGiam);

        values.put("so_luong_ton", soLuongTon);

        values.put("noi_bat", noiBat);

        long maSanPham = db.insert("SanPham", null, values);

        db.close();

        return maSanPham;
    }
}