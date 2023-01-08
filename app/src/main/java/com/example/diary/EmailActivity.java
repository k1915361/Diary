/*
    #2 	Email
        a selected diary to staff (formatted in a way to transcribe to “official” system)
        SHOULD – 4 marks
*/

package com.example.diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity{
    EditText bodyEdit;
    EditText subjectEdit;
    EditText emailEdit;
    Button emailButton;
    String email = "a@gmail.com";
    String subject = "Diary Data Report";
    String body = "\n";
    final String DiaryCSVHeader = "id,title,datetime,pageFrom,pageTo,comments,teacherComments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        emailButton = (Button) findViewById(R.id.emailButton2);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        bodyEdit = (EditText) findViewById(R.id.bodyEdit);
        subjectEdit = (EditText) findViewById(R.id.subjectEdit);
        emailEdit.setHint(email);
        subjectEdit.setHint(subject);
        bodyEdit.setHint(body+DiaryCSVHeader+"\n"+MainActivity.HELPER.getDataOfficialFormat());
        emailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                email = (Helper.orElseStr(Helper.text(emailEdit),"a@gmail.com"));
                subject = (Helper.orElseStr(Helper.text(subjectEdit),"Diary Data Report"));
                body = Helper.text(bodyEdit);
                body += DiaryCSVHeader+"\n";
                body += MainActivity.HELPER.getDataOfficialFormat();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email });
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT,body);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast error = Toast.makeText(getApplicationContext(), "No email app installed!", Toast.LENGTH_LONG);
                    error.show();
                }
            }
        });
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

