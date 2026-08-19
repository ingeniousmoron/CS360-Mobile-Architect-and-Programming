package com.zybooks.cs360inventoryapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static UserDatabaseHelper helper;
    public static UserDatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new UserDatabaseHelper(context);
        }
        return helper;
    }

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private class UserTable {
        private static final String TABLE = "Users";
        private static final String COL_ID = "_id";
        private static final String COL_NAME = "user_name";
        private static final String COL_EMAIL = "user_email";
        private static final String COL_PASS = "user_password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.TABLE +  " (" +
                UserTable.COL_ID + " Integer primary key autoincrement, " +
                UserTable.COL_NAME + " text, " +
                UserTable.COL_EMAIL + " text, " +
                UserTable.COL_PASS + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserDatabaseHelper.UserTable.TABLE);
        onCreate(db);
    }

    public boolean add(String name, String email, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(UserTable.COL_NAME, name);
        values.put(UserTable.COL_EMAIL, email);
        values.put(UserTable.COL_PASS, pass);

        long newRowId = db.insert(UserTable.TABLE, null, values);

        return newRowId != -1;
    }

    public boolean login(String name, String pass) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                UserTable.COL_ID,
                UserTable.COL_NAME,
                UserTable.COL_EMAIL,
                UserTable.COL_PASS

        };

        String selection = UserTable.COL_NAME + " = ? AND " + UserTable.COL_PASS + " = ?";
        String[] selectionArgs = {name, pass};

        Cursor cursor = db.query(
                UserTable.TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            return true;
        } else {
            return false;
        }
    }
}
