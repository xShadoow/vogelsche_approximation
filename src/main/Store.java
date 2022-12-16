package main;

public class Store {

    private static int CURRENT_ID = 1;

    private static int NEXT_ID() {
        return CURRENT_ID++;
    }

    private final int id;
    private int demand;
    private final int[] effortUnits;

    public Store(int demand, int[] effortUnits) {
        this.id = NEXT_ID();
        this.demand = demand;
        this.effortUnits = effortUnits;
    }

    public int getId() {
        return this.id;
    }

    public int getDemand() {
        return this.demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int[] getEffortUnits() {
        return this.effortUnits;
    }

}
