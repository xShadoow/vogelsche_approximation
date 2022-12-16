package main;

public class Warehouse {

    private static int CURRENT_ID = 1;

    private static int NEXT_ID() {
        return CURRENT_ID++;
    }

    private final int id;
    private int capacity;

    public Warehouse(int capacity) {
        this.id = NEXT_ID();
        this.capacity = capacity;
    }

    public int getId() {
        return this.id;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
