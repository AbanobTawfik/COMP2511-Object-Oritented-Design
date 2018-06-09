package Lab3;

import java.util.ArrayList;

public class Course {
    private Session s;
    private int credits;
    private int grade;
    private String gradeChar;
    private ArrayList<Student> students;

    public void addLecture(Lecture l){
        s.getLectures().add(l);
    }

    public void addTutorial(Tutorial t){
        s.getTutorials().add(t);
    }

    public void addLab(Lab l){
        s.getLabs().add(l);
    }

    public void grade(Student s){
        int index = s.getCourses().indexOf(this);
        s.getCourses().get(index).grade = 50;
        s.getCourses().get(index).gradeChar = "PS";
    }

    public Session getSession(){
        return s;
    }
}
