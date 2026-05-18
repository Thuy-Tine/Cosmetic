package com.example.mypham.model;

public class product {
    private int maSanPham; // Đổi 'id' thành 'maSanPham' để đồng bộ với Database
    private String image;
    private String name;
    private String price;

    // 1. Constructor rỗng (Cần thiết cho một số thao tác với Framework)
    public product() {
    }

    // 2. Constructor ĐẦY ĐỦ 4 THAM SỐ (Bắt buộc dùng khi lấy dữ liệu từ DB lên)
    public product(int maSanPham, String image, String name, String price) {
        this.maSanPham = maSanPham;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    // 3. Constructor 3 tham số (Giữ lại nếu bạn có dùng ở dữ liệu mồi cũ)
    public product(String image, String name, String price) {
        this.image = image;
        this.name = name;
        this.price = price;
    }

    // ========================================================
    // CÁC HÀM LIÊN QUAN ĐẾN MÃ SẢN PHẨM
    // ========================================================

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    // Hàm tự động ghép chữ "SP" và nối thêm số 0 cho đủ 3 chữ số (VD: SP001)
    public String getMaSanPhamFormat() {
        return String.format("SP%03d", maSanPham);
    }

    // ========================================================
    // CÁC HÀM GETTER / SETTER CÒN LẠI
    // ========================================================

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}