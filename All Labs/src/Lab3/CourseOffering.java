package Lab3;

import java.util.ArrayList;

public class CourseOffering {
    private Course course;
    private ArrayList<Course> prerequisites;

    public boolean enrol(Student s){
        if(s.getCourses().containsAll(prerequisites)){
            course.getSession().getLectures().get(0).addStudent(s);
            course.getSession().getLabs().get(0).addStudent(s);
            course.getSession().getTutorials().get(0).addStudent(s);
            return true;
        }
        return false;
    }
}
