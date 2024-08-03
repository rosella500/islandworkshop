package islandworkshop;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CycleSchedule
{
    int day;
    int startingGroove;
    int endingGroove;
    int grooveBonus = -1;
    int otherBonus = -1;
    WorkshopSchedule[] workshops = new WorkshopSchedule[Solver.NUM_WORKSHOPS];
    HashMap<Item, Integer> numCrafted;
    
    public CycleSchedule(int day, int groove)
    {
        this.day = day;
        startingGroove = groove;
    }
    
    public void setForFirstThreeWorkshops(List<Item> crafts)
    {
        workshops[0] = new WorkshopSchedule(crafts);
        workshops[1] = new WorkshopSchedule(crafts);
        workshops[2] = new WorkshopSchedule(crafts);
        if(workshops.length > 3 && workshops[3] == null)
            workshops[3] = new WorkshopSchedule(new ArrayList<>());
    }
    public void setFourthWorkshop(List<Item> crafts)
    {
        if(workshops.length > 3)
            workshops[3] = new WorkshopSchedule(crafts);
    }


    public void setWorkshop(int index, List<Item> crafts)
    {
        if(workshops[index] == null)
            workshops[index] = new WorkshopSchedule(crafts);
        else
            workshops[index].setCrafts(crafts);
    }

    public int getTrueGroovelessValue()
    {
        if(workshops[0] == null)
            return 0;
        for(int i=0; i<workshops.length;i++)
            workshops[i].currentIndex = 0;

        int value = 0;
        Map<Item, Integer> numCrafted = new HashMap<>();
        for(int hour = 4; hour <=24; hour+=2) //Nothing can finish until hour 4
        {
            HashMap<Item, Integer> craftsToAdd = new HashMap<>();
            int cowriesThisHour = 0;
            for(int i=0; i<workshops.length;i++)
            {
                if(workshops[i].currentCraftCompleted(hour))
                {
                    ItemInfo completedCraft = workshops[i].getCurrentCraft();
                    boolean efficient = workshops[i].currentCraftIsEfficient();
                    craftsToAdd.put(completedCraft.item, craftsToAdd.getOrDefault(completedCraft.item, 0) + (efficient? 2 : 1));

                    //System.out.println("Found completed "+completedCraft.item+" at hour "+hour+". Efficient? "+efficient);

                    cowriesThisHour += workshops[i].getValueForCurrent(day, numCrafted.getOrDefault(completedCraft.item, 0), 0, efficient, Solver.verboseCalculatorLogging);

                    workshops[i].currentIndex++;
                }
            }
            if(Solver.verboseCalculatorLogging && cowriesThisHour>0)
                System.out.println("hour "+hour+": "+cowriesThisHour);

            value += cowriesThisHour;
            craftsToAdd.forEach((k, v) ->  {numCrafted.put(k, numCrafted.getOrDefault(k, 0) + v); });

        }
        return value;
    }
    
    public int getValue() 
    {
        numCrafted = new HashMap<>();
        if(workshops == null || workshops.length == 0 || workshops[0] == null || workshops[0].getItems() == null || workshops[0].getItems().size() == 0)
            return 0;
       
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
                   
                   cowriesThisHour += workshops[i].getValueForCurrent(day, numCrafted.getOrDefault(completedCraft.item, 0), currentGroove, efficient, Solver.verboseCalculatorLogging);
                   
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
    public List<Item> getItems()
    {
        return workshops[0].getItems();
    }
    public List<Item> getSubItems() { return workshops.length>3?workshops[3].getItems():new ArrayList<>(); }
    
    public int getMaterialCost()
    {
        int cost = 0;
        for(WorkshopSchedule shop : workshops)
        {
            if(shop != null)
                cost+=shop.getMaterialCost();
        }
        return cost;
    }

    public void addMaterials(Map<RareMaterial, Integer> matsUsed)
    {
        for(var workshop : workshops)
        {
            for(var mat : workshop.rareMaterialsRequired.entrySet())
            {
                matsUsed.put(mat.getKey(), matsUsed.getOrDefault(mat.getKey(), 0)+mat.getValue());
            }
        }
    }

    public int getGrooveBonus()
    {
        if(grooveBonus >= 0)
            return grooveBonus;

        grooveBonus = 0;
        for(int i=0;i<workshops.length;i++)
        {
            var workshop = workshops[i];
            workshop.getValueWithGrooveEstimate(day, startingGroove, i>=3);
            grooveBonus+= workshop.getGrooveValue();
            otherBonus += workshop.otherBonus;
        }
        return grooveBonus;
    }
    public int getWeightedValue()
    {
        return getValue() + getGrooveBonus() + otherBonus - (int)(getMaterialCost() * Solver.materialWeight);
    }

    public Map<Item, Integer> getLimitedUses(Map<Item, Integer> limitedUse)
    {
        limitedUse = workshops[0].getLimitedUses(limitedUse, false);
        if(workshops.length>3)
            limitedUse = workshops[3].getLimitedUses(limitedUse, true);
        return limitedUse;
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

    @Override
    public String toString()
    {
        return "Day: "+(day+1)+", Items: " + workshops[0].getItems() + " Sub items: "+(workshops.length>3?workshops[3].getItems():null)+", Starting groove: "+startingGroove+", Ending groove: "+endingGroove;
    }

    public String printWorkshops()
    {
        return "WS1: "+workshops[0].getItems()+"\n"
                +"WS2: "+workshops[1].getItems()+"\n"
                +"WS3: "+workshops[2].getItems()+"\n"
                +"WS4: "+workshops[3].getItems();
    }
    
}
