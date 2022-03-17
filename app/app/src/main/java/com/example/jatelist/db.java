package com.example.jatelist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class db extends SQLiteOpenHelper {
    private static final String users_TABLE_CREATE = "CREATE TABLE users(user TEXT PRIMARY KEY, password TEXT)";
    private static final String jatetxea_TABLE_CREATE = "CREATE TABLE jatetxea(_id INTEGER PRIMARY KEY AUTOINCREMENT, izena TEXT, ubicacion TEXT, valoracion INTERGER, comentarios TEXT, user TEXT)";
    private static final String DB_NAME = "JateList.sqlite";
    private static final int DB_VERSION = 1;
    public db(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(users_TABLE_CREATE);
        db.execSQL(jatetxea_TABLE_CREATE);


        }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertUser(String user, String password) {
        Log.i("info", "INSERT USER INTO DB");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("password", password);


        db.insert("users", null, contentValues);
        return true;
    }

    public boolean updateJatetxe(String name, String ubi, String valoracion, String comentarios,String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("izena", name);
        contentValues.put("ubicacion", ubi);
        contentValues.put("valoracion", valoracion);
        contentValues.put("comentarios", comentarios);

        db.update("jatetxea", contentValues, "izena=?  AND user =?",  new String[]{name,user});

        return true;
    }

    public boolean insertJatetxe(String name, String ubi, String valoracion, String comentarios, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("izena", name);
        contentValues.put("ubicacion", ubi);
        contentValues.put("valoracion", valoracion);
        contentValues.put("comentarios", comentarios);
        contentValues.put("user", user);

        db.insert("jatetxea", null, contentValues);
        return true;
    }

    @SuppressLint("Range")
    public ArrayList getAllCotacts(String user) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[] {user};
        Cursor res = db.rawQuery(" SELECT  * FROM jatetxea WHERE user=?", args);
        Log.i("info","GET ALL RESTAURANTS");


        ArrayList<ArrayList<String>> array_list = new ArrayList<>();

        res.moveToFirst();
        while(res.isAfterLast() == false) {
            ArrayList<String> ezaugarriak = new ArrayList<String>();
            ezaugarriak.add(res.getString(res.getColumnIndex("izena")));
            ezaugarriak.add(res.getString(res.getColumnIndex("ubicacion")));
            ezaugarriak.add(res.getString(res.getColumnIndex("valoracion")));
            ezaugarriak.add(res.getString(res.getColumnIndex("comentarios")));

            array_list.add(ezaugarriak);
            res.moveToNext();
        }
        return array_list;
    }

    public boolean checkCredentials(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[] {user};
        Cursor c = db.rawQuery(" SELECT user FROM users WHERE user=?", args);
        Log.i("info",String.valueOf(c.moveToFirst()));

        return c.moveToFirst();


    }


    public boolean deleteJatetxea(String izena,  String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("jatetxea", "izena=? and user=?", new String[]{izena,user});
        return true;
    }


}
