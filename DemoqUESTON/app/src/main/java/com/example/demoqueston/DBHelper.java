package com.example.demoqueston;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {
    
    public static final String IMAGE_ID = "id";
    private final Context mcontext;
    
    public static final String IMAGE = "image";
    
    private DatabaseHelper mDbHelper;
    
    private SQLiteDatabase mDB;
    
    private static final String DATABASE_NAME = "Images.db";
    
    private static final int DATABASE_VERSION =1;
     
    private static final String IMAGES_TABLE = "ImagesTable";
    
    private static final String CREATE_IMAGE_TABLE = " CREATE TABLE " + IMAGES_TABLE + " (" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + IMAGE + " BLOB NOT NULL );"; 
    
    private static final class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ IMAGES_TABLE);
            onCreate(db);
        }
       
    }

    public void Reset(){

        mDbHelper.onUpgrade(this.mDB,1,1);
    }
    
    public DBHelper(Context context){
        mcontext = context;
        mDbHelper = new DatabaseHelper(mcontext);
    }

    public DBHelper open() throws SQLException{
        mDB = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close (){
        mDbHelper.close();
    }

    public void insertImage(byte[] imageBytes){
        ContentValues cv = new ContentValues();
        cv.put(IMAGE,imageBytes);
        mDB.insert(IMAGES_TABLE,null,cv);

    }
    public byte[] retrieveImageFromDB() {
        Cursor cursor = mDB.query(true, IMAGES_TABLE, new String[]{IMAGE},
                null, null, null, null, IMAGE_ID + " DESC", "1");

        if (cursor != null && cursor.moveToFirst()) {
            int imageIndex = cursor.getColumnIndex(IMAGE);
            byte[] blob = cursor.getBlob(imageIndex);
            cursor.close();
            return blob;
        }

        // Close the cursor
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }




}
    
                

