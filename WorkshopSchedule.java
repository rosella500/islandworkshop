package islandworkshop;
import java.util.List;
import java.util.ArrayList;

public class WorkshopSchedule
{
    private List<ItemInfo> crafts;
    List<Integer> completionHours;
    public int currentIndex = 0; //Used for cycle scheduler to figure out crafts stuff
    
    
    public WorkshopSchedule(List<Item> crafts)
    {
        completionHours = new ArrayList<Integer>();
        this.crafts = new ArrayList<ItemInfo>();
        setCrafts(crafts);
    }
    
    public void setCrafts(List<Item> newCrafts)
    {
        crafts.clear();
        newCrafts.forEach(item -> {crafts.add(Solver.items[item.ordinal()]);});
        int currentHour = 0;
        completionHours.clear();
        for(ItemInfo craft : crafts)
        {
            currentHour+=craft.time;
            
            completionHours.add(currentHour);
        }
    }
    
    public ItemInfo getCurrentCraft()
    {
        if(currentIndex < crafts.size())
            return crafts.get(currentIndex);
        return null;
    }
    
    public boolean currentCraftCompleted(int hour)
    {
        if(completionHours.get(currentIndex) == hour)
            return true;
        return false;
    }
    
    public int getValueForCurrent(int day, int craftedSoFar, int currentGroove, boolean isEfficient)
    {
        ItemInfo craft = crafts.get(currentIndex);
        if(currentGroove > Solver.GROOVE_MAX)
            currentGroove = Solver.GROOVE_MAX;
        
        int baseValue = (int) (craft.baseValue * Solver.WORKSHOP_BONUS * (1.0+currentGroove/100.0));
        int supply = craft.getSupplyOnDay(day) + craftedSoFar;
        int adjustedValue = (int) (baseValue * craft.popularity.multiplier * ItemInfo.getSupplyBucket(supply).multiplier);
        
        if(isEfficient)
            adjustedValue *= 2;
        if(Solver.verboseLogging)
            System.out.println(craft.item+" is worth "+adjustedValue + " at hour "+completionHours.get(currentIndex) +" with "+currentGroove+" groove at "+ItemInfo.getSupplyBucket(craft.getSupplyOnDay(day) + craftedSoFar)+ " supply ("+supply+") and "+craft.popularity+" popularity");
        
        return adjustedValue;
    }
    
    public boolean currentCraftIsEfficient()
    {
        if(currentIndex > 0 && currentIndex < crafts.size())
            if(crafts.get(currentIndex).getsEfficiencyBonus(crafts.get(currentIndex-1)))
                return true;
        
        return false;
    }
    
    public int getMaterialCost()
    {
        int cost = 0;
        for(ItemInfo craft : crafts)
        {
            cost+=craft.materialValue;
        }
        return cost;
    }
}
