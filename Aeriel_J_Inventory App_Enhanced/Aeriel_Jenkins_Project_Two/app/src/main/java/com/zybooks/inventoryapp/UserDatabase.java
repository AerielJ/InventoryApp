package com.zybooks.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


public class UserDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "user.db";

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create users table
        db.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.COL_USERNAME + " primary key, " +
                UserTable.COL_PASSWORD + " String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }

    // Function to add user to user database
    public boolean addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, user.getUsername());
        values.put(UserTable.COL_PASSWORD, user.getPassword());
        long id = db.insert(UserTable.TABLE, null, values);
        db.close();
        return id != -1;
    }

    // Function to check user database for username and password
    public boolean checkUser(User user) {
        SQLiteDatabase db = getReadableDatabase();

        String username = user.getUsername();
        String password = user.getPassword();

        String[] selectionArgs = {username, password};
        String sql = "select * from " + UserTable.TABLE + " where username = ? and password = ?";

        Cursor cursor = db.rawQuery(sql, selectionArgs);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
