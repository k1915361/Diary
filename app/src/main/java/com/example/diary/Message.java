package com.example.diary;

import android.content.Context;
import android.widget.Toast;

public class Message {
    private static Context context;
    public Message (Context context){
        this.context = context;
    }
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
