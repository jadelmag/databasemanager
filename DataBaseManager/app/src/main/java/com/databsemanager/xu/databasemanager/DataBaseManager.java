package com.databsemanager.xu.databasemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {
    // Mode
    private static final Boolean MODE_READABLE = true;

    // Name Table
    public static final String DB_NAME_TABLE = "contacts";

    // Fields
    public static final String CN_ID = "_id";
    public static final String CN_NAME = "name";
    public static final String CN_PHONE = "phone";

    // Create Table
    public static final String CREATE_TABLE = "create table " + DB_NAME_TABLE
            + " (" + CN_ID + " integer primary key autoincrement,"
            + CN_NAME + " text not null,"
            + CN_PHONE + " text);";

    private DataBaseHelper dHelper;
    private SQLiteDatabase db;

    // If database does not exist:
    // When getWritableDatabase() is called: it creates the database and returns it in write mode.
    // But, already it exists, returns directly.
    public DataBaseManager(Context context) {
        open(context);
    }

    public void open(Context context) {
        dHelper = new DataBaseHelper(context);
        db = dHelper.getWritableDatabase();
    }

    public void close() {
        dHelper.close();
    }

    // Data Base Methods
    public void insert(String name, String phone) {
        db.insert(DB_NAME_TABLE, null, getContentValues(name, phone));
    }

    public void remove(String _id) {
        db.delete(DB_NAME_TABLE, CN_ID + "=?", new String[]{_id});
    }

    public void modify(String _id, String newName, String newPhone) {
        db.update(DB_NAME_TABLE, getContentValues(newName, newPhone), CN_ID + "=?", new String[]{_id});
    }

    public Cursor getAllContacts() {
        // query (String Table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        String[] columns = new String[]{CN_ID, CN_NAME, CN_PHONE};
        return db.query(DB_NAME_TABLE, columns, null, null, null, null, null, null);
    }

    public Cursor getContact(String name) {
        String[] columns = new String[]{CN_ID, CN_NAME, CN_PHONE};
        return db.query(DB_NAME_TABLE, columns, CN_NAME + "=?", new String[]{name}, null, null, null, null);
    }

    // Methods
    private ContentValues getContentValues(String name, String phone) {
        ContentValues values = new ContentValues();

        values.put(CN_NAME,isNull(name));
        values.put(CN_PHONE,isNull(phone));

        return values;
    }

    private String isNull(String param) {
        if (param == null) return "Empty";
        else return param;
    }

}
