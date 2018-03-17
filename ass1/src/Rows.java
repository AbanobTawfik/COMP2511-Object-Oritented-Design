import java.util.*;

public class Rows {
    private String rowName;
    private ArrayList<Seats> chairs;
    private int numberOfSeatsInRow;
    private int numberOfAvailableSeatsInRow;

    public Rows(String rowName, int numberOfSeatsInRow) {
        this.rowName = rowName;
        this.numberOfSeatsInRow = numberOfSeatsInRow;
        chairs = new ArrayList<Seats>();
        for(int i = 0;i<numberOfSeatsInRow;i++)
            this.addSeatsToRow(1);
    }

    /**
     *
     * @return the name of the row (name of row)
     */
    public String getRowName() {
        return rowName;
    }

    /**
     *
     * @return all seats in the row
     */
    public ArrayList<Seats> getChairs() {
        return chairs;
    }

    /**
     *
     * @return the number of seats in the row
     */
    public int getNumberOfSeatsInRow() {
        return numberOfSeatsInRow;
    }

    /**
     *
     * @return the number of unreserved seats in the row
     */
    public int getNumberOfAvailableSeatsInRow() {
        return numberOfAvailableSeatsInRow;
    }

    /**
     * function name says it all
     * @param numberOfAvailableSeatsInRow number of seats
     */
    public void setNumberOfAvailableSeatsInRow(int numberOfAvailableSeatsInRow) {
        this.numberOfAvailableSeatsInRow = numberOfAvailableSeatsInRow;
    }

    /**
     * adds seats to a row
     * @param numberofseats number of seats to be added
     */
    public void addSeatsToRow(int numberofseats){
        for(int k = 0; k < numberofseats; k++)
            chairs.add(new Seats(k, false));

        numberOfAvailableSeatsInRow += numberofseats;
    }

    /**
     * This function is designed to find the index of the first free seat in the row
     * @return ths index of the first free seat in the row
     */
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
