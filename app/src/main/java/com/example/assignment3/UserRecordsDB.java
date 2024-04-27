package com.example.assignment3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
public class UserRecordsDB {
    private final String DATABASE_NAME = "UserRecordsDB";
    private final String DATABASE_TABLE = "Users_Table";
    private final String KEY_ID = "_id";
    private final String KEY_NAME = "_name";
    private final String KEY_PASS = "_password";

    private final int DATABASE_VERSION = 1;

    Context context;

    MyDatabaseHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public UserRecordsDB(Context c)
    {
        context = c;
    }

    public void open()
    {
        helper = new MyDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insert(String name, String password)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PASS, password);

        long temp = sqLiteDatabase.insert(DATABASE_TABLE, null, cv);
        if(temp == -1)
        {
            Toast.makeText(context, "User not added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteContact(int id)
    {
        int rows = sqLiteDatabase.delete(DATABASE_TABLE, KEY_ID+"=?", new String[]{String.valueOf(id)});
        if(rows > 0)
        {
            Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "User not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<UserRecord> readAllContacts()
    {
        Cursor c =  sqLiteDatabase.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);
        ArrayList<UserRecord> Users = new ArrayList<>();
        int id_index = c.getColumnIndex(KEY_ID);
        int id_name = c.getColumnIndex(KEY_NAME);
        int id_phone = c.getColumnIndex(KEY_PASS);

        if(c.moveToFirst())
        {
            do{
                UserRecord userRecord = new UserRecord();
                userRecord.setId(c.getInt(id_index));
                userRecord.setUsername(c.getString(id_name));
                userRecord.setPassword(c.getString(id_phone));

                Users.add(userRecord);
            }while(c.moveToNext());
        }

        c.close();
        return Users;
    }
    public void close()
    {
        sqLiteDatabase.close();
        helper.close();
    }

    public void updateContact(int id, String newUserName, String newPassword) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, newUserName);
        cv.put(KEY_PASS, newPassword);

        int rows = sqLiteDatabase.update(DATABASE_TABLE, cv, KEY_ID+"=?", new String[]{id+""});
        if(rows>0)
        {
            Toast.makeText(context, "User Record updated successfully", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Failed to update User Record", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username});
        boolean returnvalue=false;
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);

            if (count > 0) {
                returnvalue = true;
                //Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                // Additional actions can be added, like changing the border color of EditText or displaying an error TextView.
            }
            else
                returnvalue = false;
        }


        cursor.close();
        return returnvalue;
    }
    public boolean checkUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + " = ? AND " + KEY_PASS + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username, password});

        boolean validCredentials = false;
        if (cursor.moveToFirst()) {
            validCredentials = true;  // If there's a record with the matching username and password, the credentials are valid
        }

        cursor.close();  // Always close the cursor to avoid memory leaks
        return validCredentials;
    }


    private class MyDatabaseHelper extends SQLiteOpenHelper
    {
        public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+DATABASE_TABLE+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NAME+" TEXT NOT NULL, "+KEY_PASS+" TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }

}
