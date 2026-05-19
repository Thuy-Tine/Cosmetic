package com.example.mypham.model;

public class DanhMuc {
    private int maDanhMuc;
    private String tenDanhMuc;

    public DanhMuc(int maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    public int getMaDanhMuc() { return maDanhMuc; }
    public String getTenDanhMuc() { return tenDanhMuc; }

    @Override
    public String toString() {
        return tenDanhMuc; // Spinner sẽ tự động hiển thị chuỗi này lên giao diện
    }
}