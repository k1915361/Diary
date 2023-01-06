package com.example.diary;

public class Diary {
    private String date;
    private String time;
    private String title;
    private String testField;
    private String pageFrom;
    private String pageTo;
    private String comments;
    private String commentsTeacher;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getCommentsTeacher() {
        return commentsTeacher;
    }

    public void setCommentsTeacher(String commentsTeacher) {
        this.commentsTeacher = commentsTeacher;
    }

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
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
}
