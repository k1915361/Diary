/*
#3 	Search
    previous diaries (all fields recorded)
    COULD – 3 marks

*/
package com.example.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class Search extends AppCompatActivity {
    myDbAdapter helper;
    TextView diaries;
    EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        diaries = findViewById(R.id.Diaries);
        searchField = findViewById(R.id.searchField);
        helper = new myDbAdapter(this);
    }

    public void viewSelectedDiary(View view){
        String data = helper.getData();
        diaries.setText(data);
    }

    public boolean isMatches(String src, String dst){
        return Pattern.compile(Pattern.quote(dst), Pattern.CASE_INSENSITIVE)
                .matcher(src)
                .find();
    }
}
