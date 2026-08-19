package com.zybooks.cs360inventoryapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.zybooks.cs360inventoryapp.models.Item;

import java.util.ArrayList;

public class InventoryDatabaseHelper extends SQLiteOpenHelper {

    private static InventoryDatabaseHelper helper;
    public static InventoryDatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new InventoryDatabaseHelper(context);
        }
        return helper;
    }
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private class ItemTable {
        private static final String TABLE = "Inventory";
        private static final String COL_ID = "_id";
        private static final String COL_NAME = "item_name";
        private static final String COL_UNIT = "item_unit";
        private static final String COL_QUANT = "item_quantity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemTable.TABLE + " (" +
                ItemTable.COL_ID + " Integer primary key autoincrement, " +
                ItemTable.COL_NAME + " text, " +
                ItemTable.COL_UNIT + " text, " +
                ItemTable.COL_QUANT + " Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + ItemTable.TABLE);
        onCreate(db);
    }

    public void getAllItems(@NonNull ArrayList<Item> items) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + ItemTable.TABLE;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                String unit = cursor.getString(2);
                int quant = cursor.getInt(3);

                items.add(new Item(id, name, unit, quant));

            } while(cursor.moveToNext());
        }
    }

    public long add(Item item) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ItemTable.COL_NAME, item.getItemName());
        values.put(ItemTable.COL_QUANT, item.getQuantity());
        values.put(ItemTable.COL_UNIT, item.getItemUnit());

        long id = db.insert(ItemTable.TABLE, null, values);
        item.setId(id);

        return id;
    }

    public Boolean update(Item item) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ItemTable.COL_NAME, item.getItemName());
        values.put(ItemTable.COL_UNIT, item.getItemUnit());
        values.put(ItemTable.COL_QUANT, item.getQuantity());

        int rowsUpdated = db.update(ItemTable.TABLE, values, "_id = ?",
                new String[] { Long.toString(item.getId())});
        return rowsUpdated > 0;
    }

    public Boolean delete(Item item) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ItemTable.COL_NAME, item.getItemName());
        values.put(ItemTable.COL_UNIT, item.getItemUnit());
        values.put(ItemTable.COL_QUANT, item.getQuantity());

        int rowsDeleted = db.delete(ItemTable.TABLE, ItemTable.COL_ID + " = ?",
                new String[] { Long.toString(item.getId())});
        return rowsDeleted > 0;
    }
}
