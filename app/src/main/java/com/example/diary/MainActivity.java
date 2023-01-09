package com.example.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

// lecture 5 & 7 & 9
public class MainActivity extends AppCompatActivity {
    EditText title;
    EditText datetime;
    EditText pageFrom;
    EditText pageTo;
    EditText comments;
    EditText commentsTeacher;
    Diary diary;
    TextView diaryView;
    EditText diaryId;
    EditText search;
    ScrollView diaryScrollView;
    DiaryDbAdapter helper;
    static DiaryDbAdapter HELPER;
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
        commentsTeacher = findViewById(R.id.teacherComments);
        diaryView = findViewById(R.id.diaryView2);
        diaryId = findViewById(R.id.diaryId);
        search = (EditText) findViewById(R.id.SearchDiaryField);
        helper = new DiaryDbAdapter(this);
        HELPER = new DiaryDbAdapter(this);
        submit = (Button) findViewById(R.id.addBtn);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        testScreen2 = (Button) findViewById(R.id.searchScrBtn);
        emailButton = (Button) findViewById(R.id.emailScrBtn);
        diary = new Diary();
        datetime.setHint(currentDateTime());
        datetime.setText(currentDateTime());
        viewSelectedDiary();
        Log.d(TAG, text(diaryView));
        emailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emailBtnPressed();
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
                searchScrnBtnPressed(v);
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
        search.performClick();
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
            long id = helper.insertData(p1,p2,p3,p4,p5,p6);
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

    public void viewDiaryOnStartUp(View view){
        String data = helper.getData();
        diaryView.setText(DiaryViewHeader+data);
    }

    public void viewSelectedDiary(View view){
        String data = helper.getData();
        diaryView.setText(data);
    }

    public void viewSelectedDiary(){
        String data = helper.getData();
        diaryView.setText(data);
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

    /*
        Auto-fills the user entries when a previous record is selected
        Remembers all user inputs before autofill
        Just empty the search field then automatically brings back.
     */
    public void autoFill(){
        Diary d = helper.getDiaryById(text(diaryId)); //search
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

    public void delete(View... view) {
        String id = text(search);
        if(id.isEmpty() || isYes == 0) {
            Toast.makeText(this, "Enter Data", Toast.LENGTH_LONG).show();
        } else {
            int a = helper.deleteById(id);
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

    /*  Search by pageRange eg 3 6
        search by Ids eg 3,6,9
        search by book, date, comments

        Sample data
        1 Mars              07-01-2023_00:35 3 4    my comments and rating +++++  teacher's comments
        2 Man in the mirror 07-01-2023_00:35 1 9    my rating +++++  t
        3 The Glory         07-01-2023_00:35 1 9    my rating +++++  t
        4 Charlie Chaplin   07-01-2023_00:35 1 88   my rating ++++  t
        5 The mice and man  07-01-2023_00:35 1 1    my rating +++  t
        6 The cat in boots  07-01-2023_00:35 1 1    my rating ++  t
        7 The fairy tale    06-01-2023_23:30 1 99   my rating + t
    */
    public void viewDiaryBySearchAny(View... view) {
        String sch = text(search);
        getSelectedIds();
        if(sch.isEmpty()){
            viewSelectedDiary();
        } else {
            String data = "";
            data = helper.selectByPageRange(sch);
            if(data.isEmpty()){
                data = helper.selectByIds(sch);
            }
            if(data.isEmpty() && Helper.isInt(sch)) {
                data = helper.getById(sch);
            }
            if(data.isEmpty()) {
                data = helper.searchAny(sch);
            }
            if(data.isEmpty()){
                viewSelectedDiary();
            } else {
                diaryView.setText(data);
                int idx = data.indexOf(" ");
                String id = data.split(" ")[0];
                id = data.substring(0, idx);
                id = Helper.idOfRecord(data);
                diaryId.setText(id);
            }
        }
    }

    public String getDiarySelectedInFormat(){
        String sch = text(search);
        String data ="";
        if(sch.isEmpty()){
            viewSelectedDiary();
        } else {
            data = helper.getCsvByIds(sch);
            Helper.toast(this, ""+data);
            if(!data.isEmpty()) return data;
            if(data.isEmpty() && Helper.isInt(sch)) {
                data = helper.getByIdInFormat(sch);
            }
            if(data.isEmpty()){
                data = helper.selectByPageRange(sch);
            }
            if(data.isEmpty()) {
                data = helper.searchAnyInFormat(sch);
            } else {
                return data;
            }
            if(!data.isEmpty()) {
                return data;
            }
        }
        return helper.getDataInFormat();
    }

    public String getSelectedIds(){
        String diary = text(diaryView);
        String[] records = diary.split("\n");
        String ids = "";
        for (int i=0; i<records.length; i++){
            records[i] = Helper.idOfRecord(records[i]);
        }
        ids = Arrays.toString(records);
        ids = ids.replace("[","").replace("]","");
        return ids;
    }

    public String testPlayground(){
        String sch = text(search);
        String data ="";
        if(sch.isEmpty()){
            viewSelectedDiary();
        } else {
            data = helper.getCsvByIds(sch);
            Helper.toast(this, ""+data);
            if(!data.isEmpty()) return data;
            if(data.isEmpty() && Helper.isInt(sch)) {
                data = helper.getByIdInFormat(sch);
            }
            if(data.isEmpty()) {
                data = helper.searchAnyInFormat(sch);
            } else {
                return data;
            }
            if(!data.isEmpty()) {
                return data;
            }
        }
        return helper.getDataInFormat();
    }

    public String getSearchDiary() {
        String sch = search.getText().toString();
        if(sch.isEmpty()){
            viewSelectedDiary();
        } else {
            String data = "";
            data = helper.selectByIds(sch);
            if(data.isEmpty() && Helper.isInt(sch)) {
                data = helper.getById(sch);
            }
            if(data.isEmpty()) {
                data = helper.searchAny(sch);
            }
            if (data.isEmpty()){
                return helper.getDataOfficialFormat();
            } else {
                return data;
            }
        }
        return "";
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
            int a = helper.updateDiary(id, u1, u2, u3, u4, u5, u6);
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

    public void searchScrnBtnPressed(View view){
        Intent screen = new Intent(this, SearchActivity.class);
        String drs = text(diaryView);
        screen.putExtra("nameChosen", drs);
        startActivityForResult(screen, 3);
    }

    public void emailBtnPressed(){
        Intent screen = new Intent(this, EmailActivity.class);
        String data = getDiarySelectedInFormat();
        String ids = getSelectedIds();
        screen.putExtra("diarySelected", data);
        screen.putExtra("diaryIdsSelected", ids);
        startActivityForResult(screen, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String reply = data.getStringExtra("ReturnedMessage");
        search.setText(reply);
    }

    public void emailAppChooser(Intent emailIntent){
        Intent intent = emailIntent;
        startActivity(Intent.createChooser(intent, "Send mail..."));
    }
}