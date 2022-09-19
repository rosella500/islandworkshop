package islandworkshop;
import java.util.ArrayList;
import java.util.Map;

import static islandworkshop.Supply.*;
import static islandworkshop.DemandShift.*;
import static islandworkshop.ItemCategory.*;
import static islandworkshop.PeakCycle.*;

public class ItemInfo
{
    final static int[][] SUPPLY_PATH = {{0, 0,0}, //Unknown
            {-4, -4, 10, 0, 0, 0, 0}, //Cycle2Weak 
            {-8, -7, 15, 0, 0, 0, 0}, //Cycle2Strong
            {0, -4, -4, 10, 0, 0, 0}, //Cycle3Weak
            {0, -8, -7, 15, 0, 0, 0}, //Cycle3Strong
            {0, 0, -4, -4, 10, 0, 0}, //Cycle4Weak
            {0, 0, -8, -7, 15, 0, 0}, //Cycle4Strong
            {0, 0, 0, -4, -4, 10, 0}, //5Weak
            {0, 0, 0, -8, -7, 15, 0}, //5Strong
            {0, -1, 5, -4, -4, -4, 10}, //6Weak
            {0, -1, 8, -7, -8, -7, 15}, //6Strong
            {0, -1, 8, -3, -4, -4, -4}, //7Weak
            {0, -1, 8, 0, -7, -8, -7}, //7Strong
            {0, 0, 0}, //4/5
            {0, 0, 0,-4,-4}, //5
            {0, -1, 8, 0} //6/7
            };
    
    //Constant info
    Item item;
    int baseValue;
    ItemCategory category1;
    ItemCategory category2;
    int time;
    Map<RareMaterial, Integer> materialsRequired;
    int materialValue;
    
    //Weekly info
    int id;
    Popularity popularity;
    PeakCycle previousPeak;
    PeakCycle peak;
    int[] craftedPerDay;
    ArrayList<ObservedSupply> observedSupplies;
    
    public ItemInfo(Item i, ItemCategory cat1, ItemCategory cat2, int value, int hours, Map<RareMaterial,Integer> mats)
    {
        item = i;
        baseValue = value;
        category1 = cat1;
        category2 = cat2;
        time = hours;
        materialsRequired = mats;
        materialValue = 0;
        
        if(mats != null)
            materialsRequired.forEach((k, v) -> {materialValue+=k.cowrieValue * v;});
        
    }
    
    public boolean getsEfficiencyBonus(ItemInfo other)
    {
        return !(other.item == item) && 
                ((other.category1!=Invalid && (other.category1 == category1 || other.category1 == category2)) ||
                (other.category2!=Invalid && (other.category2 == category1 || other.category2 == category2)));
    }
    
    private static final PeakCycle[][] PEAKS_TO_CHECK = {{Cycle3Weak, Cycle3Strong, Cycle67, Cycle45}, //Day2
            {Cycle4Weak, Cycle4Strong, Cycle6Weak, Cycle5, Cycle67}, //Day3
            {Cycle5Weak, Cycle5Strong, Cycle6Strong, Cycle7Weak, Cycle7Strong}}; //Day4
    
    //Set start-of-week data
    public void setInitialData(Popularity pop, PeakCycle prevPeak, Supply startingSupply, DemandShift startingDemand)
    {
        popularity = pop;
        previousPeak = prevPeak;
        peak = Unknown;
        craftedPerDay = new int[7];
        observedSupplies = new ArrayList<ObservedSupply>();
        observedSupplies.add(new ObservedSupply(startingSupply, startingDemand));   
        setPeakBasedOnObserved();
    }
    
    
    public String toString()
    {
        return item+", "+peak;
    }
    
    public void addObservedDay(Supply supply, DemandShift demand)
    {
        observedSupplies.add(new ObservedSupply(supply, demand));
        setPeakBasedOnObserved();
    }
    
    public void addCrafted(int num, int day)
    {
        craftedPerDay[day]+=num;
    }
    
