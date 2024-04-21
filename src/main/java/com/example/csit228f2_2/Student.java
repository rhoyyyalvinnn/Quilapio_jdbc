package com.example.csit228f2_2;

public class Student {
    private String name;
    private String id;
    private String email;
    private String course;
    private boolean subjectOOP1;
    private boolean subjectRizal;
    private boolean subjectCalculus;

    public Student(String name, String id, String email, String course, boolean subjectOOP1, boolean subjectRizal, boolean subjectCalculus) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.course = course;
        this.subjectOOP1 = subjectOOP1;
        this.subjectRizal = subjectRizal;
        this.subjectCalculus = subjectCalculus;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public boolean isSubjectOOP1() {
        return subjectOOP1;
    }

    public void setSubjectOOP1(boolean subjectOOP1) {
        this.subjectOOP1 = subjectOOP1;
    }

    public boolean isSubjectRizal() {
        return subjectRizal;
    }

    public void setSubjectRizal(boolean subjectRizal) {
        this.subjectRizal = subjectRizal;
    }

    public boolean isSubjectCalculus() {
        return subjectCalculus;
    }

    public void setSubjectCalculus(boolean subjectCalculus) {
        this.subjectCalculus = subjectCalculus;
    }
}
