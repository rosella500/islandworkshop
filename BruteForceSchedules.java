package islandworkshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BruteForceSchedules extends ArrayList<Map.Entry<WorkshopSchedule, Integer>>
{
    //Should be private
    private List<Item> bestSubItems;
    private CycleSchedule bestRec;
    private int day;
    private int startingGroove;
    public BruteForceSchedules(List<Map.Entry<WorkshopSchedule, Integer>> list, int day, int startingGroove)
    {
        super(list);
        this.day =day;
        this.startingGroove = startingGroove;
    }

    public void setBestSubItems(List<Item> bestSubItems)
    {
        this.bestSubItems = bestSubItems;
        bestRec = new CycleSchedule(day, startingGroove);
        if(size() == 0)
            return;
        bestRec.setForFirstThreeWorkshops(get(0).getKey().getItems());
        bestRec.setFourthWorkshop(bestSubItems);
        if (!get(0).getKey().getItems().equals(bestSubItems) && bestSubItems.size() > 0)
        {
            int bestValue = bestRec.getWeightedValue();
            CycleSchedule all4Rec = new CycleSchedule(day, startingGroove);
            all4Rec.setForFirstThreeWorkshops(get(0).getKey().getItems());
            all4Rec.setFourthWorkshop(get(0).getKey().getItems());
            int all4Value = all4Rec.getWeightedValue();
            //System.out.println("4th WS different: "+bestValue+", all 4 the same: "+all4Value);

            if (all4Value > bestValue)
            {
                this.bestSubItems = get(0).getKey().getItems();
                bestRec = all4Rec;
            }
        }
    }

    public List<Item> getBestSubItems()
    {
        return bestSubItems;
    }

    public CycleSchedule getBestRec()
    {
        return bestRec;
    }
}