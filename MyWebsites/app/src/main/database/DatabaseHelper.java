package com.yourapp.mywebsites.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.yourapp.mywebsites.utils.EncryptionUtils;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "websites.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table names
    private static final String TABLE_WEBSITES = "websites";
    private static final String TABLE_PASSCODE = "passcode";
    
    // Website columns
    private static final String COL_ID = "id";
    private static final String COL_WEBSITE_NAME = "website_name";
    private static final String COL_WEBSITE_URL = "website_url";
    private static final String COL_ADMIN_URL = "admin_url";
    private static final String COL_ADMIN_USERNAME = "admin_username";
    private static final String COL_ADMIN_EMAIL = "admin_email";
    private static final String COL_ADMIN_PASSWORD = "admin_password";
    private static final String COL_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Websites table
        String createWebsitesTable = "CREATE TABLE " + TABLE_WEBSITES + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_WEBSITE_NAME + " TEXT UNIQUE,"
                + COL_WEBSITE_URL + " TEXT,"
                + COL_ADMIN_URL + " TEXT,"
                + COL_ADMIN_USERNAME + " TEXT,"
                + COL_ADMIN_EMAIL + " TEXT,"
                + COL_ADMIN_PASSWORD + " TEXT,"
                + COL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(createWebsitesTable);

        // Passcode table (only one row)
        String createPasscodeTable = "CREATE TABLE " + TABLE_PASSCODE + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + "passcode TEXT" + ")";
        db.execSQL(createPasscodeTable);
        
        // Insert default passcode (encrypted)
        ContentValues values = new ContentValues();
        values.put(COL_ID, 1);
        values.put("passcode", EncryptionUtils.encrypt("334863"));
        db.insert(TABLE_PASSCODE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEBSITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSCODE);
        onCreate(db);
    }

    // Insert website with encryption
    public boolean insertWebsite(WebsiteModel website) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WEBSITE_NAME, website.getWebsiteName());
        values.put(COL_WEBSITE_URL, website.getWebsiteUrl());
        values.put(COL_ADMIN_URL, website.getAdminUrl());
        values.put(COL_ADMIN_USERNAME, EncryptionUtils.encrypt(website.getAdminUsername()));
        values.put(COL_ADMIN_EMAIL, EncryptionUtils.encrypt(website.getAdminEmail()));
        values.put(COL_ADMIN_PASSWORD, EncryptionUtils.encrypt(website.getAdminPassword()));
        
        long result = db.insert(TABLE_WEBSITES, null, values);
        db.close();
        return result != -1;
    }

    // Get all websites
    public List<WebsiteModel> getAllWebsites() {
        List<WebsiteModel> websiteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WEBSITES, null, null, null, null, null, COL_CREATED_AT + " DESC");
        
        if (cursor.moveToFirst()) {
            do {
                WebsiteModel website = new WebsiteModel();
                website.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                website.setWebsiteName(cursor.getString(cursor.getColumnIndex(COL_WEBSITE_NAME)));
                website.setWebsiteUrl(cursor.getString(cursor.getColumnIndex(COL_WEBSITE_URL)));
                website.setAdminUrl(cursor.getString(cursor.getColumnIndex(COL_ADMIN_URL)));
                website.setAdminUsername(EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_USERNAME))));
                website.setAdminEmail(EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_EMAIL))));
                website.setAdminPassword(EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_PASSWORD))));
                websiteList.add(website);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return websiteList;
    }

    // Search websites
    public List<WebsiteModel> searchWebsites(String query) {
        List<WebsiteModel> websiteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_WEBSITE_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        Cursor cursor = db.query(TABLE_WEBSITES, null, selection, selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                WebsiteModel website = new WebsiteModel();
                website.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                website.setWebsiteName(cursor.getString(cursor.getColumnIndex(COL_WEBSITE_NAME)));
                website.setWebsiteUrl(cursor.getString(cursor.getColumnIndex(COL_WEBSITE_URL)));
                website.setAdminUrl(cursor.getString(cursor.getColumnIndex(COL_ADMIN_URL)));
                website.setAdminUsername(EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_USERNAME))));
                website.setAdminEmail(EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_EMAIL))));
                website.setAdminPassword(EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex(COL_ADMIN_PASSWORD))));
                websiteList.add(website);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return websiteList;
    }

    // Update website
    public boolean updateWebsite(WebsiteModel website) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WEBSITE_NAME, website.getWebsiteName());
        values.put(COL_WEBSITE_URL, website.getWebsiteUrl());
        values.put(COL_ADMIN_URL, website.getAdminUrl());
        values.put(COL_ADMIN_USERNAME, EncryptionUtils.encrypt(website.getAdminUsername()));
        values.put(COL_ADMIN_EMAIL, EncryptionUtils.encrypt(website.getAdminEmail()));
        values.put(COL_ADMIN_PASSWORD, EncryptionUtils.encrypt(website.getAdminPassword()));
        
        int result = db.update(TABLE_WEBSITES, values, COL_ID + "=?", new String[]{String.valueOf(website.getId())});
        db.close();
        return result > 0;
    }

    // Delete website
    public boolean deleteWebsite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_WEBSITES, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Get passcode
    public String getPasscode() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PASSCODE, null, COL_ID + "=?", new String[]{"1"}, null, null, null);
        String passcode = "";
        if (cursor.moveToFirst()) {
            passcode = EncryptionUtils.decrypt(cursor.getString(cursor.getColumnIndex("passcode")));
        }
        cursor.close();
        db.close();
        return passcode;
    }

    // Update passcode
    public boolean updatePasscode(String newPasscode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("passcode", EncryptionUtils.encrypt(newPasscode));
        int result = db.update(TABLE_PASSCODE, values, COL_ID + "=?", new String[]{"1"});
        db.close();
        return result > 0;
    }
}