package Lab3;

import java.util.ArrayList;

public class Semester {
    private ArrayList<Course> courses = new ArrayList<>();

    public void addCourse(Course c){
        courses.add(c);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }
}
