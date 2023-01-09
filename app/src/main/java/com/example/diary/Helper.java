package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Helper {
    static final String TAG = "Helper";

    public static String currentDateTime(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public long dateDiff(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date firstDate = sdf.parse(currentDate());
        Date secondDate = sdf.parse(date);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }

    public static String whatTimeAgo(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            Date firstDate = sdf.parse(currentDate());
            Date secondDate = sdf.parse(date);
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diffInMillies/1000 <= 60 && false) return diffInMillies/1000+"sec ago";
            if (hours < 1 && false) return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)+"min ago";
            if (hours <= 24 && false) return hours+(hours > 1 ? "hrs" : "hr")+" ago";
            if (diff <= 30) return diff+(diff > 1 ? "ds" : "d")+" ago";
            if (diff > 30) return Math.floor(diff/30)+(diff/30 > 1 ? "ms" : "m")+" ago";
            if (diff > 365) return Math.floor(diff/365)+(diff/365 > 1 ? "yrs" : "yr")+" ago";
        } catch (Exception e) {
            Log.d(TAG, ""+e);
        }
        return "null";
    }

    public static void containsTest(EditText edt, String search, TextView view) {
        String drs = text(edt);
        boolean hasText = drs.contains(search);
        int indexOfSearch = drs.indexOf(""+search);
        String searchResult = drs.substring(Helper.negativeToDefault(indexOfSearch,0));
        if(!search.isEmpty()) {
            view.setText(MessageFormat.format("{0}\n{1}{2}\n{3}\n{4}"
                    , drs.length(), indexOfSearch, indexOfSearch == -1 ? "\nNo Search Result" : ""
                    , searchResult, Helper.nullToStr(null, "replaced")));
        }
    }

    public static String nullToStr(Object str, String defaultValue) {
        try{
            if(str == null) return defaultValue;
            else if(str.toString().length() == 0) return defaultValue;
            else if(str.toString().isEmpty()) return defaultValue;
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return "";
    }

    public static String text(EditText e){
        return e.getText().toString();
    }

    public static String text(TextView e){
        return e.getText().toString();
    }

    public String orElse(EditText text){
        Editable editable = text.getText();
        String value = editable == null ? "" : editable.toString();
        return value;
    }

    public static int orElseInt(Object o, int defaultValue) {
        try{
            defaultValue = Integer.parseInt(o.toString());
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return defaultValue;
    }

    public static Object orElseObj(Object o, Object defaultValue) {
        try{
            if(o != null) return o;
            if(o.toString().isEmpty()) return o;
            if(o.toString().length() != 0) return o;
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return defaultValue;
    }

    public static String orElseStr(Object o, String defaultValue) {
        try{
            if(o.toString().isEmpty()) return o.toString();
            if(o != null) return o.toString();
            if(o.toString().length() != 0) return o.toString();
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return defaultValue;
    }

    public static String orElseStr(String o, String defaultValue) {
        try{
            if(o.isEmpty()) return defaultValue;
            if(o != null) return defaultValue;
            if(o.length() != 0) return defaultValue;
            defaultValue = o;
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return defaultValue;
    }

    public static boolean isNull(Object o){
        try{
            if(o.toString().isEmpty()) return true;
        } catch(Exception e){
            return true;
        }
        return false;
    }

    public static boolean isNull(String o){
        try{
            if(o.isEmpty()) return true;
        } catch(Exception e){
            return true;
        }
        return false;
    }

    public static boolean isBlank(String o){
        if(o == null || o.trim().isEmpty())
            return false;
        return true;
    }

    public static boolean isInt(Object o){
        if (o == null) return false;
        try{
            Integer.parseInt(o.toString());
        } catch(Exception e){
            return false;
        }
        return false;
    }

    public static boolean isNumeric(String s){
        if(s == null) return false;
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    public static int negativeToDefault(int num, int defaultValue) {
        if (num < 0) return defaultValue;
        return num;
    }


    public static String dateStringToString(String date)
    {
        DateFormat df = new SimpleDateFormat(date);
        Date today = Calendar.getInstance().getTime();
        String dateToString = df.format(today);
        return (dateToString);
    }

    public int toInt(String v){
        int myNum = 0;
        try {
            myNum = Integer.parseInt(v);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        return myNum;
    }

    public static String toStr(String[][] arr){
        StringBuilder str = new StringBuilder();
        try {
            str.append(Arrays.deepToString(arr));
//            for (String[] s : arr){
//                str.append(Arrays.toString(s)).append(",");
//            }
        } catch(NumberFormatException nfe) {
            return "";
        }
        return str.toString();
    }

    public String toStr(String[] arr){
        String str;
        try {
            str = Arrays.toString(arr);
        } catch(NumberFormatException nfe) {
            return "";
        }
        return str;
    }

    public void calendarViewOnDateChangeListener() {
        new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                month = month + 1;
                String newDate = year+"-"+month+"-"+day;
//                datetime.setText(newDate);
            }
        };
    }

    public static String dateStringToStringV2(String date)
    {
        LocalDate givenDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            givenDate = LocalDate.parse(date);
        }

        String dateToString = givenDate.toString();
        if (dateToString.equals(null)) return date.toString();

        return (dateToString);
    }

    public static String currentDate(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String currentTime(){
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        return currentTime;
    }

    public void accessFilespermission () {
        ;
    }

    public static void toast(Context cnt, Object msg){
        Toast.makeText(cnt, ""+msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isMatches(String src, String dst){
        return Pattern.compile(Pattern.quote(dst), Pattern.CASE_INSENSITIVE)
                .matcher(src)
                .find();
    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    public static String format(String id,String title,String datetime,String pageFrom,String pageTo,String comments,String teacherComments) {
        return String.format("%s%15s%15s%15s%15s%15s%15s\n",id, title, datetime, pageFrom, pageTo, comments, teacherComments);
    }

    public static String idOfRecord(String str){
        int idx = str.indexOf(" ");
        String id = str.substring(0, idx);
        return id;
    }
}
