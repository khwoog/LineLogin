package com.example.user.linelogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성(해당 이름의 db가 없을때만 생성)
        /* 이름은 User_info, 사용자 id, 사용자 name, 사용자 사진 이미지 uri로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE User_info (id TEXT PRIMARY KEY , name TEXT, uri TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String id, String name, String uri) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("uri", uri);
        // DB에 입력한 값으로 행 추가
        db.insert("user_info", null, values);
        db.close();
    }

    public void update(String item, int price) {}

    public void delete(String item) {}

    public List<String> getData() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<String> data = new ArrayList();

        //Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM User_info", null);
        while (cursor.moveToNext()) {
            for (int i = 0; i < 3; i++)
                data.add(cursor.getString(i));
        }
        return data;
    }
}
