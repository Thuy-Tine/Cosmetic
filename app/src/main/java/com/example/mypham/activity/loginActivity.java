package com.example.mypham.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypham.R;
import com.example.mypham.sqlite.DAO.TaiKhoanDAO;
import com.google.android.material.textfield.TextInputEditText;

public class loginActivity extends AppCompatActivity {


//----------------------Step 1-----------------

    TextInputEditText edtTaiKhoan, edtMatKhau;
    TextView txtDangKy;
    CheckBox chkLuu;
    Button btnDangNhap;
    TaiKhoanDAO loginDAO;
    // Khai báo và khởi tạo Tên tập tin lưu trạng thái
    String preferName = "DangNhapData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        AddViews();
        AddEvents();
    }

    //-----------------------Step 2-----------------------
    private void AddViews(){
        edtTaiKhoan = findViewById(R.id.editTextTaiKhoan);
        edtMatKhau = findViewById(R.id.editTextMatKhau);
        chkLuu = findViewById(R.id.checkBoxLuu);
        btnDangNhap = findViewById(R.id.buttonDangNhap);
         loginDAO = new TaiKhoanDAO(this);
        txtDangKy = findViewById(R.id.textViewDangKy);
    }

    //----------------------Step 3-----------------------
    private void AddEvents(){
        btnDangNhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String taiKhoan = edtTaiKhoan.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();


                if(loginDAO.checkLogin(taiKhoan,matKhau)){
                    Toast.makeText(loginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                    finish();

                    //Chay qua trang HeThongActivity
                    Intent intent = new Intent(loginActivity.this, homeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(loginActivity.this, "Username or password is not right!", Toast.LENGTH_SHORT).show();

                }
            }
        });





        txtDangKy.setOnClickListener(v -> {

            Intent intent = new Intent(loginActivity.this,
                    registerActivity.class);

            startActivity(intent);

        });

    }
    //--------------------------Step 4------------------------
    //Viết hàm lưu trữ thông tin xuống file
    private void SavePreferences(){
        SharedPreferences sharePrefer = getSharedPreferences(preferName, MODE_PRIVATE );
        SharedPreferences.Editor editor = sharePrefer.edit();

        if(chkLuu.isChecked()){
            editor.putString("putUser", edtTaiKhoan.getText().toString());
            editor.putString("putPass", edtMatKhau.getText().toString());
            editor.putBoolean("putSave", chkLuu.isChecked());
        }
        else{
            editor.clear();
        }

        editor.commit();


    }
    //--------------------------Step 7--------------------
    //Goi onPause để lưu thông tin
    protected void onPause(){
        super.onPause();
        SavePreferences();
    }

    private void LoadPreferences(){
        SharedPreferences prefer = getSharedPreferences(preferName, MODE_PRIVATE);
        String user = prefer.getString("putUser","");
        String pass = prefer.getString("putPass", "");
        Boolean save = prefer.getBoolean("putSave",false);

        edtTaiKhoan.setText(user);
        edtMatKhau.setText(pass);
        chkLuu.setChecked(save);

    }

    //Goị onResume để đọc thông tin lên giao diện lại
    protected void onResume(){
        super.onResume();
        LoadPreferences();
    }

}