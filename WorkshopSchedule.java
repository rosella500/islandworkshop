package islandworkshop;
import java.util.*;
import java.util.Map.Entry;

import static islandworkshop.Solver.reservedHelpers;
import static islandworkshop.Solver.rested;

public class WorkshopSchedule
{
    private List<ItemInfo> crafts;
    private List<Item> items; //Just a dupe of crafts, but accessible
    List<Integer> completionHours;
    public int currentIndex = 0; //Used for cycle scheduler to figure out crafts stuff
    public int grooveValue =0;
    public int otherBonus = 0;

    Map<RareMaterial,Integer> rareMaterialsRequired;
    
    public WorkshopSchedule(List<Item> crafts)
    {
        completionHours = new ArrayList<Integer>();
        this.crafts = new ArrayList<ItemInfo>();
        items = new ArrayList<Item>();
        rareMaterialsRequired = new HashMap<>();
        setCrafts(crafts);
    }
    
    public void setCrafts(List<Item> newCrafts)
    {
        crafts.clear();
        newCrafts.forEach(item -> {crafts.add(Solver.items[item.ordinal()]);});
        int currentHour = 0;
        completionHours.clear();
        items.clear();
        for(ItemInfo craft : crafts)
        {
            currentHour+=craft.time;
            items.add(craft.item);
            
            completionHours.add(currentHour);
            
            if(craft.materialsRequired!= null)
                for(Entry<RareMaterial, Integer> indivMat : craft.materialsRequired.entrySet())
                    rareMaterialsRequired.put(indivMat.getKey(), rareMaterialsRequired.getOrDefault(indivMat.getKey(), 0) + indivMat.getValue()); 
        }
        
    }
    
    public ItemInfo getCurrentCraft()
    {
        if(currentIndex < crafts.size())
            return crafts.get(currentIndex);
        return null;
    }
    
    public int getNumCrafts()
    {
        return crafts.size();
    }
    
    public List<Item> getItems()
    {
        return items;
    }
    
    public boolean currentCraftCompleted(int hour)
    {
        if(currentIndex >= crafts.size())
            return false;
        
        if(completionHours.get(currentIndex) == hour)
            return true;
        return false;
    }
    
