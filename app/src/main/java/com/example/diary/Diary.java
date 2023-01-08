package com.example.diary;

public class Diary {
    private String id;
    private String title;
    private String datetime;
    private String pageFrom;
    private String pageTo;
    private String comments;
    private String teacherComments;

    public Diary(String id, String title, String datetime, String pageFrom, String pageTo, String comments, String teacherComments){
        this.id = id;
        this.title = title;
        this.datetime = datetime;
        this.pageFrom = pageFrom;
        this.pageTo = pageTo;
        this.comments = comments;
        this.teacherComments = teacherComments;
    }

    public Diary(){;}

    public Diary id(String id) {
        this.id = id;
        return this;
    }

    public Diary title(String title) {
        this.title = title;
        return this;
    }
    
    public Diary dateTime(String datetime) {
        this.datetime = datetime;
        return this;
    }
    
    public Diary pageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
        return this;
    }
    
    public Diary pageTo(String pageTo) {
        this.pageTo = pageTo;
        return this;
    }
    
    public Diary comments(String comments) {
        this.comments = comments;
        return this;
    }
    
    public Diary teacherComments(String teacherComments) {
        this.teacherComments = teacherComments;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String datetime) {
        this.datetime = datetime;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(String pageFrom) {
        this.pageFrom = pageFrom;
    }

    public String getTeacherComment() {
        return teacherComments;
    }

    public void setTeacherComment(String teacherComments) {
        this.teacherComments = teacherComments;
    }

    public String getPageTo() {
        return pageTo;
    }

    public void setPageTo(String pageTo) {
        this.pageTo = pageTo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String toString(){
        return id
        +" "+ title
        +" "+ datetime
        +" "+ pageFrom
        +" "+ pageTo
        +" "+ comments
        +" "+ teacherComments;
    }

}
