package com.example.jatelist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class db extends SQLiteOpenHelper {

    /* This class manage the db */

    private static final String users_TABLE_CREATE = "CREATE TABLE users(user TEXT PRIMARY KEY, password TEXT)";
    private static final String jatetxea_TABLE_CREATE = "CREATE TABLE jatetxea(_id INTEGER PRIMARY KEY AUTOINCREMENT, izena TEXT, ubicacion TEXT, valoracion INTERGER, comentarios TEXT, tlf TEXT,user TEXT)";
    private static final String argazkiak_TABLE_CREATE = "CREATE TABLE argazkiak( identificador TEXT PRIMARY KEY not null,foto blob not null, titulo TEXT not null,  FOREIGN KEY (titutlo) REFERENCES jatetxea(_id))";
    private static final String DB_NAME = "JateList.sqlite";
    private static final int DB_VERSION = 6;

    public db(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create sql tables
        db.execSQL(users_TABLE_CREATE);
        db.execSQL(jatetxea_TABLE_CREATE);
        db.execSQL(argazkiak_TABLE_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //insert new user into users table in db
    public boolean insertUser(String user, String password) {
        Log.i("info", "INSERT USER INTO DB");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("password", password);


        db.insert("users", null, contentValues);
        return true;
    }

    //update jatetxe info in jatetxea table
    public boolean updateJatetxe(String name, String ubi, String valoracion, String comentarios, String tlf, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("izena", name);
        contentValues.put("ubicacion", ubi);
        contentValues.put("valoracion", valoracion);
        contentValues.put("comentarios", comentarios);
        contentValues.put("tlf", tlf);

        db.update("jatetxea", contentValues, "izena=?  AND user =?", new String[]{name, user});

        return true;
    }

    //insert new jatetxe into jatetxea table
    public boolean insertJatetxe(String name, String ubi, String valoracion, String comentarios, String tlf, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("izena", name);
        contentValues.put("ubicacion", ubi);
        contentValues.put("valoracion", valoracion);
        contentValues.put("comentarios", comentarios);
        contentValues.put("tlf", tlf);
        contentValues.put("user", user);

        db.insert("jatetxea", null, contentValues);
        return true;
    }

    //get all restaurants from the login user
    @SuppressLint("Range")
    public ArrayList getAllCotacts(String user) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{user};
        Cursor res = db.rawQuery(" SELECT  * FROM jatetxea WHERE user=?", args);
        Log.i("info", "GET ALL RESTAURANTS");


        ArrayList<ArrayList<String>> array_list = new ArrayList<>();

        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ArrayList<String> ezaugarriak = new ArrayList<String>();
            ezaugarriak.add(res.getString(res.getColumnIndex("izena")));
            ezaugarriak.add(res.getString(res.getColumnIndex("ubicacion")));
            ezaugarriak.add(res.getString(res.getColumnIndex("valoracion")));
            ezaugarriak.add(res.getString(res.getColumnIndex("comentarios")));
            ezaugarriak.add(res.getString(res.getColumnIndex("tlf")));
            ezaugarriak.add(res.getString(res.getColumnIndex("_id")));


            array_list.add(ezaugarriak);
            res.moveToNext();
        }
        return array_list;
    }

    //check login user credentials
    public boolean checkCredentials(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{user};
        Cursor c = db.rawQuery(" SELECT user FROM users WHERE user=?", args);
        Log.i("info", String.valueOf(c.moveToFirst()));

        return c.moveToFirst();


    }

    //remove jatetxea from the user register
    public boolean deleteJatetxea(String izena, String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("jatetxea", "izena=? and user=?", new String[]{izena, user});
        return true;
    }


    public boolean insertArgazkia(String jatetxeid, Bitmap foto) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] fototransformada = stream.toByteArray();


        SQLiteDatabase db = this.getWritableDatabase();
        String identificador = jatetxeid + 'f';
        String titulo = jatetxeid;
        String sql = "INSERT INTO argazkiak VALUES (?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, Long.parseLong(identificador));
        statement.bindBlob(2, fototransformada);
        statement.bindString(3, titulo);
        statement.executeInsert();
        return true;
    }

    public Boolean getAllimages(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{user};
        Cursor cursor = db.rawQuery(" SELECT  * FROM argazkiak WHERE user=?", args);
        Log.i("info", "GET ALL IMAGES");
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            byte[] image = cursor.getBlob(1);
            String titulo = cursor.getString(3);
            int id = cursor.getInt(2);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
            Bitmap laimg = BitmapFactory.decodeStream(imageStream);
        }
        return true;

    }

    public Bitmap getImage(String user,String jatetxeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{user,jatetxeid+'f'};
        Cursor cursor = db.rawQuery(" SELECT  * FROM argazkiak WHERE user=? and titulo=?", args);
        Log.i("info", "GET ALL IMAGES");
        cursor.moveToFirst();
        Bitmap laimg=null;
        if (cursor.getCount() > 0) {
            byte[] image = cursor.getBlob(1);
            String titulo = cursor.getString(3);
            int id = cursor.getInt(2);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
            laimg = BitmapFactory.decodeStream(imageStream);
        }
        return laimg;

    }

}
