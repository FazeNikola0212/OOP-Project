package Models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Event {
    private String name;
    private LocalDate date;
    private Hall hall;
    private Map<Integer, Map<Integer, Seat>> seats;

    public Event(String name, LocalDate date, Hall hall) {
        this.name = name;
        this.date = date;
        this.hall = hall;
        this.seats = new HashMap<>();

        for (int r = 1; r <= hall.getRows(); r++) {
            seats.put(r, new HashMap<>());
            for (int s = 1; s <= hall.getSeatsPerRow(); s++) {
                seats.get(r).put(s, new Seat(r, s));
            }
        }
    }

    public String getName() {
        return name;
    }
    public LocalDate getDate() {
        return date;
    }
    public Hall getHall() {
        return hall;
    }
    public Seat getSeat(int row, int seat) { return seats.get(row).get(seat); }
    public String getHallName(){
        return hall.getName();
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", hall=" + hall +
                '}';
    }
}
