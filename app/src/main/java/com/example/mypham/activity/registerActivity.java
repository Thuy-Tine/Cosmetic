package com.example.mypham.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypham.R;
import com.example.mypham.sqlite.DAO.TaiKhoanDAO;

public class registerActivity extends AppCompatActivity {


    Button buttonDangKy;

    EditText editTextEmail,
            editTextTaiKhoan,
            editTextMatKhau,
            editTextXacNhanMatKhau;

    TaiKhoanDAO taiKhoanDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        editTextEmail =
                findViewById(R.id.editTextEmail);

        editTextTaiKhoan =
                findViewById(R.id.editTextTaiKhoan);

        editTextMatKhau =
                findViewById(R.id.editTextMatKhau);

        editTextXacNhanMatKhau =
                findViewById(R.id.editTextXacNhanMatKhau);

        buttonDangKy =
                findViewById(R.id.buttonDangKy);
        taiKhoanDAO = new TaiKhoanDAO(this);

        buttonDangKy.setOnClickListener(v -> {

            String email =
                    editTextEmail.getText().toString().trim();

            String taiKhoan =
                    editTextTaiKhoan.getText().toString().trim();

            String matKhau =
                    editTextMatKhau.getText().toString().trim();

            String xacNhan =
                    editTextXacNhanMatKhau
                            .getText()
                            .toString()
                            .trim();

            // Check rỗng
            if(email.isEmpty()
                    || taiKhoan.isEmpty()
                    || matKhau.isEmpty()
                    || xacNhan.isEmpty()){

                Toast.makeText(this,
                        "Vui lòng nhập đầy đủ",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // Check confirm password
            if(!matKhau.equals(xacNhan)){

                Toast.makeText(this,
                        "Mật khẩu không khớp",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // Check username tồn tại
            if(taiKhoanDAO.isUsernameExists(taiKhoan)){

                Toast.makeText(this,
                        "Tài khoản đã tồn tại",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // Register
            boolean check =
                    taiKhoanDAO.register(
                            email,
                            taiKhoan,
                            matKhau
                    );

            if(check){

                Toast.makeText(this,
                        "Đăng ký thành công",
                        Toast.LENGTH_SHORT).show();

                finish();

            }else{

                Toast.makeText(this,
                        "Đăng ký thất bại",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}