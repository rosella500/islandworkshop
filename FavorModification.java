package islandworkshop;

import java.util.Comparator;

public class FavorModification implements Comparable<FavorModification>
{
    private CycleSchedule schedule;
    private Item favor;
    private int numWorkshops;
    private WorkshopSchedule workshop;

    private int value;
    private int delta;

    public FavorModification(CycleSchedule schedule, Item favor, int numWorkshops, WorkshopSchedule workshop, int value) {
        this.schedule = schedule;
        this.favor = favor;
        this.numWorkshops = numWorkshops;
        this.workshop = workshop;
        this.value = value;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public CycleSchedule getSchedule() {
        return schedule;
    }

    public Item getFavor() {
        return favor;
    }

    public int getNumWorkshops() {
        return numWorkshops;
    }

    public WorkshopSchedule getWorkshop() {
        return workshop;
    }

    public int getDelta() {
        return delta;
    }

    public int getValue() {
        return value;
    }
    public int compareTo(FavorModification o)
    {
        return ((Integer)value).compareTo(o.value);
    }
}
