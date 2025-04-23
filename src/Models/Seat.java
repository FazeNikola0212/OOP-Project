package Models;

public class Seat {
    private int row;
    private int seat;
    private boolean isBooked;
    private boolean isSold;
    private String note;

    public Seat(int row, int seat) {
        this.row = row;
        this.seat = seat;
        this.isBooked = false;
        this.isSold = false;
    }

    public boolean isAvailable() {
        return !isBooked && !isSold;
    }
    public void book(String note) {
        this.isBooked = true;
        this.note = note;
    }
    public void unbook() {
        this.isBooked = false;
        this.note = null;
    }
    public void buy() {
        this.isSold = true;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "row=" + row +
                ", seat=" + seat +
                ", isBooked=" + isBooked +
                ", isSold=" + isSold +
                ", note='" + note + '\'' +
                '}';
    }
}
