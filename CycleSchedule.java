package islandworkshop;

import java.util.List;
import java.util.HashMap;
public class CycleSchedule
{
    int day;
    int startingGroove;
    int endingGroove;
    WorkshopSchedule[] workshops = new WorkshopSchedule[3];
    HashMap<Item, Integer> numCrafted;
    
    public CycleSchedule(int day, int groove)
    {
        this.day = day;
        startingGroove = groove;
    }
    
    public void setForAllWorkshops(List<Item> crafts)
    {
        workshops[0] = new WorkshopSchedule(crafts);
        workshops[1] = new WorkshopSchedule(crafts);
        workshops[2] = new WorkshopSchedule(crafts);
    }
    
    public void setWorkshop(int index, List<Item> crafts)
    {
        if(workshops[index] == null)
            workshops[index] = new WorkshopSchedule(crafts);
        else
            workshops[index].setCrafts(crafts);
    }
    
    public int getValue() 
    {
       numCrafted = new HashMap<Item, Integer>(); 
       
       for(int i=0; i<workshops.length;i++)
           workshops[i].currentIndex = 0;
       
       int currentGroove = startingGroove;
       
       int totalCowries = 0;
       for(int hour = 4; hour <=24; hour+=2) //Nothing can finish until hour 4
       {
           HashMap<Item, Integer> craftsToAdd = new HashMap<Item,Integer>();
           int grooveToAdd = 0;
           int cowriesThisHour = 0;
           for(int i=0; i<workshops.length;i++)
           {
               if(workshops[i].currentCraftCompleted(hour))
               {
                   ItemInfo completedCraft = workshops[i].getCurrentCraft();
                   boolean efficient = workshops[i].currentCraftIsEfficient();
                   craftsToAdd.put(completedCraft.item, craftsToAdd.getOrDefault(completedCraft.item, 0) + (efficient? 2 : 1));
                   
                   //System.out.println("Found completed "+completedCraft.item+" at hour "+hour+". Efficient? "+efficient);
                   
                   cowriesThisHour += workshops[i].getValueForCurrent(day, numCrafted.getOrDefault(completedCraft.item, 0), currentGroove, efficient);
                   
                   workshops[i].currentIndex++;
                   if(workshops[i].currentCraftIsEfficient())
                       grooveToAdd++;
               }
           }
           if(Solver.verboseCalculatorLogging && cowriesThisHour>0)
               System.out.println("hour "+hour+": "+cowriesThisHour);
           
           totalCowries += cowriesThisHour;
           currentGroove += grooveToAdd;
           if(currentGroove > Solver.GROOVE_MAX)
               currentGroove = Solver.GROOVE_MAX;
           craftsToAdd.forEach((k, v) ->  {numCrafted.put(k, numCrafted.getOrDefault(k, 0) + v); });
           
       }
       
       endingGroove = currentGroove;
       
       return totalCowries;
       
    }
    
    public int getSingleWorkshopValue(int index)
    {
        int value = 0;
        numCrafted = new HashMap<Item, Integer>(); 
        workshops[index].currentIndex = 0;
        for(int i=0; i<workshops[index].getNumCrafts(); i++)
        {
            ItemInfo completedCraft = workshops[index].getCurrentCraft();
            boolean efficient = workshops[index].currentCraftIsEfficient();
            value += workshops[index].getValueForCurrent(day, numCrafted.getOrDefault(completedCraft.item, 0), i*3, efficient);
            workshops[index].currentIndex++;
            numCrafted.put(completedCraft.item, numCrafted.getOrDefault(completedCraft.item, 0) + 3);
        }
        return value;
    }
    
    public int getValueWithGrooveEstimate()
    {
        int craftsAbove4 = 0;
        craftsAbove4+=workshops[0].getNumCrafts()-4;
        int daysToGroove = 5-day;
        if(!Solver.rested)
            daysToGroove--;
        
        if(daysToGroove < 0)
            daysToGroove = 0;
                
        return craftsAbove4 * daysToGroove * Solver.groovePerDay + getSingleWorkshopValue(0);
    }
    
    public int getMaterialCost()
    {
        int cost = 0;
        for(WorkshopSchedule shop : workshops)
        {
            cost+=shop.getMaterialCost();
        }
        return cost;
    }
    
    public boolean equals(Object other)
    {
        if(other instanceof CycleSchedule)
        {
            return workshops.equals(((CycleSchedule)other).workshops);
        }
        return false;
    }
    
    public int hashCode()
    {
        return workshops.hashCode();
    }
    
}
