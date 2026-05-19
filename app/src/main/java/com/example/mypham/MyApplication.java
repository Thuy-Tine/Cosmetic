package com.example.mypham;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // KHÓA CHẶT LUỒNG: Ép buộc toàn bộ ứng dụng chỉ sử dụng chế độ Sáng (Light Mode)
        // Bất chấp điện thoại của người dùng đang bật Dark Mode hệ thống
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}