package islandworkshop;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CSVImporter
{
    
    public static List<List<Item>> allEfficientChains;
    
    public static PeakCycle[] lastWeekPeaks;
    public static Popularity[] currentPopularity;
    public static List<List<ObservedSupply>> observedSupplies;
    public static PeakCycle[] currentPeaks;
    
    public static void initBruteForceChains()
    {
        allEfficientChains = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("AllEfficientChains.csv"))) 
        {
            String line;
            while ((line = br.readLine()) != null) 
            {
                String[] values = line.split(",");
                List<Item> items = new ArrayList<>();
                for(String itemStr : values)
                {
                    itemStr = itemStr.replace(" ", "");
                    itemStr = itemStr.replace("'", "");
                    itemStr = itemStr.replace("Islefish", "");
                    itemStr = itemStr.replace("Isleberry", "");
                    
                    if(!itemStr.isBlank())
                        items.add(Item.valueOf(itemStr));
                    
                }
                allEfficientChains.add(items);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error importing csv: "+e.getMessage());
        }
    }
    
    public static void writeCurrentPeaks(int week)
    {
        String path = "Week"+(week)+"Supply.csv";
        
        //TODO: this
    }
    
    public static void initSupplyData(int week)
    {
        lastWeekPeaks = new PeakCycle[Solver.items.length];
        String path = "Week"+(week-1)+"Supply.csv";
        
        if(week > 1)
        {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) 
            {
                String line;
                for(int c=0; (line = br.readLine()) != null; c++)
                {
                    String[] values = line.split(",");

                    if(values.length >= 11)
                    {
                        String peak = values[10].replace(" ","");
                        
                        lastWeekPeaks[c] = PeakCycle.valueOf(peak);
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("Error importing csv "+path+": "+e.getMessage());
            }
        }
        else
        {
            for(int c=0; c<lastWeekPeaks.length; c++)
            {                   
                lastWeekPeaks[c] = PeakCycle.Cycle2Weak;
            }
            lastWeekPeaks[Item.Barbut.ordinal()] = PeakCycle.Cycle7Strong;
            lastWeekPeaks[Item.Tunic.ordinal()] = PeakCycle.Cycle7Strong;
            lastWeekPeaks[Item.Brush.ordinal()] = PeakCycle.Cycle3Strong;
            
        }
        
        path = "Week"+week+"Supply.csv";
        
        currentPopularity = new Popularity[Solver.items.length];
        observedSupplies = new ArrayList<>();
        currentPeaks = new PeakCycle[Solver.items.length];
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) 
        {
            String line;
            for (int row=0; (line = br.readLine()) != null; row++) 
            {
                String[] values = line.split(",");
                for(int c=0; c<values.length; c+=2)
                {
                    String data1 = values[c];
                    String data2 = "";
                    if(c+1 < values.length)
                        data2 = values[c+1];
                    switch(c)
                    {
                    case 0:
                        data2 = data2.replace(" ", "");
                        currentPopularity[row] = Popularity.valueOf(data2);
                        break;
                    case 2:
                        ObservedSupply d1 = new ObservedSupply(Supply.valueOf(data1), DemandShift.valueOf(data2));
                        observedSupplies.add(new ArrayList<ObservedSupply>());
                        observedSupplies.get(row).add(d1);
                        break;
                    case 4:
                        ObservedSupply d2 = new ObservedSupply(Supply.valueOf(data1), DemandShift.valueOf(data2));
                        observedSupplies.get(row).add(d2);
                        break;
                    case 6:
                        ObservedSupply d3 = new ObservedSupply(Supply.valueOf(data1), DemandShift.valueOf(data2));
                        observedSupplies.get(row).add(d3);
                        break;
                    case 8:
                        ObservedSupply d4 = new ObservedSupply(Supply.valueOf(data1), DemandShift.valueOf(data2));
                        observedSupplies.get(row).add(d4);
                        break;
                    case 10:
                        data1 = data1.replace(" ","");
                        currentPeaks[row] = PeakCycle.valueOf(data1);
                        
                    }
                    
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error importing csv "+path+": "+e.getMessage());
        }
    }
    
}