package com.example.doublejk.project3;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-07-21.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurantDB";
    private static final int DATABASE_VERSION = 1;
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //DB 생성할때 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
/*        db.execSQL("CREATE TABLE restaurant(name TEXT PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, address TEXT, number TEXT, content TEXT);");*/
        db.execSQL("CREATE TABLE restaurant(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, address TEXT, number TEXT, content TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS restaurant");
    }

    public void insert(Restaurant restaurant) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO restaurant VALUES(null, '" + restaurant.getTitle() + "', '" +
                restaurant.getAddress() + "', '" + restaurant.getNumber() + "', '" +
                restaurant.getContent() + "');");
    }

    public void onDrop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS restaurant");
        onCreate(db);
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM restaurant WHERE id ='" + id + "';");
        db.close();
    }

    public void select() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM restaurant", null);
        String result = " ";
        while(cursor.moveToNext()) {
            result += cursor.getString(0) + " " + cursor.getString(1) + " " +
                    cursor.getString(2) + " " + cursor.getString(3) + " " +
                    cursor.getString(4) + "\n";
        }
        cursor.close();
        db.close();
    }

    public ArrayList<Restaurant> getRestaurants(ArrayList<Restaurant> restaurants) {
        restaurants = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM restaurant", null);
        while(cursor.moveToNext()) {
            restaurants.add(new Restaurant(cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)));
        }
        db.close();
        return restaurants;
    }
}
