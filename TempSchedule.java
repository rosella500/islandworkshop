package islandworkshop;

public class TempSchedule {
    int grossValue;
    int netValue;

    public TempSchedule(int grossValue, int netValue) {
        this.grossValue = grossValue;
        this.netValue = netValue;
    }

    public int getGrossValue() {
        return grossValue;
    }

    public int getNetValue() {
        return netValue;
    }
}
