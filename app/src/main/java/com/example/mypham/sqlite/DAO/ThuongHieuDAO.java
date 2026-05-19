package com.example.mypham.sqlite.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.mypham.model.ThuongHieu;
import com.example.mypham.sqlite.databaseHelper;
import java.util.ArrayList;

public class ThuongHieuDAO {
    private databaseHelper dbHelper;

    public ThuongHieuDAO(Context context) {
        dbHelper = new databaseHelper(context);
    }

    public ArrayList<ThuongHieu> getAllThuongHieu() {
        ArrayList<ThuongHieu> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ma_thuong_hieu, ten_thuong_hieu FROM ThuongHieu WHERE dang_hoat_dong = 1", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                list.add(new ThuongHieu(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}