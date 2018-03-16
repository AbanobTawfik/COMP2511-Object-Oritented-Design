package com.company;
import java.lang.reflect.Array;
import java.util.Arrays;
/**
 * MY CRC CLASS for this case
 * <p>
 * Students enrol in courses that are offered in particular semesters
 * Students receive grades (pass, fail, etc.) for courses in particular semesters
 * Courses may have prerequisites (other courses) and must have credit point values
 * For a student to enrol in a course, s/he must have passed all prerequisite courses
 * Course offerings are broken down into multiple sessions (lectures, tutorials and labs)
 * Sessions in a course offering for a particular semester have an allocated room and timeslot
 * <p>
 * If a student enrols in a course, s/he must also enrol in some sessions of that course
 * COURSE
 * =====================================================================================================
 * RESPONIBILITES                               |                        COLLABORATION
 * =====================================================================================================
 * # Return a grade at the end of the semester  |   # student                       - students
 * # knows the pre-requisites for itself        |   # Lecturer in charge            - lectures
 * # keeping track of student performance       |   # tutors                        - tutes
 * # knows which session it is offered          |   # lab co-ordinators lab people  - labs
 * # checking if student is eligible to enrol   |   # buildings for the labs/lectures/tutes
 * # allocating rooms/timetable for student     |   # timetable
 * # allocating correct credit points to student|
 *                                              |
 *                                              |
 * =====================================================================================================
 * UML CLASS DIADRAM
 */


public class course {
    private int creditPoints;
    private String session;
    private course[] preRequisites;

    /**
     * class constructors and getters and setters
     */
    public course(int creditPoints, String session, course[] preRequisites) {
        this.creditPoints = creditPoints;
        this.session = session;
        this.preRequisites = preRequisites;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public course[] getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(course[] preRequisites) {
        this.preRequisites = preRequisites;
    }

    /**
     * CLASS METHODS
     */


    public boolean checkmetPrequisites(student s){
        return Arrays.asList(s.getCompleted()).containsAll(Arrays.asList(this.preRequisites));
    }

    public String assignMark(student s){
        int b = s.getResult();

        if(b < 50)
                return "FL";
        else if(b > 50 && b < 75)
                return "PS";
        else if(b > 65 && b < 75)
                return "CR";
        else if(b > 75 && b < 85)
                return "DN";
        else if(b > 85)
                return "HD";
        }

    public void assignCredits(student s){
        String result = assignMark(s);
        if(result != "FL") {
            s.setCompletedCreditPoints(s.getCreditPoints() + creditPoints);
        }
    }

}
