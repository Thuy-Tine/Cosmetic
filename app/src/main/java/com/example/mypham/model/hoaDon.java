package com.example.mypham.model;

public class hoaDon {
    private int maHoaDon;
    private double tongTien;
    private int trangThai;
    private String phuongThucThanhToan; // THÊM BIẾN NÀY

    public hoaDon() {}

    // Cập nhật constructor
    public hoaDon(int maHoaDon, double tongTien, int trangThai, String phuongThucThanhToan) {
        this.maHoaDon = maHoaDon;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    // Thêm Getter / Setter
    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public int getMaHoaDon() { return maHoaDon; }
    public double getTongTien() { return tongTien; }
    public int getTrangThai() { return trangThai; }
    public String getMaHoaDonFormat() { return String.format("HD%04d", maHoaDon); }
}