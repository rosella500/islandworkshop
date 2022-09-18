package islandworkshop;
import java.util.ArrayList;
import static islandworkshop.PeakCycle.*;
import static islandworkshop.Supply.*;
import static islandworkshop.DemandShift.*;

public class ItemInstance
{
    int id;
    Popularity popularity;
    PeakCycle previousPeak;
    PeakCycle peak;
    int[] craftedPerDay;
    ArrayList<ObservedSupply> observedSupplies;
    
    private static final PeakCycle[][] PEAKS_TO_CHECK = {{Cycle2Weak, Cycle2Strong, Cycle3Weak, Cycle3Strong, Cycle67, Cycle45}, //Day2
            {Cycle4Weak, Cycle4Strong, Cycle5, Cycle6Weak, Cycle67}, //Day3
            {Cycle5Weak, Cycle5Strong, Cycle6Strong, Cycle7Weak, Cycle7Strong}}; //Day4
    
    //Set start-of-week data
    public ItemInstance(int id, Popularity pop, PeakCycle prevPeak, Supply startingSupply, DemandShift startingDemand)
    {
        this.id = id;
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
        return Solver.ITEMS[id].getName()+" has peak "+peak;
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
        if(observedSupplies.size() < 2 && observedSupplies.get(0).supply == Insufficient)
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
            else
            {
                peak = Cycle2Weak;
                if(previousPeak == Cycle7Strong)
                    System.out.println("Warning! Can't tell if "+Solver.ITEMS[id].getName()+" is a weak or a strong 2 peak.");
                else
                    System.out.println("Need to craft "+Solver.ITEMS[id].getName()+" to determine weak or strong 2 peak, assuming weak.");
            }
        }        
        if(observedSupplies.size() > 1)
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
                    int expectedPrevious = Solver.getSupplyOnDayByPeak(potentialPeak, day-1);
                    int expectedSupply = Solver.getSupplyOnDayByPeak(potentialPeak, day);
                    ObservedSupply expectedObservation = new ObservedSupply(Solver.GetSupplyBucket(crafted + expectedSupply), 
                            Solver.GetDemandShift(expectedPrevious, expectedSupply));
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
                    System.out.println(Solver.ITEMS[id].getName() + " does not match any known patterns for day "+(day+1));
            }
        }
    }
}