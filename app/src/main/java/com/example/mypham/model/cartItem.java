package com.example.mypham.model;

public class cartItem {
    private int maGioHang;
    private int maSanPham;
    private String tenSanPham;
    private double gia;
    private String duongDanAnh;
    private int soLuong;
    private boolean isChecked;

    public cartItem() {}

    public cartItem(int maGioHang, int maSanPham, String tenSanPham, double gia, String duongDanAnh, int soLuong, boolean isChecked) {
        this.maGioHang = maGioHang;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.duongDanAnh = duongDanAnh;
        this.soLuong = soLuong;
        this.isChecked = isChecked;
    }

    public int getMaGioHang() { return maGioHang; }
    public int getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public double getGia() { return gia; }
    public String getDuongDanAnh() { return duongDanAnh; }
    public int getSoLuong() { return soLuong; }
    public boolean isChecked() { return isChecked; }

    public void setChecked(boolean checked) { isChecked = checked; }
}