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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Email extends AppCompatActivity{

    Button emailButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailButton = (Button) findViewById(R.id.emailButton);

        emailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri mailUri = Uri.parse("mailto:test@mail.com");
                String subject = "This is a test email";
                String body = "This is my wonderful test email.\n\n";
                body += "This is not spam at all, but very very useful.\n\n";
                body += "I could click the button many times and send lots of them.";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, mailUri);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                if(emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                } else {
                    Toast error = Toast.makeText(getApplicationContext()/*this*/, "No email app installed!", Toast.LENGTH_LONG);
                    error.show();
                }
            }
        });

    }


}

