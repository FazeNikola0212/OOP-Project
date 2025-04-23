package Models;

public class Hall {
    private String name;
    private int rows;
    private int seatsPerRow;

    public Hall(String name, int rows, int seatsPerRow) {
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }
    public int getCapacity() {
        return rows * seatsPerRow;
    }
    public String getName() {
        return name;
    }
    public int getRows() {
        return rows;
    }
    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    @Override
    public String toString() {
        return "Hall{" +
                "name='" + name + '\'' +
                ", rows=" + rows +
                ", seatsPerRow=" + seatsPerRow +
                '}';
    }
}
