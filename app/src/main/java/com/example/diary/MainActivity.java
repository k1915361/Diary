package com.example.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

// lecture 5 & 7 & 9
public class MainActivity extends AppCompatActivity {
    EditText title;
    EditText datetime;
    EditText pageFrom;
    EditText pageTo;
    EditText comments;
    EditText commentsTeacher;
    Diary diary;
    TextView diaries;
    EditText diaryId;
    EditText search;
    myDbAdapterDiary helperDiary;
    static myDbAdapterDiary HELPER;
    Button submit;
    Button deleteBtn;
    Button updateBtn;
    Button testScreen2;
    Button emailButton;
    String previousId;
    String previousTitle;
    String previousDatetime;
    String previousPageFrom;
    String previousPageTo;
    String previousComments;
    String previousCommentsTeacher;
    int isYes = 0;
    final String DiaryViewHeader = "ID Title Date Page Comments TeacherComments\n";
    final String DiaryCSVHeader = "id,title,datetime,pageFrom,pageTo,comments,teacherComments";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.editTitle);
        pageFrom = findViewById(R.id.editPageFrom);
        pageTo = findViewById(R.id.editPageTo);
        datetime = findViewById(R.id.editTextDate);
        comments = findViewById(R.id.comments);
        commentsTeacher = findViewById(R.id.commentsTeacher);
        diaries = findViewById(R.id.Diaries);
        diaryId = findViewById(R.id.DiaryIdField);
        search = (EditText) findViewById(R.id.SearchDiaryField);
        helperDiary = new myDbAdapterDiary(this);
        HELPER = new myDbAdapterDiary(this);
        submit = (Button) findViewById(R.id.submitButton);
        updateBtn = (Button) findViewById(R.id.UpdateStudent);
        deleteBtn = (Button) findViewById(R.id.DeleteStudent);
        testScreen2 = (Button) findViewById(R.id.testScreen2);
        emailButton = (Button) findViewById(R.id.emailButton);
        diary = new Diary();
        datetime.setText(currentDateTime());
        datetime.setHint(currentDateTime());
        viewSelectedDiary();

        emailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emailBtnPressed();
