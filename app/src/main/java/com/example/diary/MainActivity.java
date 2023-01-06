package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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

// lecture 5 & 7 & 9
public class MainActivity extends AppCompatActivity {
    Button submit;      // Edit
    EditText name;
    EditText title;
    EditText datetime;
    EditText pageFrom;
    EditText pageTo;
    EditText comments;
    EditText commentsTeacher; // } Edit
    EditText testField;
    Diary diary;
    TextView diaries;   // Edit Remove
    TextView updateOld;
    TextView updateNew;
    myDbAdapter helper;
    Button viewDiary;
    Button deleteStudent;
    Button updateStudent;
    Button testScreen2;
    String testStr;
    Message message = new Message(this);

    public void toast(String str) {
        Message.toast(str);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        String reply = data.getStringExtra("ReturnedMessage");
//        EditText textView = findViewById(R.id.StudentName);
//        textView.setText(reply+"!");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.StudentName);
        title = findViewById(R.id.editTitle);
        pageFrom = findViewById(R.id.editPageFrom);
        pageTo = findViewById(R.id.editPageTo);
        comments = findViewById(R.id.comments);
        commentsTeacher = findViewById(R.id.commentsTeacher);
        datetime = findViewById(R.id.editTextDate);
        diaries = findViewById(R.id.Diaries);
        updateOld = findViewById(R.id.UpdateOld);
        updateNew = findViewById(R.id.UpdateNew);
        name = (EditText) findViewById(R.id.StudentName);
        helper = new myDbAdapter(this);
        submit = (Button) findViewById(R.id.submitButton);
        viewDiary = (Button) findViewById(R.id.GetDiary);
        updateStudent = (Button) findViewById(R.id.UpdateStudent);
        deleteStudent = (Button) findViewById(R.id.DeleteStudent);
        testScreen2 = (Button) findViewById(R.id.testScreen2);
        testField = findViewById(R.id.testField);

        diary = new Diary();
        diary.setTitle("abc test here");
        testField.setText(":) "+diary.getTitle());


        datetime.setText(""+currentDateTime());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent(v);
            }
        });
        viewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                viewSelectedDiary(v);
            }
        });
        updateStudent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                update(v);
            }
        });
        deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                delete(v);
                viewSelectedDiary(v);
            }
        });

        testScreen2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                testStr = "aaabc";
                testScreen2ButtonPressed(v);
            }
        });
    }

    public static String dateStringToString(String date)
    {
        DateFormat df = new SimpleDateFormat(date);
        Date today = Calendar.getInstance().getTime();
        String dateToString = df.format(today);
        return (dateToString);
    }

    public void calendarViewOnDateChangeListener() {
        new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                month = month + 1;
                String newDate = year+"-"+month+"-"+day;
                datetime.setText(newDate);
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

    public static String currentDateTime(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault()).format(new Date());
        return currentDate;
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

    public void addStudent(View view) {
        String p1 = title.getText().toString();
        String p2 = datetime.getText().toString();
        String p3 = pageFrom.getText().toString();
        String p4 = pageTo.getText().toString();
        String p5 = comments.getText().toString();
        String p6 = commentsTeacher.getText().toString();

        if(p1.isEmpty())
        {
            toast( "Enter Name");
        }
        else
        {
            long id = helper.insertData(p1,p2,p3,p4,p5,p6);
            if(id <= 0){
                toast( "Insertion Unsuccessful");
                name.setText("");
            }
            else
            {
                toast( "Insertion Successful");
                name.setText("");
            }
        }
        viewSelectedDiary(view);
    }

    public void viewSelectedDiary(View view){
        String data = helper.getData();
        diaries.setText(data);
        toast( data);
    }

    public void delete(View view)
    {
        String uname = name.getText().toString();
        int selectedId = Integer.parseInt(updateNew.getText().toString());
        if(uname.isEmpty())
        {
            toast( "Enter Data");
        }
        else {
            int a = helper.delete(selectedId);
            if(a <= 0)
            {
                toast( "Unsuccessful");
                name.setText("");
            }
            else
            {
                toast( "DELETED");
                name.setText("");
            }
        }
    }

    public void update(View view){
        String example = updateNew.getText().toString();
        int id = Integer.parseInt(updateOld.getText().toString());
        String u1 = title.getText().toString();
        String u2 = datetime.getText().toString();
        String u3 = pageFrom.getText().toString();
        String u4 = pageTo.getText().toString();
        String u5 = comments.getText().toString();
        String u6 = commentsTeacher.getText().toString();
        String msg = "Enter New ";

        if(Integer.toString(id).isEmpty())
        {
            msg += "ID ";
        }
        if(u1.isEmpty())
        {
            msg += "title ";
        }
        if(u2.isEmpty())
        {
            msg += "datetime ";
        }
        if(u3.isEmpty())
        {
            msg += "pageFromNumber ";
        }
        if(u4.isEmpty())
        {
            msg += "pageToNumber ";
        }
        if(u5.isEmpty())
        {
            msg += "comments ";
        }
        if(u6.isEmpty())
        {
            msg += "teacherComments ";
        }
        if(Integer.toString(id).isEmpty() || u1.isEmpty() || u2.isEmpty() || u3.isEmpty() || u4.isEmpty() || u5.isEmpty() || u6.isEmpty()){
            toast(msg);
        }
        else
        {
            int a = helper.updateDiary(id, u1, u2, u3, u4, u5, u6);
            if(a <= 0)
            {
                toast( "Unsuccessful");
                updateOld.setText("");
                updateNew.setText("");
            } else {
                toast( "Updated");
                updateOld.setText("");
                updateNew.setText("");
            }
        }


    }

    public void testScreen2ButtonPressed(View view){
        Intent animalScreen = new Intent(this, ProjectActivity.class);
        String nam = name.getText().toString();
        animalScreen.putExtra("nameChosen", nam);
        startActivityForResult(animalScreen, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String reply = data.getStringExtra("responseFromAnimalScreen");
        TextView textView = findViewById(R.id.testField);
        textView.setText("The last pet was called "+reply+"!");
    }
}