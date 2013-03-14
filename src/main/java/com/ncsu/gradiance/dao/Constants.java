package com.ncsu.gradiance.dao;

import java.util.Date;

public class Constants {
    private Constants(){}

    public static final String PROF_ROLE = "prof";
    public static final String STUDENT_ROLE = "student";
    public static final String TA = "ta";

    public static java.util.Date getTodaysDate(){
        return new java.util.Date();
    }

    public static java.sql.Date getTodaysSqlDate(){
        return new java.sql.Date(getTodaysDate().getTime()) ;
    }

}
