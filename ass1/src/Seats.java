
public class Seats {
    private int seatnumber;
    private boolean reserveStatus;

    public Seats(int seatnumber, boolean reserveStatus) {
        this.seatnumber = seatnumber;
        this.reserveStatus = reserveStatus;
    }

    public int getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(int seatnumber) {
        this.seatnumber = seatnumber;
    }

    public boolean reserveStatus() {
        return reserveStatus;
    }

    public void setReserved(boolean reserved) {
        reserveStatus = reserved;
    }

    public boolean checkReserveStatus(){
        return reserveStatus;
    }

    public void reserveSeat(){
        reserveStatus = true;
    }

    public void removeReservedSeat(){
        reserveStatus = false;
    }
}
