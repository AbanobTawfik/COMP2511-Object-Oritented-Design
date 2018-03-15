import java.util.*;

public class Rows {
    private char rowname;
    private ArrayList<Seats> chairs;
    private int numberOfSeatsInRow;
    private int numberOfAvailableSeatsInRow;

    public Rows(char rowname, int numberOfSeatsInRow) {
        this.rowname = rowname;
        this.numberOfSeatsInRow = numberOfSeatsInRow;
        chairs = new ArrayList<Seats>();
        for(int i = 0;i<numberOfSeatsInRow;i++)
            this.addSeatsToRow(1);
        //create an array of empty unbooked seats

    }

    public char getRowname() {
        return rowname;
    }

    public void setRowname(char rowname) {
        this.rowname = rowname;
    }

    public ArrayList<Seats> getChairs() {
        return chairs;
    }

    public void setChairs(ArrayList<Seats> chairs) {
        this.chairs = chairs;
    }

    public int getNumberOfSeatsInRow() {
        return numberOfSeatsInRow;
    }

    public void setNumberOfSeatsInRow(int numberOfSeatsInRow) {
        this.numberOfSeatsInRow = numberOfSeatsInRow;
    }

    public int getNumberOfAvailableSeatsInRow() {
        return numberOfAvailableSeatsInRow;
    }

    public void setNumberOfAvailableSeatsInRow(int numberOfAvailableSeatsInRow) {
        this.numberOfAvailableSeatsInRow = numberOfAvailableSeatsInRow;
    }

    public void addSeatsToRow(int numberofseats){
        for(int k = 0; k < numberofseats; k++)
            chairs.add(new Seats(k, false));

        numberOfAvailableSeatsInRow += numberofseats;
    }

    public int firstFreeSeatInRow(){
        int ret = -1;
        for(int k = 0;k < numberOfSeatsInRow; k++ ){
            if(chairs.get(k).checkReserveStatus() == false) {
                return k;
            }
        }
        return ret;
    }

}
