package com.company;
import java.util.Arrays;
public class student extends course {
    private int[] completed;
    private int result;
    private int completedCreditPoints;
    private int tutorialTimes[];
    private String tutors[];
    private int lectureTime[];
    private String lecturer;
    private int labTimes[];
    private String labInstructor;

    public student(int creditPoints, String session, course[] preRequisites, course[] completed, int result, int completedCreditPoints) {
        super(creditPoints, session, preRequisites);
        this.completed = completed;
        this.result = result;
        this.completedCreditPoints = new ArrayList<completedCreditPoints>();
    }

    public course[] getCompleted() {
        return completed;
    }

    public void setCompleted(course[] completed) {
        this.completed = completed;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getCompletedCreditPoints() {
        return completedCreditPoints;
    }

    public void setCompletedCreditPoints(int completedCreditPoints) {
        this.completedCreditPoints = completedCreditPoints;
    }

    public int[] getTutorialTimes() {
        return tutorialTimes;
    }

    public void setTutorialTimes(int[] tutorialTimes) {
        this.tutorialTimes = tutorialTimes;
    }

    public String[] getTutors() {
        return tutors;
    }

    public void setTutors(String[] tutors) {
        this.tutors = tutors;
    }

    public int[] getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(int[] lectureTime) {
        this.lectureTime = lectureTime;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int[] getLabTimes() {
        return labTimes;
    }

    public void setLabTimes(int[] labTimes) {
        this.labTimes = labTimes;
    }

    public String getLabInstructor() {
        return labInstructor;
    }

    public void setLabInstructor(String labInstructor) {
        this.labInstructor = labInstructor;
    }
}
