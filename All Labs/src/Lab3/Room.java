package Lab3;

import java.util.ArrayList;
import java.util.Date;

public abstract class Room {
    private Date time;
    private int roomId;
    private ArrayList<Student> students = new ArrayList<Student>();

    public Room(Date time, int roomId) {
        this.time = time;
        this.roomId = roomId;
    }

    public Date getTime() {
        return time;
    }

    public int getRoomId() {
        return roomId;
    }

    public void addStudent(Student s){
        students.add(s);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
