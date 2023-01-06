package com.example.diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class myDbAdapter {
    myDbHelper myhelper;
    public myDbAdapter(Context context){
        myhelper = new myDbHelper(context);
    }

    public long insertData_Example(String name, String platform){
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.MyPLATFORM, platform);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertData(String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments){
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TITLE, title);
        contentValues.put(myDbHelper.DATETIME, datetime);
        contentValues.put(myDbHelper.PAGE_FROM, pageFrom);
        contentValues.put(myDbHelper.PAGE_TO, pageTo);
        contentValues.put(myDbHelper.COMMENTS, comments);
        contentValues.put(myDbHelper.TEACHER_COMMENTS, teacherComments);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public String getData_Example() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME, myDbHelper.MyPLATFORM};

        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext())
        {
            int cid = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.UID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.NAME));
            String platform = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.MyPLATFORM));
            buffer.append(cid+" "+name+" "+platform+" \n");
        }
        return buffer.toString();
    }

    public String getData() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.TITLE,myDbHelper.DATETIME ,myDbHelper.PAGE_FROM ,myDbHelper.PAGE_TO ,myDbHelper.COMMENTS ,myDbHelper.TEACHER_COMMENTS ,myDbHelper.TABLE_NAME};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext())
        {
            int cid = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.UID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            buffer.append(cid+" "+title+" "+datetime+" "+pageFrom+" "+pageTo+" "+comments+" "+teacherComments+" \n");
        }
        return buffer.toString();
    }

    public int delete_Example(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {uname};

        int count = db.delete(myDbHelper.TABLE_NAME, myDbHelper.NAME+" = ?", whereArgs);
        return count;
    }

    public int delete(int cid)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {""+cid};

        int count = db.delete(myDbHelper.TABLE_NAME, myDbHelper.UID+" = ?", whereArgs);
        return count;
    }

    public int updateName_Example(String oldName, String newName)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, newName);
        String[] whereArgs = {oldName};
        int count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.NAME+" = ?", whereArgs);
        return count;
    }

    public int updateDiary(int id, String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TITLE, title);
        contentValues.put(myDbHelper.DATETIME, datetime);
        contentValues.put(myDbHelper.PAGE_FROM, pageFrom);
        contentValues.put(myDbHelper.PAGE_TO, pageTo);
        contentValues.put(myDbHelper.COMMENTS, comments);
        contentValues.put(myDbHelper.TEACHER_COMMENTS, teacherComments);
        String[] whereArgs = {""+id};
        int count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.UID+" = ?", whereArgs);
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myDatabase";
        private static final String TABLE_NAME = "myTable";
        private static final int DATABASE_Version = 1;
        private static final String UID = "_id";
        private static final String DATETIME = "Datetime";
        private static final String TITLE = "Title";
        private static final String PAGE_FROM = "PageFrom";
        private static final String PAGE_TO = "PageTo";
        private static final String COMMENTS = "Comments";
        private static final String TEACHER_COMMENTS = "TeacherComments";
        private static final String NAME = "Name";
        private static final String MyPLATFORM = "Platform";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TITLE+" VARCHAR(255) ,"
                +DATETIME+" VARCHAR(225) ,"
                +PAGE_FROM+" VARCHAR(225) ,"
                +PAGE_TO+" VARCHAR(225) ,"
                +COMMENTS+" VARCHAR(225) ,"
                +TEACHER_COMMENTS+" VARCHAR(225));"
                ;
        private static final String CREATE_TABLE_EXAMPLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ MyPLATFORM+" VARCHAR(225));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context){
                super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Message.message(context, ""+e);
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Message.message(context, "OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch(Exception e){
                Message.message(context, ""+e);
            }
        }
    }

    public byte[] toByte(Diary diary) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(diary);
            byte[] diaryAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(diaryAsBytes);
            return diaryAsBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Diary byteToDiary(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            Diary dataobj = (Diary) ois.readObject();
            return dataobj ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
