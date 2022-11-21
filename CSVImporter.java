package islandworkshop;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CSVImporter
{
    
    public static List<List<Item>> allEfficientChains;

    public static Popularity[] currentPopularity = new Popularity[50]; // item
    public static PeakCycle[][] currentPeaks; //item, day
    
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
    
    public static void initSupplyData(int week)
    {
        try (BufferedReader br = new BufferedReader(new FileReader("craft_peaks.csv"))) {
            var peaks = br.lines().skip((week - 1) * 50).limit(Solver.items.length)
                    .map(line -> Arrays.stream(line.split(",")).skip(2).limit(4).map(PeakCycle::fromString).collect(Collectors.toUnmodifiableList())
                    ).collect(Collectors.toUnmodifiableList());
            currentPeaks = new PeakCycle[peaks.size()][];
            for (int i = 0; i < peaks.size(); i++) {
                var itemPeaks = peaks.get(i);
                PeakCycle[] itemPeakArray = new PeakCycle[itemPeaks.size()];
                currentPeaks[i] = itemPeaks.toArray(itemPeakArray);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error importing peaks for week "+week+": "+e.getMessage());
        }
        try (BufferedReader br = new BufferedReader(new FileReader("craft_peaks.csv"))) {
            int index = br.lines().skip((week - 1) * 50).limit(1).map(line -> Arrays.stream(line.split(",")).skip(6).map(Integer::parseInt).findFirst().get()).findFirst().get();
            //System.out.println("Getting popularity " + index + " for week " + week);
            initPopularity(index);
        }
        catch(Exception e)
        {
            System.out.println("Error importing popularity index for week "+week+": "+e.getMessage());
        }
    }

    private static void initPopularity(int index)
    {
        try (BufferedReader br = new BufferedReader(new FileReader("popularity.csv")))
        {
            var popularities = br.lines().skip(index).limit(1)
                    .map(line -> Arrays.stream(line.split(","))
                            .map(Integer::parseInt).map(Popularity::fromIndex)
                            .collect(Collectors.toUnmodifiableList()))
                    .findFirst().get();
            popularities.toArray(currentPopularity);
            //System.out.println("Popularities this week: "+Arrays.toString(currentPopularity));
        }
        catch(Exception e)
        {
            System.out.println("Error getting popularity for index "+index+" from popularity.csv: "+e.getMessage());
        }
    }
    
}