    public int getValueForCurrent(int day, int craftedSoFar, int currentGroove, boolean isEfficient, boolean verbose)
    {
        ItemInfo craft = crafts.get(currentIndex);        
        int baseValue = craft.baseValue * Solver.WORKSHOP_BONUS * (100+currentGroove) / 10000;
        int supply = craft.getSupplyOnDay(day) + craftedSoFar;
        int adjustedValue = baseValue * craft.popularity.multiplier * ItemInfo.getSupplyBucket(supply).multiplier  / 10000;
        if(verbose)
            System.out.println(craft.item+": Found "+craft.getSupplyOnDay(day)+" supply with "+craftedSoFar+" crafts done today");
        
        if(isEfficient)
            adjustedValue *= 2;
        if(verbose)
            System.out.println(craft.item+" is worth "+adjustedValue +" with "+currentGroove+" groove at "+ItemInfo.getSupplyBucket(supply)+ " supply ("+supply+") and "+craft.popularity+" popularity");
        
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
    public int getGrooveValue()
    {
        return grooveValue;
    }

    public int getValueWithGrooveEstimate(int day, int startingGroove)
    {
        boolean verboseLogging = false;

        if(Solver.schedulesToCheck != null && Solver.schedulesToCheck.containsKey(day) && Solver.schedulesToCheck.get(day).contains(items))
            verboseLogging = true;

        if(verboseLogging)
            System.out.println("Getting value for workshop making "+Arrays.toString(items.toArray()));

        int expectedGroove = 3;
        int effCrafts = 0;
        for(int i=1; i< crafts.size(); i++)
        {
            if(crafts.get(i-1).getsEfficiencyBonus(crafts.get(i)))
                effCrafts++;
        }
        int deltaGroove = effCrafts - expectedGroove;
        int daysToGroove = 6 - day;
        if (!rested)
            daysToGroove--;

        //How many days will it take to hit max normally
        int estimatedGroovePerDay = expectedGroove * Solver.NUM_WORKSHOPS;
        int expectedStartingGroove = startingGroove + estimatedGroovePerDay;

        boolean groovePenalty = false;

        if (deltaGroove < 0)
        {
            groovePenalty = true;
            expectedStartingGroove += Solver.NUM_WORKSHOPS * deltaGroove;

            if(expectedStartingGroove < 0)
                expectedStartingGroove = 0;

            deltaGroove *= -1;
        }

        float grooveBonus = 0;
        for (int i = 0; i < deltaGroove; i++)
        {
            int craftingDaysLeft = daysToGroove;
            int fullDays = 0;
            int numRowsOfPartialDay = 0;
            int expectedEndingGroove = expectedStartingGroove;
            while (craftingDaysLeft > 0 && expectedEndingGroove < Solver.GROOVE_MAX)
            {
                if (expectedEndingGroove + estimatedGroovePerDay + Solver.NUM_WORKSHOPS - 1 <= Solver.GROOVE_MAX)
                {
                    fullDays++;
                    expectedEndingGroove += estimatedGroovePerDay;
                    craftingDaysLeft--;
                }
                else
                {
                    int grooveToGo = Solver.GROOVE_MAX - expectedEndingGroove;
                    numRowsOfPartialDay = (grooveToGo + 1) / Solver.NUM_WORKSHOPS;
                    expectedEndingGroove = Solver.GROOVE_MAX;
                }
            }

            switch (numRowsOfPartialDay) {
                case 1 -> grooveBonus += fullDays + 0.10f;
                case 2 -> grooveBonus += fullDays + .5f;
                case 3 -> grooveBonus += fullDays + .60f;
                case 4 -> grooveBonus += fullDays + 1;
                default -> grooveBonus += fullDays;
            }

            expectedStartingGroove+=Solver.NUM_WORKSHOPS;
            //if(verboseLogging)
                //System.out.println("Groove bonus "+grooveBonus+"% over "+daysToGroove+" days, with the last day giving "+numRowsOfPartialDay+" rows");
        }
        float valuePerDay = Solver.averageDayValue;

        grooveBonus = (grooveBonus * valuePerDay) / 100f;

        if (groovePenalty)
            grooveBonus *= -1;

        grooveValue = 0;

        if (daysToGroove > 0 && grooveBonus != 0)
        {
            grooveValue = (int)grooveBonus;
        }

        int workshopValue = 0;
        HashMap<Item,Integer> numCrafted = new HashMap<>();
        currentIndex = 0;
        for(int i=0; i<getNumCrafts(); i++)
        {
            ItemInfo completedCraft = getCurrentCraft();
            boolean efficient = currentCraftIsEfficient();
            int previouslyCrafted = numCrafted.getOrDefault(completedCraft.item, 0);
            int nextGroove = Math.min(startingGroove + i*Solver.NUM_WORKSHOPS, Solver.GROOVE_MAX);
            workshopValue += getValueForCurrent(day, previouslyCrafted, nextGroove, efficient, verboseLogging);
            currentIndex++;
            int amountCrafted = efficient? Solver.NUM_WORKSHOPS*2 : Solver.NUM_WORKSHOPS;
            numCrafted.put(completedCraft.item, previouslyCrafted + amountCrafted);

        }


        //Figure out if a penalty should apply for using a future item
        int helperPenalty = 0;
        for(var kvp : reservedHelpers.entrySet())
        {
            ItemInfo mainItem = Solver.items[kvp.getKey().ordinal()];
            if(items.contains(kvp.getKey())) //We're using the main item so it's fine
                continue;
            if(mainItem.peaksOnOrBeforeDay(day)) //Item has peaked already so it's fine
                continue;
            if(!items.contains(kvp.getValue().item)) //We aren't using the helper so it's fine
                continue;

            
            //None of the above conditions are true so it's not fine.
            //apply a penalty for x usages (2x if efficient)
            for(int i=0; i<items.size(); i++)
            {
                if(items.get(i) == kvp.getValue().item)
                {
                    if(verboseLogging)
                        System.out.println("Not using main item "+kvp.getKey()+" that hasn't peaked yet, but are using its helper "+kvp.getValue().item+". Adding penalty of "+(i==0?1:2)+"x "+kvp.getValue().penalty);
                    helperPenalty+=kvp.getValue().penalty*(i==0?1:2);
                }
            }
        }


        int prepeakBonus = 0;
        for(int i=0;i<crafts.size();i++)
        {
            if(crafts.get(i).couldPrePeak(day))
                prepeakBonus+= Solver.helperPenalty*(i==0?1:2);
        }

        int total = workshopValue + grooveValue - (int)(getMaterialCost() * Solver.materialWeight) - helperPenalty + prepeakBonus;
        if(verboseLogging)
            System.out.println("Day "+(day+1)+", "+startingGroove+" starting groove. Workshop value: "+workshopValue+", grooveBonus: "+grooveValue+", material cost: "+getMaterialCost()+" x"+Solver.materialWeight+", helper penalty: "+helperPenalty+", prepeak bonus: "+prepeakBonus+", total: "+total);

        otherBonus = prepeakBonus - helperPenalty;
        //Allow for the accounting for materials if desired
        return total;
    }
    
    
    public boolean usesTooMany(Map<Item,Integer> limitedUse, boolean sub)
    {
        if(limitedUse == null)
            return false;
        boolean tooMany = false;
       
        Map<Item, Integer> used = new HashMap<Item,Integer>();
        int numWorkshops = sub?1:3;
            
            
        for(int i=0; i<items.size(); i++)
        {
            used.put(items.get(i), used.getOrDefault(items.get(i), 0) + numWorkshops+(i>0?numWorkshops:0));
        }
        for(Item key : used.keySet())
        {
            if(limitedUse.containsKey(key) && limitedUse.get(key) < used.get(key))
            {
//                if(Solver.verboseSolverLogging)
//                    System.out.println("Using too many "+key+" in schedule "+items+". Can only use "+limitedUse.get(key)+" but using "+used.get(key));
                return true;
            }
        }
        return false;
    }

    public Map<Item, Integer> getLimitedUses(Map<Item,Integer> previousLimitedUses, boolean subSchedule)
    {
        int numWorkshops = 3;
        if(subSchedule)
            numWorkshops = 1;
        Map<Item,Integer> limitedUses;
        if(previousLimitedUses == null)
            limitedUses = new HashMap<>();
        else
            limitedUses = new HashMap<>(previousLimitedUses);

        for(int i=0; i<items.size(); i++)
        {
            if(!limitedUses.containsKey(items.get(i)))
                limitedUses.put(items.get(i), 12);

            boolean isEfficient = false;
            if(i > 0)
                isEfficient = Solver.items[items.get(i-1).ordinal()].getsEfficiencyBonus(Solver.items[items.get(i).ordinal()]);

            limitedUses.put(items.get(i), Math.max(limitedUses.get(items.get(i))-numWorkshops - (isEfficient?numWorkshops:0), 0));
        }

        return limitedUses;
    }
    
    public boolean equals(Object other)
    {
        if(other instanceof WorkshopSchedule)
        {
            WorkshopSchedule otherWorkshop = (WorkshopSchedule)other;
            
            return items.equals(otherWorkshop.items);
            
        }
        return false;
    }

    public boolean interferesWithMe(List<Item> subSchedule)
    {
        int currentHour = 0;
        for(var item : subSchedule)
        {
            if(items.contains(item))
            {
                int lastStartingHour = lastStartingHourForItem(item);
                if(currentHour < lastStartingHour)
                    return true;
            }
            currentHour += Solver.items[item.ordinal()].time;
        }
        return false;
    }

    private int lastStartingHourForItem(Item item)
    {
        int currentHour = 24;
        for(int i=items.size()-1; i>=0; i--)
        {
            var currentItem = items.get(i);
            currentHour -= Solver.items[currentItem.ordinal()].time;
            if(currentItem==item)
                return currentHour;
        }
        return -1;
    }

    public int hashCode()
    {
        return items.hashCode();
    }
}
