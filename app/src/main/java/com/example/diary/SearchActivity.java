package com.example.diary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    EditText search;
    final String DiaryViewHeader = "ID Book Date Page Comments Teacher's\n";
    TextView diaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        diaryView = findViewById(R.id.diaryView);
        search = findViewById(R.id.searchEdit);
        viewSelectedDiary();
        search.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {;}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {;}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewDiaryBySearchAny();
            }
        });
    }

    public void viewDiaryBySearchAny() {
        String srch = search.getText().toString();
        if(srch.isEmpty()){
            viewSelectedDiary();
        } else {
            String data = "";
            if(Helper.isInt(srch)) {
                data = MainActivity.HELPER.getById(srch);
            }
            if(data.isEmpty()) {
                data = MainActivity.HELPER.searchAny(srch);
            }
            if (data.isEmpty()){
                viewSelectedDiary();
            } else {
                diaryView.setText(DiaryViewHeader+"\n"+data);
            }
        }
    }

    public void viewSelectedDiary(){
        String data = MainActivity.HELPER.getData();
        diaryView.setText(DiaryViewHeader+"\n"+data);
    }

    public void ReturnButtonPressed(View view) {
        String strExtra = getIntent().getStringExtra("searched");
        String ReturnMessage = Helper.text(search);
        Intent replyIntent = new Intent();
        replyIntent.putExtra("ReturnedMessage", ReturnMessage);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}