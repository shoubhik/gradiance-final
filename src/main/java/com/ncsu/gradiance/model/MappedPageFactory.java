package com.ncsu.gradiance.model;

import java.util.HashMap;
import java.util.Map;

public class MappedPageFactory {
    private Map<String, String> profMappedPages;
    private Map<String, String> studentMappedPages;
    private Map<String, String> taMappedPages;

    public MappedPageFactory(){
        profMappedPages = new HashMap<String, String>();
        profMappedPages.put("home", "profHome");
        profMappedPages.put("selectCourse", "profSelectCourse");
        profMappedPages.put("afterCourseSelected", "profAfterCourseSelected");
        profMappedPages.put("addCourse", "profAddCourse");
        profMappedPages.put("courseAdded", "profCourseAdded");
        profMappedPages.put("addCourseFailure", "addCourseFailure");

        studentMappedPages = new HashMap<>();
        studentMappedPages.put("home", "profHome");
        studentMappedPages.put("addCourse", "studentAddCourse");
        studentMappedPages.put("courseAdded", "profCourseAdded");
        studentMappedPages.put("addCourseFailure", "addCourseFailure");
        studentMappedPages.put("selectCourse", "profSelectCourse");
        studentMappedPages.put("afterCourseSelected", "studentAfterCourseSelected");

        taMappedPages = new HashMap<>();
        taMappedPages.put("home", "profHome");
        taMappedPages.put("addCourse", "studentAddCourse");
        taMappedPages.put("courseAdded", "profCourseAdded");
        taMappedPages.put("addCourseFailure", "addCourseFailure");
        taMappedPages.put("selectCourse", "profSelectCourse");
        taMappedPages.put("afterCourseSelected", "taAfterCourseSelected");

    }

    public Map<String, String> getStudentPages(){
        return this.studentMappedPages;
    }

    public Map<String, String> getTAPages(){
        return this.taMappedPages;
    }

    public Map<String, String> getProfPages(){
        return this.profMappedPages;
    }

}