//                String mailStr = "a@gmail.com";
//                String subject = "Diary Data";
//                String body = DiaryCSVHeader+"\n";
//                body += helperDiary.getDataOfficialFormat();
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mailto:"));
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ mailStr });
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                intent.putExtra(Intent.EXTRA_TEXT,body);
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
//                    Toast error = Toast.makeText(getApplicationContext()/*this*/, "No email app installed!", Toast.LENGTH_LONG);
//                    error.show();
//                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiary(v);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                update(v);
                viewSelectedDiary(v);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                deletePressed(v);
                viewSelectedDiary(v);
            }
        });

        testScreen2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                testScreen2ButtonPressed(v);
            }
        });
        search.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {;}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {;}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoFill();
                viewDiaryBySearchAny();
                if(text(search) == null){
                    bringBackPreviousInputs();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        viewDiary.performClick();
    }

    public static String currentDateTime(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public void addDiary(View view) {
        String p1 = title.getText().toString();
        String p2 = datetime.getText().toString();
        String p3 = pageFrom.getText().toString();
        String p4 = pageTo.getText().toString();
        String p5 = comments.getText().toString();
        String p6 = commentsTeacher.getText().toString();
        String msg = "Enter "
                +Helper.nullToStr(p1,"title ")
                +Helper.nullToStr(p2,"datetime ")
                +Helper.nullToStr(p3,"pageFrom ")
                +Helper.nullToStr(p4,"pageTo ")
                +Helper.nullToStr(p5,"comments ")
                +Helper.nullToStr(p6,"TeacherComments ");

        if(p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty() || p5.isEmpty() || p6.isEmpty()){
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            long id = helperDiary.insertData(p1,p2,p3,p4,p5,p6);
            if(id <= 0){
                Toast.makeText(this, "Insertion Unsuccessful", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Insertion Successful", Toast.LENGTH_LONG).show();
            }
        }
        viewSelectedDiary(view);
    }

    public void viewSelectedDiary(View view){
        String data = helperDiary.getData();
        diaries.setText(DiaryViewHeader+data);
    }

    public void viewSelectedDiary(){
        String data = helperDiary.getData();
        diaries.setText(DiaryViewHeader+data);
    }

    public void bringBackPreviousInputs(){
        diaryId.setText(previousId);
        title.setText(previousTitle);
        datetime.setText(previousDatetime);
        pageFrom.setText(previousPageFrom);
        pageTo.setText(previousPageTo);
        comments.setText(previousComments);
        commentsTeacher.setText(previousCommentsTeacher);
    }

    public void autoFill(){
        Diary d = helperDiary.getDiaryById(text(search));
        if(d != null){
            previousId = text(diaryId);
            previousTitle = text(title);
            previousDatetime = text(datetime);
            previousPageFrom = text(pageFrom);
            previousPageTo = text(pageTo);
            previousComments = text(comments);
            previousCommentsTeacher = text(commentsTeacher);
            if(text(diaryId).isEmpty());
            diaryId.setText(d.getId());
            title.setText(d.getTitle());
            datetime.setText(d.getDateTime());
            pageFrom.setText(d.getPageFrom());
            pageTo.setText(d.getPageTo());
            comments.setText(d.getComments());
            commentsTeacher.setText(d.getTeacherComment());
        } else {
            bringBackPreviousInputs();
        }
    }

    public void deletePressed(View view) {
        confirmDialog();
    }

    public void delete(View view) {
        String id = text(search);
        Toast.makeText(this, "isYes 0 "+isYes, Toast.LENGTH_LONG).show();
        if(id.isEmpty() || isYes == 0) {
            Toast.makeText(this, "Enter Data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "isYes 0 "+isYes, Toast.LENGTH_LONG).show();
//            int a = helperDiary.deleteById(id);
            int a=1;
            if(a <= 0) {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "DELETED", Toast.LENGTH_LONG).show();
                search.setText("");
            }
            isYes = 0;
        }
        Toast.makeText(this, "isYes 0 "+isYes, Toast.LENGTH_LONG).show();
        isYes = 0;
    }

    public void delete() {
        String id = text(search);
        if(id.isEmpty() || isYes == 0) {
            Toast.makeText(this, "Enter Data", Toast.LENGTH_LONG).show();
        } else {
            int a = helperDiary.deleteById(id);
            if(a <= 0) {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "DELETED", Toast.LENGTH_LONG).show();
                search.setText("");
            }
        }
    }

    public void confirmDialog() {
        new AlertDialog.Builder(this)
        .setTitle("Title")
        .setMessage("Are you sure?")
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                isYes = 1;
                delete();
                isYes = 0;
            }})
        .setNegativeButton(android.R.string.no, null).show();
    }

    public void viewDiaryById(View view) {
        String id = search.getText().toString();
        String diariesStr = diaries.getText().toString();
        String isDiariesViewEmpty = diariesStr.substring(0,1);

        if(id.isEmpty()){
            Toast.makeText(this, "Enter ID", Toast.LENGTH_LONG).show();
            viewSelectedDiary(view);
        } else {
            String data = helperDiary.getById(id);
            if (data.isEmpty()){
                viewSelectedDiary(view);
                Toast.makeText(this, "No Diary found with the ID", Toast.LENGTH_SHORT).show();
            } else {
                diaries.setText(DiaryViewHeader+data);
            }
        }
    }

    public void viewDiaryBySearchAny() {
        String srch = search.getText().toString();

        if(srch.isEmpty()){
            viewSelectedDiary();
        } else {
            String data = helperDiary.searchAny(srch);
            if (data.isEmpty()){
                viewSelectedDiary();
            } else {
                diaries.setText(DiaryViewHeader+data);
            }
        }
    }

    public void update(View view){
        String id = diaryId.getText().toString();
        String u1 = title.getText().toString();
        String u2 = datetime.getText().toString();
        u2 = u2.isEmpty() ? currentDateTime() : u2;
        String u3 = pageFrom.getText().toString();
        String u4 = pageTo.getText().toString();
        String u5 = comments.getText().toString();
        String u6 = commentsTeacher.getText().toString();
        String msg = "Enter New "
                +Helper.nullToStr(id,"diaryID ")
                +Helper.nullToStr(u1,"title ")
                +Helper.nullToStr(u2,"datetime ")
                +Helper.nullToStr(u3,"pageFrom ")
                +Helper.nullToStr(u4,"pageTo ")
                +Helper.nullToStr(u5,"comments ")
                +Helper.nullToStr(u6,"TeacherComments ");

        if(id.isEmpty() || u1.isEmpty() || u2.isEmpty() || u3.isEmpty() || u4.isEmpty() || u5.isEmpty() || u6.isEmpty()){
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            int a = helperDiary.updateDiary(id, u1, u2, u3, u4, u5, u6);
            if(a <= 0)
            {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show();

                diaryId.setText("");
            }

        }
    }

    public String text(EditText e){
        return Helper.text(e);
    }
    public String text(TextView e){
        return Helper.text(e);
    }

    public void testScreen2ButtonPressed(View view){
        Intent screen = new Intent(this, ProjectActivity.class);
        String drs = text(diaries);
        screen.putExtra("nameChosen", drs);
        startActivityForResult(screen, 3);
    }

    public void emailBtnPressed(){
        Intent screen = new Intent(this, EmailActivity.class);
        String diriess = text(diaries);
        screen.putExtra("nameChosen", diriess);
        startActivityForResult(screen, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String reply = data.getStringExtra("ReturnedMessage");
        diaries.setText("From Screen2: "+reply+"");
    }

    public void emailAppChooser(Intent emailIntent){
        Intent intent = emailIntent;
        startActivity(Intent.createChooser(intent, "Send mail..."));
    }
}