package com.example.mypham.model;

public class ThuongHieu {
    private int maThuongHieu;
    private String tenThuongHieu;

    public ThuongHieu(int maThuongHieu, String tenThuongHieu) {
        this.maThuongHieu = maThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
    }

    public int getMaThuongHieu() { return maThuongHieu; }
    public String getTenThuongHieu() { return tenThuongHieu; }

    @Override
    public String toString() {
        return tenThuongHieu;
    }
}