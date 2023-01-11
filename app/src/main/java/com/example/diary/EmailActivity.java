/*
    #2 	Email
        a selected diary to staff (formatted in a way to transcribe to “official” system)
        SHOULD – 4 marks
*/

package com.example.diary;

import static com.example.diary.Helper.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity{
    EditText bodyEdit;
    EditText subjectEdit;
    EditText emailEdit;
    TextView diaryView;
    Button emailBtn;
    CheckBox csvBtn;
    CheckBox jsonBtn;
    String email = "a@gmail.com";
    String subject = "Diary Data";
    String body = "\n";
    String ids;
    String diarySelected;
    final String DiaryCSVHeader = "id,title,datetime,pageFrom,pageTo,comments,teacherComments";
    static final DiaryDbAdapter DB = MainActivity.HELPER;
    String jsonData;
    String csvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        emailBtn = (Button) findViewById(R.id.emailButton2);
        csvBtn = (CheckBox) findViewById(R.id.csvBtn);
        jsonBtn = (CheckBox) findViewById(R.id.jsonBtn);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        bodyEdit = (EditText) findViewById(R.id.bodyEdit);
        subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        diaryView = findViewById(R.id.diaryDataPreview);
        emailEdit.setHint(email);
        subjectEdit.setHint(subject);
        bodyEdit.setHint("Email body");
        diarySelected = getIntent().getStringExtra("diarySelected");
        ids = getIntent().getStringExtra("diaryIdsSelected");
        diaryView.setText(DiaryCSVHeader+"\n"+diarySelected);
        jsonData = new StringBuilder().append(DB.getJsonByIds(ids)).append("\n").toString();
        csvData = new StringBuilder().append(DiaryCSVHeader).append("\n").append(DB.getCsvByIds(ids)).append("\n").toString();

        csvBtn.setChecked(true);
        csvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(csvBtn.isChecked() && jsonBtn.isChecked()) {
                    diaryView.setText(new StringBuilder().append(csvData).append("\n").append(jsonData).toString());
                }
                else if(isFormatChecked()) {;}
                else {
                    jsonBtn.performClick();
                }
            }
        });
        jsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jsonBtn.isChecked() && csvBtn.isChecked()) {
                    diaryView.setText(new StringBuilder().append(jsonData).append("\n").append(csvData).toString());
                }
                else if(isFormatChecked()) {;}
                else {
                    csvBtn.performClick();
                }
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                email = (orElseStr(text(emailEdit),"a@gmail.com"));
                subject = (orElseStr(text(subjectEdit),"Diary Data Report"));
                body = text(bodyEdit);
                body = text(diaryView);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email });
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT,body);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                    Toast error = Toast.makeText(getApplicationContext(), "No email app installed!", Toast.LENGTH_LONG);
                    error.show();
                }
            }
        });
    }

    public boolean isFormatChecked(){
        if(csvBtn.isChecked()) {
            diaryView.setText(csvData);
            return true;
        }
        else if(jsonBtn.isChecked()) {
            diaryView.setText(jsonData);
            return true;
        }
        return false;
    }

    public void ReturnButtonPressed(View view) {
        String lastProject = getIntent().getStringExtra("lastProjectChosen");
        String ReturnMessage = "Thank you for selecting: "+lastProject;
        Intent replyIntent = new Intent();
        replyIntent.putExtra("ReturnedMessage", ReturnMessage);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void csvBtnPerformClick(){
        csvBtn.performClick();
    }
}

