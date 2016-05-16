// Copyright 2016, Bruno Terkaly, All Rights Reserved
package com.terkaly.JavaMongoDB;

public class Course {
    public Course(int id, String coursenumber, String coursetitle, Rating rating) {
        super();
        this.id = id;
        this.coursenumber = coursenumber;
        this.coursetitle = coursetitle;
        this.rating = rating;
    }
    private int id;
    private String coursenumber;
    private String coursetitle;
    public int getCourseId() {
        return id;
    }
    private Rating rating;
}