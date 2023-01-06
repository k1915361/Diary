package com.example.diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class myDbAdapterDiary {
    myDbHelper myhelper;
    public myDbAdapterDiary(Context context){
        myhelper = new myDbHelper(context);
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

        long id = -1;
        try{
            id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        }catch(Exception e){
            Log.d(myDbHelper.TAG,""+e);
        }
        return id;
    }

    public String getData() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {
                myDbHelper.ID, myDbHelper.TITLE, myDbHelper.DATETIME ,
                myDbHelper.PAGE_FROM , myDbHelper.PAGE_TO , myDbHelper.COMMENTS ,
                myDbHelper.TEACHER_COMMENTS
        };
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            buffer.append(id+" "+title+" "+datetime+" "+pageFrom+" "+pageTo+" "+comments+" "+teacherComments+" \n");
        }
        return buffer.toString();
    }

    public int deleteById(String id)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {id};

        int count = db.delete(myDbHelper.TABLE_NAME, myDbHelper.ID+" = ?", whereArgs);
        return count;
    }

    public int updateDiary(String id, String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TITLE, title);
        contentValues.put(myDbHelper.DATETIME, datetime);
        contentValues.put(myDbHelper.PAGE_FROM, pageFrom);
        contentValues.put(myDbHelper.PAGE_TO, pageTo);
        contentValues.put(myDbHelper.COMMENTS, comments);
        contentValues.put(myDbHelper.TEACHER_COMMENTS, teacherComments);
        String[] whereArgs = {id};
        int count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.ID+" = ?", whereArgs);
        return count;
    }

    public String getById(String id)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = myDbHelper.ID + " = '%?%' ";
        String[] whereArgs = {id};
//        whereClause = "SELECT * from users WHERE ID LIKE '%"+id+"%'";
        String[] columns = {
                myDbHelper.ID, myDbHelper.TITLE, myDbHelper.DATETIME ,
                myDbHelper.PAGE_FROM , myDbHelper.PAGE_TO , myDbHelper.COMMENTS ,
                myDbHelper.TEACHER_COMMENTS
        };

        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, whereClause, whereArgs,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            buffer.append(id+" "+title+" "+datetime+" "+pageFrom+" "+pageTo+" "+comments+" "+teacherComments+" \n");
        }
        return buffer.toString();
    }

    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myDiaryDatabase";
        private static final String TABLE_NAME = "myDiaryTable";
        private static final int DATABASE_Version = 1;
        private static final String ID = "_id";
        private static final String DATETIME = "Datetime";
        private static final String TITLE = "Title";
        private static final String PAGE_FROM = "PageFrom";
        private static final String PAGE_TO = "PageTo";
        private static final String COMMENTS = "Comments";
        private static final String TEACHER_COMMENTS = "TeacherComments";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TITLE+" VARCHAR(255) ,"
                +DATETIME+" VARCHAR(225) ,"
                +PAGE_FROM+" VARCHAR(225) ,"
                +PAGE_TO+" VARCHAR(225) ,"
                +COMMENTS+" VARCHAR(225) ,"
                +TEACHER_COMMENTS+" VARCHAR(225));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private static final String TAG = "MyDbAdapterDiary";
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
