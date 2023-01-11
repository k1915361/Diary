package com.example.diary;

import static com.example.diary.Helper.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DiaryDbAdapter {
    myDbHelper myhelper;
    public DiaryDbAdapter(Context context){
        myhelper = new myDbHelper(context);
    }

    public long insertData(String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments){
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = putAllCols(title, datetime, pageFrom, pageTo, comments, teacherComments);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ContentValues putAllCols(String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments){
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TITLE, title);
        contentValues.put(myDbHelper.DATETIME, datetime);
        contentValues.put(myDbHelper.PAGE_FROM, pageFrom);
        contentValues.put(myDbHelper.PAGE_TO, pageTo);
        contentValues.put(myDbHelper.COMMENTS, comments);
        contentValues.put(myDbHelper.TEACHER_COMMENTS, teacherComments);
        return contentValues;
    }

    public Cursor getDataCursor() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {
                myDbHelper.ID, myDbHelper.TITLE, myDbHelper.DATETIME ,
                myDbHelper.PAGE_FROM , myDbHelper.PAGE_TO , myDbHelper.COMMENTS ,
                myDbHelper.TEACHER_COMMENTS
        };
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null,null,null,null);
        return cursor;
    }

    public String getData() {
        Cursor cursor = getDataCursor();
        return cursorToStr(cursor);
    }

    public String getDataInFormat() {
        Cursor cursor = getDataCursor();
        return cursorToCsv(cursor);
    }

    public String getDataOfficialFormat() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {
                myDbHelper.ID, myDbHelper.TITLE, myDbHelper.DATETIME ,
                myDbHelper.PAGE_FROM , myDbHelper.PAGE_TO , myDbHelper.COMMENTS ,
                myDbHelper.TEACHER_COMMENTS
        };
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null,null,null,null);
        return cursorToCsv(cursor);
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
        ContentValues contentValues = putAllCols(title, datetime, pageFrom, pageTo, comments, teacherComments);
        String[] whereArgs = {id};
        int count = 0;
        try {
             count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.ID + " = ?", whereArgs);
        }catch(Exception e){
            Log.d(myDbHelper.TAG, ""+e);
        }
        return count;
    }

    public Cursor getCursor_getById(String id)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = myDbHelper.ID + " = ?";
        String[] whereArgs = {id};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        return cursor;
    }

    public String getById(String id)
    {
        Cursor cursor = getCursor_getById(id);
        return cursorToStr(cursor);
    }

    public String getByIdInFormat(String id)
    {
        Cursor cursor = getCursor_getById(id);
        return cursorToCsv(cursor);
    }

    public Cursor cursorSearchAny(String srch){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause =
                myDbHelper.ID + " = ? OR "
                        + myDbHelper.TITLE + " LIKE ? OR "
                        + myDbHelper.DATETIME + " LIKE ? OR "
                        + myDbHelper.PAGE_FROM + " LIKE ? OR "
                        + myDbHelper.PAGE_TO + " LIKE ? OR "
                        + myDbHelper.COMMENTS + " LIKE ? OR "
                        + myDbHelper.TEACHER_COMMENTS + " LIKE ? "
                ;
        String[] whereArgs = {srch, srch, srch, srch, srch, srch, srch };
        for (int i=1; i<whereArgs.length-1; i++){
            whereArgs[i] = '%'+whereArgs[i]+'%';
        }
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        return cursor;
    }

    public String searchAny(String srch){
        Cursor cursor = cursorSearchAny(srch);
        return cursorToStr(cursor);
    }

    public String searchAnyInFormat(String srch){
        Cursor cursor = cursorSearchAny(srch);
        return cursorToCsv(cursor);
    }

    public String searchAll (String id, String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause =
            (id.isEmpty() ? "" : myDbHelper.ID + " = ? AND ")
            + (title.isEmpty() ? "" : myDbHelper.TITLE + " LIKE ? AND ")
            + (datetime.isEmpty() ? "" : myDbHelper.DATETIME + " LIKE ? AND ")
            + (pageFrom.isEmpty() ? "" : myDbHelper.PAGE_FROM + " LIKE ? AND ")
            + (pageTo.isEmpty() ? "" : myDbHelper.PAGE_TO + " LIKE ? AND ")
            + (comments.isEmpty() ? "" : myDbHelper.COMMENTS + " LIKE ? AND ")
            + (teacherComments.isEmpty() ? "" : myDbHelper.TEACHER_COMMENTS + " LIKE ? ;");
        String[] whereArgs = {id, title, datetime, pageFrom, pageTo, comments, teacherComments };
        for (int i=1; i<whereArgs.length-1; i++){
            whereArgs[i] = '%'+whereArgs[i]+'%';
        }
        StringBuffer buffer = new StringBuffer();
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        return cursorToStr(cursor);
    }

    public Diary getDiaryById (String id){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = myDbHelper.ID + " = ?";
        String[] whereArgs = {id};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        Diary diary = null;
        while(cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            diary = new Diary(id,title,datetime,pageFrom,pageTo,comments,teacherComments);
        }
        return diary;
    }

    public Cursor selectByPageRange_Cursor(String str){
        String[] whereArgs = str.split(" ");
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = new StringBuilder()
                .append(myDbHelper.PAGE_FROM).append(" = ? AND ")
                .append(myDbHelper.PAGE_TO).append(" = ? ").toString();
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        return cursor;
    }

    public String selectByPageRange(String str){
        String[] whereArgs = str.split(" ");
        if(whereArgs.length <= 1 || !isNumeric(whereArgs[0]) || !isNumeric(whereArgs[1])) return "";
        Cursor cursor = selectByPageRange_Cursor(str);
        return cursorToStr(cursor);
    }

    public Cursor selectByIds_Cursor(String str){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = myDbHelper.ID + " IN (? ";
        String[] whereArgs = {};
        whereArgs = str.split(",",0);
        whereClause += repeat(whereArgs.length-1, ",?");
        if(str != null) {}
        whereClause += ")";
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        return cursor;
    }

    public String selectByIds(String str){
        Cursor cursor = selectByIds_Cursor(str);
        return cursorToStr(cursor);
    }

    public String getCsvByIds(String str){
        Cursor cursor = selectByIds_Cursor(str);
        return cursorToCsv(cursor);
    }

    public String getJsonByIds(String str){
        Cursor cursor = selectByIds_Cursor(str);
        return cursorToJson(cursor);
    }

    public String testPlayground (String str){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String whereClause = myDbHelper.ID + " IN (? ";
        String[] whereArgs = {};
        whereArgs = str.split(",",0);
        whereClause += repeat(whereArgs.length-1, ",?");
        if(str != null) {}
        whereClause += ")";
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, myDbHelper.ALL_COLUMNS, whereClause, whereArgs,null,null,null);
        return cursorToCsv(cursor);
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
        private static final String[] ALL_COLUMNS = {
                ID, TITLE, DATETIME, PAGE_FROM, PAGE_TO, COMMENTS, TEACHER_COMMENTS
        };
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
                toast(context, ""+e);
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                toast(context, "OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch(Exception e){
                toast(context, ""+e);
            }
        }
    }

    public String cursorToStr(Cursor cursor) {
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
            buffer.append(id+" "+title+" "+/*Helper.whatTimeAgo()*/datetime+" "+pageFrom+" "+pageTo+" "+comments+" "+teacherComments+" \n");
        }
        return buffer.toString();
    }

    public String cursorToStrCsv(Cursor cursor) {
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
            buffer.append(id+","+title+","+datetime+","+pageFrom+","+pageTo+","+comments+","+teacherComments+"\n");
        }
        return buffer.toString();
    }

    public String[] cursorToStrArrCsv(Cursor cursor){
        ArrayList<String> list = new ArrayList<>();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            list.add(new StringBuilder().append(id).append(",").append(title).append(",").append(datetime).append(",").append(pageFrom).append(",").append(pageTo).append(",").append(comments).append(",").append(teacherComments).append(" \n").toString());
        }
        return list.toArray(new String[0]);
    }

    public String[][] cursorToStrArrArr(Cursor cursor){
        ArrayList<String[]> list = new ArrayList<>();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.ID));
            String idStr = Integer.toString(id);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            list.add(new String[]{idStr, title, datetime, pageFrom, pageTo, comments, teacherComments});
        }
        return list.toArray(new String[0][]);
    }

    public String cursorToCsv(Cursor cursor){
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.ID));
            String idStr = Integer.toString(id);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            buffer.append(id).append(",").append(title).append(",").append(datetime).append(",").append(pageFrom).append(",").append(pageTo).append(",").append(comments).append(",").append(teacherComments).append("\n");
        }
        return buffer.toString();
    }

    public String cursorToJson(Cursor cursor){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.ID));
            String idStr = Integer.toString(id);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            buffer.append("{\"id\":\"").append(id)
                    .append("\",\"title\":\"").append(title)
                    .append("\",\"datetime\":\"").append(datetime)
                    .append("\",\"pageFrom\":\"").append(pageFrom)
                    .append("\",\"pageTo\":\"").append(pageTo)
                    .append("\",\"comments\":\"").append(comments)
                    .append("\",\"teacherComments\":\"").append(teacherComments)
                    .append("\"},").append("\n");
        }
//        buffer.delete(-2,-1);
        buffer.append("]");
        return buffer.toString();
    }

    public Diary[] cursorToDiaryArr(Cursor cursor){
        ArrayList<Diary> list = new ArrayList<>();
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.ID));
            String idd = Integer.toString(id);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TITLE));
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DATETIME));
            String pageFrom = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_FROM));
            String pageTo = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.PAGE_TO));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.COMMENTS));
            String teacherComments = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.TEACHER_COMMENTS));
            Diary diary = new Diary()
                    .id(idd)
                    .title(title)
                    .dateTime(datetime)
                    .pageFrom(pageFrom)
                    .pageTo(pageTo)
                    .comments(comments)
                    .teacherComments(teacherComments);
            list.add(diary);
        }
        return list.toArray(new Diary[0]);
    }
}
