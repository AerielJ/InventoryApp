package com.zybooks.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;


public class ItemDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "item.db";

    public enum ItemSortOrder { ALPHABETIC, UPDATE_DESC, UPDATE_ASC }

    public ItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class ItemTable {
        private static final String TABLE = "Items";
        private static final String COL_NAME = "name";
        private static final String COL_QUANTITY = "quantity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create item table
        db.execSQL("create table " + ItemDatabase.ItemTable.TABLE + " (" +
                ItemDatabase.ItemTable.COL_NAME + " primary key, " +
                ItemDatabase.ItemTable.COL_QUANTITY + " String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ItemDatabase.ItemTable.TABLE);
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

    // Function to get list of inventory items
    public ArrayList<Item> getItems(ItemSortOrder order) {
        ArrayList<Item> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy;
        switch (order) {
            case ALPHABETIC:
                orderBy = ItemTable.COL_NAME + " collate nocase";
                break;
            case UPDATE_DESC:
                orderBy = ItemTable.COL_QUANTITY + " desc";
                break;
            default:
                orderBy = ItemTable.COL_QUANTITY + " asc";
                break;
        }

        String sql = "select * from " + ItemTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int quantity = cursor.getInt(1);
                items.add(new Item(name, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return items;
    }

    // Function to add item to Item database
    public boolean addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemDatabase.ItemTable.COL_NAME, item.getName());
        values.put(ItemDatabase.ItemTable.COL_QUANTITY, item.getQuantity());
        long id = db.insert(ItemDatabase.ItemTable.TABLE, null, values);
        db.close();
        return id != -1;
    }

    // Function to update item in Item database
    public void updateItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemTable.COL_NAME, item.getName());
        values.put(ItemTable.COL_QUANTITY, item.getQuantity());
        db.update(ItemTable.TABLE, values,
                ItemTable.COL_NAME + " = ?", new String[] { item.getName() });
        db.close();
    }

    // Function to delete item from Item database
    public void deleteItem(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ItemTable.TABLE,
                ItemTable.COL_NAME + " = ?", new String[] { name });
        db.close();
    }

}
