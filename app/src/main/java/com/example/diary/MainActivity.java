package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Objects;
import java.util.Optional;

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
    TextView testTextView;
    EditText updateOld;
    EditText updateNew;
    EditText search;
//    myDbAdapter helper;
    myDbAdapterDiary helperDiary;
    Button viewDiary;
    Button deleteStudent;
    Button updateStudent;
    Button testScreen2;
    String testStr;
    String diariesSearchResult;
    boolean isTrue;
    int isOn;

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
        testTextView = findViewById(R.id.TestTextView);
        updateOld = findViewById(R.id.UpdateOld);
        updateNew = findViewById(R.id.UpdateNew);
        name = (EditText) findViewById(R.id.StudentName);
        search = (EditText) findViewById(R.id.SearchDiaryField);
//        helper = new myDbAdapter(this);
        helperDiary = new myDbAdapterDiary(this);
        submit = (Button) findViewById(R.id.submitButton);
        viewDiary = (Button) findViewById(R.id.GetDiary);
        updateStudent = (Button) findViewById(R.id.UpdateStudent);
        deleteStudent = (Button) findViewById(R.id.DeleteStudent);
        testScreen2 = (Button) findViewById(R.id.testScreen2);
        testField = findViewById(R.id.testField);

        diary = new Diary();
        datetime.setText(currentDateTime());

        onResume();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiary(v);
            }
        });
        viewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                viewSelectedDiary(v);
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
                viewSelectedDiary(v);
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
        search.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                ;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int before, int count) {
                ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String diariess = diaries.getText().toString();
                boolean hasText = diariess.contains(s);
                int indexOfSearch = diariess.indexOf(""+s);
//                if (indexOfSearch == -1) indexOfSearch = 0;
                String searchResult = diariess.substring(isNegativeThenElse(indexOfSearch,0));
                if(s.length() != 0)
                    testField.setText(""+diariess.length());
                    testTextView.setText(""+diariess.length()
                            +"\n"+indexOfSearch
                            +""+(indexOfSearch == -1 ? "\nNo Search Result":null)
                            +"\n"+searchResult
                            +"\n"+nullToStr("hello","def")
                            +"\n"+nullToStr(null,"def")
                            +"\n"+nullToStr("-1","def")
                            +"\n"+nullToStr("0","def")
                    );
            }
        });
    }

    public static int isNegativeThenV1(int defaultValue) {
        String str = System.getProperty("property");
        int num = Integer.parseInt(str);
        if (num < 0) return defaultValue;
        return num;
    }

    public static int isNegativeThenElse(int num, int defaultValue) {
        if (num < 0) return defaultValue;
        return num;
    }

    public static Object orElseV2(Object defaultValue) {
        Object o = System.getProperty("property");
//        return Objects.requireNonNullElse(o,defaultValue);
        return "JAVA 9 FOR THIS METHOD";
    }

    public static String orElse(String defaultValue) {
        return System.getProperty("property",defaultValue);
    }

    public static String nullToStr(String str, String defaultValue) {
        if (str.equals(null)) return defaultValue;
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewDiary.performClick();
    }

    public static String currentDateTime(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public void addDiary(View view) {
        String nme = name.getText().toString();
        String p1 = title.getText().toString();
        String p2 = datetime.getText().toString();
        String p3 = pageFrom.getText().toString();
        String p4 = pageTo.getText().toString();
        String p5 = comments.getText().toString();
        String p6 = commentsTeacher.getText().toString();
        String msg = "Enter New ";
        if(p1.isEmpty())
        {
            msg += "title ";
        }
        if(p2.isEmpty())
        {
            msg += "datetime ";
        }
        if(p3.isEmpty())
        {
            msg += "pageFromNumber ";
        }
        if(p4.isEmpty())
        {
            msg += "pageToNumber ";
        }
        if(p5.isEmpty())
        {
            msg += "comments ";
        }
        if(p6.isEmpty())
        {
            msg += "teacherComments ";
        }
        if(nme.isEmpty())
        {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_LONG).show();
        }

        if(p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty() || p5.isEmpty() || p6.isEmpty()){
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            long id = helperDiary.insertData(p1,p2,p3,p4,p5,p6);
//            long id = helper.insertData(nme,p1);
            if(id <= 0){
                Toast.makeText(this, "Insertion Unsuccessful", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Insertion Successful", Toast.LENGTH_LONG).show();
                name.setText("");
            }
        }
        viewSelectedDiary(view);
    }

    public void viewSelectedDiary(View view){
//        String data = helper.getData();
        String data = helperDiary.getData();
        diaries.setText(data);
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    public void delete(View view)
    {
        String uname = name.getText().toString();
        uname = updateOld.getText().toString();
        String id = uname;
        String selectedId = updateOld.getText().toString();
        if(uname.isEmpty())
        {
            Toast.makeText(this, "Enter Data", Toast.LENGTH_LONG).show();
        }
        else {
            int a = helperDiary.deleteById(id);
//            int a = helper.deleteById(uname);
            if(a <= 0)
            {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
                name.setText("");
            }
            else
            {
                Toast.makeText(this, "DELETED", Toast.LENGTH_LONG).show();
                name.setText("");
            }
        }
    }


    public void update(View view){
        String onm = updateOld.getText().toString();
        String nnm = updateNew.getText().toString();
//        int id2 = Integer.parseInt(updateOld.getText().toString());
        String id = updateOld.getText().toString();
        String u1 = title.getText().toString();
        String u2 = datetime.getText().toString();
        String u3 = pageFrom.getText().toString();
        String u4 = pageTo.getText().toString();
        String u5 = comments.getText().toString();
        String u6 = commentsTeacher.getText().toString();
        String msg = "Enter New ";

//        if(Integer.toString(id).isEmpty())
        if(id.isEmpty()) {
            msg += "ID ";
        }
        if(u1.isEmpty()) {
            msg += "title ";
        }
        if(u2.isEmpty()) {
            msg += "datetime ";
        }
        if(u3.isEmpty()) {
            msg += "pageFromNumber ";
        }
        if(u4.isEmpty()) {
            msg += "pageToNumber ";
        }
        if(u5.isEmpty()) {
            msg += "comments ";
        }
        if(u6.isEmpty()) {
            msg += "teacherComments ";
        }
//        if(onm.isEmpty() || nnm.isEmpty()){
//            Toast.makeText(this, "Enter New name and Old name", Toast.LENGTH_LONG).show();
//        }
        if(id.isEmpty() || u1.isEmpty() || u2.isEmpty() || u3.isEmpty() || u4.isEmpty() || u5.isEmpty() || u6.isEmpty()){
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            int a = helperDiary.updateDiary(id, u1, u2, u3, u4, u5, u6);
//            int a = helper.updateName(onm,nnm);
            Toast.makeText(this, onm+" "+nnm, Toast.LENGTH_LONG).show();
            if(a <= 0)
            {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show();

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