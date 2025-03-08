import java.io.*;
import java.util.*;
public class Ticket {
    private String event;
    private String date;
    private int row;
    private int seat;
    private boolean isSold;

    public Ticket(String event, String date, int row, int seat, boolean isSold) {
        this.event = event;
        this.date = date;
        this.row = row;
        this.seat = seat;
        this.isSold = isSold;
    }
    public void sell(){
        this.isSold = true;
    }
    public boolean isSold(){
        return isSold;
    }
    public String toFileFormat(){
        return event + " " + date + " " + row + " " + seat + " " + isSold;
    }
    @Override
    public String toString() {
        return "Ticket for " + event + " on " + date + " - Row: " + row + " Seat: " + seat + " (" + (isSold ? "Sold" : "Available") + ")";
    }
}
