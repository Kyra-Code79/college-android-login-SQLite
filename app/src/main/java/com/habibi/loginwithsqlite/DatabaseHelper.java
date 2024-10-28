package com.habibi.loginwithsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_STUDENTS = "students";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        // Create students table
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, course TEXT)");

        // Do not add default user here
        // logAllUsers();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // Method to add default user
    public void addDefaultUser() {
        // Ensure this is called after the database has been created
        insertUser("testUser", "testPass"); // Change these values as needed
    }

    public void logAllUsers() {
        Cursor cursor = getAllUsers();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Recycle") // Suppress cursor not closed warning
                        String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                        Log.d("DatabaseHelper", "User: " + username + ", Password: " + password);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close(); // Ensure the cursor is closed after use
            }
        }
    }

    // Insert user
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Check login credentials
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        // Log the credentials being checked
        Log.d("DatabaseHelper", "Checking login for: " + username + ", " + password + ", exists: " + exists);
        return exists;
    }

    // CRUD for students
    public boolean insertStudent(String name, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("course", course);
        long result = db.insert(TABLE_STUDENTS, null, contentValues);
        return result != -1;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, name, course FROM " + TABLE_STUDENTS, null);
    }

    public boolean updateStudent(int id, String name, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("course", course);
        long result = db.update(TABLE_STUDENTS, contentValues, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Integer deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_STUDENTS, "id=?", new String[]{String.valueOf(id)});
    }
}

