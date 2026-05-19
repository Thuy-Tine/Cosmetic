package com.example.mypham.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mypham.R;
import com.example.mypham.model.DanhMuc;
import com.example.mypham.model.ThuongHieu;
import com.example.mypham.sqlite.DAO.DanhMucDAO;
import com.example.mypham.sqlite.DAO.ThuongHieuDAO;
import com.example.mypham.sqlite.DAO.productDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class AddEditProductActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private Spinner spnCategory, spnBrand;
    private TextInputEditText edtName, edtPrice, edtQuantity;
    private MaterialButton btnSave;

    private productDAO dao;
    private DanhMucDAO danhMucDAO;
    private ThuongHieuDAO thuongHieuDAO;

    private ArrayList<DanhMuc> listDanhMuc;
    private ArrayList<ThuongHieu> listThuongHieu;

    private int currentProductId = -1; // -1: Thêm mới, số khác: Sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        // 1. Ánh xạ View
        btnBack = findViewById(R.id.btnBackForm);
        tvTitle = findViewById(R.id.tvFormTitle);
        spnCategory = findViewById(R.id.spnCategory);
        spnBrand = findViewById(R.id.spnBrand);
        edtName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtProductPrice);
        edtQuantity = findViewById(R.id.edtProductQuantity);
        btnSave = findViewById(R.id.btnSaveProduct);

        // 2. Khởi tạo DAO
        dao = new productDAO(this);
        danhMucDAO = new DanhMucDAO(this);
        thuongHieuDAO = new ThuongHieuDAO(this);

        // 3. Đổ dữ liệu động lên các Spinner từ Database
        loadSpinnersData();

        // 4. Kiểm tra luồng Thêm hay Sửa dựa vào Intent gửi đến
        Intent intent = getIntent();
        if (intent.hasExtra("ma_san_pham")) {
            currentProductId = intent.getIntExtra("ma_san_pham", -1);
            tvTitle.setText("Cập Nhật Sản Phẩm");
            edtName.setText(intent.getStringExtra("ten_san_pham"));
            edtPrice.setText(intent.getStringExtra("gia"));

            // Tìm và đặt vị trí chính xác cho Spinner tương ứng với sản phẩm đang sửa
            setSelectionForSpinners();
        }

        addEvents();
    }

    private void loadSpinnersData() {
        // Lấy danh sách từ CSDL
        listDanhMuc = danhMucDAO.getAllDanhMuc();
        listThuongHieu = thuongHieuDAO.getAllThuongHieu();

        // Gán adapter hiển thị
        ArrayAdapter<DanhMuc> adapterDM = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDanhMuc);
        adapterDM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapterDM);

        ArrayAdapter<ThuongHieu> adapterTH = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listThuongHieu);
        adapterTH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBrand.setAdapter(adapterTH);
    }

    private void setSelectionForSpinners() {
        int[] currentIds = dao.getCategoryAndBrandIds(currentProductId);
        int targetCatId = currentIds[0];
        int targetBrandId = currentIds[1];

        // Khớp mã Danh mục
        for (int i = 0; i < listDanhMuc.size(); i++) {
            if (listDanhMuc.get(i).getMaDanhMuc() == targetCatId) {
                spnCategory.setSelection(i);
                break;
            }
        }

        // Khớp mã Thương hiệu
        for (int i = 0; i < listThuongHieu.size(); i++) {
            if (listThuongHieu.get(i).getMaThuongHieu() == targetBrandId) {
                spnBrand.setSelection(i);
                break;
            }
        }
    }

    private void addEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String qtyStr = edtQuantity.getText().toString().trim();

            // Kiểm tra ràng buộc chọn danh mục/thương hiệu từ DB
            if (spnCategory.getSelectedItem() == null || spnBrand.getSelectedItem() == null) {
                Toast.makeText(this, "Chưa có dữ liệu danh mục hoặc thương hiệu trong hệ thống!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy ID ĐỘNG hoàn toàn từ đối tượng đang được chọn trên Spinner
            DanhMuc selectedDM = (DanhMuc) spnCategory.getSelectedItem();
            ThuongHieu selectedTH = (ThuongHieu) spnBrand.getSelectedItem();

            int maDanhMuc = selectedDM.getMaDanhMuc();
            int maThuongHieu = selectedTH.getMaThuongHieu();

            double price = Double.parseDouble(priceStr);
            int qty = Integer.parseInt(qtyStr);

            boolean success;
            if (currentProductId == -1) {
                // THÊM MỚI (Lấy chuẩn 100% ID động từ CSDL, không set cứng)
                success = dao.insertProduct(maDanhMuc, maThuongHieu, name, "slug-tu-dong", price, qty);
                if (success) Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                // SỬA
                success = dao.updateProduct(currentProductId, name, price, qty);
                if (success) Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            }

            if (success) {
                finish();
            } else {
                Toast.makeText(this, "Thao tác thất bại, kiểm tra lại dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}