package com.example.mypham.sqlite.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.mypham.model.DanhMuc;
import com.example.mypham.sqlite.databaseHelper;
import java.util.ArrayList;

public class DanhMucDAO {
    private databaseHelper dbHelper;

    public DanhMucDAO(Context context) {
        dbHelper = new databaseHelper(context);
    }

    public ArrayList<DanhMuc> getAllDanhMuc() {
        ArrayList<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ma_danh_muc, ten_danh_muc FROM DanhMuc WHERE dang_hoat_dong = 1", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                list.add(new DanhMuc(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}