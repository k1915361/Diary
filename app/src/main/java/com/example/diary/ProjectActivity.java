package com.example.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        String lastProject = getIntent().getStringExtra("lastProjectChosen");
        TextView textView = findViewById(R.id.ProjectStatus);
        textView.setText("You clicked on the "+lastProject+"!");
    }

    public void ReturnButtonPressed(View view) {
        String lastProject = getIntent().getStringExtra("lastProjectChosen");
        String ReturnMessage = "Thank you for selecting: "+lastProject;
        Intent replyIntent = new Intent();
        replyIntent.putExtra("ReturnedMessage", ReturnMessage);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}