package com.example.diary;

import static com.example.diary.Helper.*;

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
    Button searchScrBtn;
    Button emailButton;
    String previousId = "";
    String previousTitle = "";
    String previousDatetime = "";
    String previousPageFrom = "";
    String previousPageTo = "";
    String previousComments = "";
    String previousCommentsTeacher = "";
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
        searchScrBtn = (Button) findViewById(R.id.searchScrBtn);
        emailButton = (Button) findViewById(R.id.emailScrBtn);
        diary = new Diary();
        datetime.setHint(currentDateTime());
        datetime.setText(currentDateTime());
        previousDatetime = text(datetime);
        viewSelectedDiary();
        Log.d(TAG, text(diaryView));
        diaryId.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousId = charSequence.toString();
            }
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousTitle = charSequence.toString();
            }
        });
        datetime.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousDatetime = charSequence.toString();
            }
        });
        pageFrom.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousPageFrom = charSequence.toString();
            }
        });
        pageTo.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousPageTo = charSequence.toString();
            }
        });
        comments.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousComments = charSequence.toString();
            }
        });
        commentsTeacher.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isEmpty(search))
                    previousCommentsTeacher = charSequence.toString();
            }
        });
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
            }
        });
        searchScrBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchScrnBtnPressed(v);
            }
        });
        search.addTextChangedListener(new TextWatcher(){
            @Override public void afterTextChanged(Editable s) {;}
            @Override public void beforeTextChanged(CharSequence s, int start, int before, int count) {;}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewDiaryBySearchAny();
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
                +nullToStr(p1,"title ")
                +nullToStr(p2,"datetime ")
                +nullToStr(p3,"pageFrom ")
                +nullToStr(p4,"pageTo ")
                +nullToStr(p5,"comments ")
                +nullToStr(p6,"TeacherComments ");
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
            diaryId.setText(d.getId());
            title.setText(d.getTitle());
            datetime.setText(d.getDateTime());
            pageFrom.setText(d.getPageFrom());
            pageTo.setText(d.getPageTo());
            comments.setText(d.getComments());
            commentsTeacher.setText(d.getTeacherComment());
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
                viewSelectedDiary();
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

    /*  Search by pageRange eg 1 88
        search by Ids eg 3,6,9
        search by book, date, comments

        Sample data
        1 Mars              07-01-2023_00:35 3 4    my comments and rating +++++  teacher's comments
        2 Man in the mirror 07-01-2023_00:35 2 9    my rating +++++  teacherA
        3 The Glory         07-01-2023_00:35 3 9    my rating +++++  teacherB
        4 Charlie Chaplin   07-01-2023_00:35 4 88   my rating ++++  teacherC
        5 The mice and man  07-01-2023_00:35 5 6    my rating +++  teacherD
        6 The cat in boots  07-01-2023_00:35 6 7    my rating ++  teacherE
        7 The fairy tale    06-01-2023_23:30 7 99   my rating + teacherF
        8 Land Of Oz        06-01-2023_23:30 8 9   my rating ++++ parentA
    */
    public void viewDiaryBySearchAny(View... view) {
        String srch = text(search);
        if(srch.isEmpty()){
            viewSelectedDiary();
            bringBackPreviousInputs();
        } else {
            String data = "";
            data = helper.selectByPageRange(srch);
            if(data.isEmpty()){
                data = helper.selectByIds(srch);
            }
            if(data.isEmpty() && isInt(srch)) {
                data = helper.getById(srch);
            }
            if(data.isEmpty()) {
                data = helper.searchAny(srch);
            }
            if(data.isEmpty()){
                viewSelectedDiary();
            } else {
                diaryView.setText(data);
                diaryId.setText(idOfRecord(data));
                autoFill();
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
            toast(this, ""+data);
            if(!data.isEmpty()) return data;
            if(data.isEmpty() && isInt(sch)) {
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
            records[i] = idOfRecord(records[i]);
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
            toast(this, ""+data);
            if(!data.isEmpty()) return data;
            if(data.isEmpty() && isInt(sch)) {
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
            if(data.isEmpty() && isInt(sch)) {
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
                +nullToStr(id,"diaryID ")
                +nullToStr(u1,"title ")
                +nullToStr(u2,"datetime ")
                +nullToStr(u3,"pageFrom ")
                +nullToStr(u4,"pageTo ")
                +nullToStr(u5,"comments ")
                +nullToStr(u6,"TeacherComments ");

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

    public boolean isAnyPreviousInputsEmpty(){
        if(isEmpty(previousId)
                || isEmpty(previousTitle)
                || isEmpty(previousPageFrom)
                || isEmpty(previousPageTo)
                || isEmpty(previousDatetime)
                || isEmpty(previousComments)
                || isEmpty(previousCommentsTeacher)){
            return true;
        }
        return false;
    }

    public boolean isAllPreviousInputsFilled(){
        if(isEmpty(previousId)
                && isEmpty(previousTitle)
                && isEmpty(previousPageFrom)
                && isEmpty(previousPageTo)
                && isEmpty(previousDatetime)
                && isEmpty(previousComments)
                && isEmpty(previousCommentsTeacher)){
            return false;
        }
        return true;
    }


    public void rememberInputs(){
        previousId = text(diaryId);
        previousTitle = text(title);
        previousDatetime = text(datetime);
        previousPageFrom = text(pageFrom);
        previousPageTo = text(pageTo);
        previousComments = text(comments);
        previousCommentsTeacher = text(commentsTeacher);
    }

    public boolean isAllFieldsEmpty(){
        if( isEmpty(diaryId) &&
                isEmpty(title) &&
                isEmpty(datetime) &&
                isEmpty(pageFrom) &&
                isEmpty(pageTo) &&
                isEmpty(comments) &&
                isEmpty(commentsTeacher)){
            return true;
        }
        return false;
    }

    public void emptyAllFields(){
        diaryId.setText("");
        title.setText("");
        pageFrom.setText("");
        pageTo.setText("");
        comments.setText("");
        commentsTeacher.setText("");
    }

    public void viewDiaryOnStartUp(View view){
        String data = helper.getData();
        diaryView.setText(DiaryViewHeader+data);
    }
}