    private int getCraftedBeforeDay(int day)
    {
        int sum = 0;
        for(int c=0; c<day; c++)
            sum+=craftedPerDay[c];
        
        return sum;
    }
    
    private void setPeakBasedOnObserved()
    {        
        if(observedSupplies.get(0).supply == Insufficient)
        {
            DemandShift observedDemand = observedSupplies.get(0).demandShift;
            
            if(previousPeak.isReliable)
            {
                if (observedDemand==None || observedDemand == Skyrocketing)
                {
                    peak = Cycle2Strong;
                    return;
                }
                else
                {
                    peak = Cycle2Weak;
                    return;
                }
            }
            else if(observedSupplies.size() > 1)
            {
                DemandShift observedDemand2 = observedSupplies.get(1).demandShift;
                if (observedDemand2 == Skyrocketing)
                {
                    peak = Cycle2Strong;
                    return;
                }
                else if(observedDemand2 == Increasing)
                {
                    peak = Cycle2Weak;
                    return;
                }
                else
                    System.out.println(item + " does not match any known patterns for day 2");
            }
            else
            {
                peak = Cycle2Weak;
                if(previousPeak == Cycle7Strong)
                    System.out.println("Warning! Can't tell if "+item+" is a weak or a strong 2 peak.");
                else
                    System.out.println("Need to craft "+item+" to determine weak or strong 2 peak, assuming weak."); 
            }
        }        
        else if(observedSupplies.size() > 1)
        {
            int daysToCheck = Math.min(4, observedSupplies.size());
            for(int day=1; day < daysToCheck; day++)
            {
                ObservedSupply observedToday = observedSupplies.get(day);
                //System.out.println("Observed: "+observedToday);
                int crafted = getCraftedBeforeDay(day);
                boolean found = false;
                
                for(int i=0; i<PEAKS_TO_CHECK[day-1].length; i++)
                {
                    PeakCycle potentialPeak = PEAKS_TO_CHECK[day-1][i];
                    int expectedPrevious = getSupplyOnDayByPeak(potentialPeak, day-1);
                    int expectedSupply = getSupplyOnDayByPeak(potentialPeak, day);
                    ObservedSupply expectedObservation = new ObservedSupply(getSupplyBucket(crafted + expectedSupply), 
                            getDemandShift(expectedPrevious, expectedSupply));
                    //System.out.println("Checking against peak "+potentialPeak+", expecting: "+expectedObservation);
                    
                    if(observedToday.equals(expectedObservation))
                    {
                        peak = potentialPeak;
                        found = true;
                        if(peak.isTerminal)
                            return;
                    }
                }
                
                if(!found)
                    System.out.println(item + " does not match any known patterns for day "+(day+1));
            }
        }
    }
    
    public int getSupplyAfterCraft(int day, int newCrafts)
    {
        return getSupplyOnDay( day) + newCrafts;
    }
    
    public int getSupplyOnDay(int day)
    {
        int supply = SUPPLY_PATH[peak.ordinal()][0];
        for(int c=1;c <= day; c++)
        {
            supply += craftedPerDay[c-1];
            supply += SUPPLY_PATH[peak.ordinal()][c];
        }
        
        return supply;
    }
    public static Supply getSupplyBucket(int supply)
    {
        if(supply < -8)
            return Nonexistent;
        if(supply < 0)
            return Insufficient;
        if(supply < 8)
            return Sufficient;
        if(supply < 16)
            return Surplus;
        return Overflowing;
    }
    
    public static int getSupplyOnDayByPeak(PeakCycle peak, int day)
    {
        int supply = SUPPLY_PATH[peak.ordinal()][0];
        for(int c = 1; c<=day; c++)
            supply += SUPPLY_PATH[peak.ordinal()][c];
        
        return supply;
    }
    
    public static DemandShift getDemandShift(int prevSupply, int newSupply)
    {
        int diff = newSupply - prevSupply;
        if(diff < -5)
            return Skyrocketing;
        if(diff < -1)
            return Increasing;
        if(diff < 2)
            return None;
        if(diff < 6)
            return Decreasing;
        return Plummeting;
    }
}