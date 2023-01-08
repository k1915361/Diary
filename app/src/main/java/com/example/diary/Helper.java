package com.example.diary;

import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
        if(search.length() != 0) {
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
            defaultValue = o.toString();
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return defaultValue;
    }

    public static Object isNull(Object o, Object defaultValue){
        try{
            if(o == null) return "1"+defaultValue;
            else if(o.toString().length() == 0) return defaultValue;
            else if(o.toString().isEmpty()) return "2"+defaultValue;
        } catch(Exception e){
            Log.d(TAG,""+e);
        }
        return "";
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
}
