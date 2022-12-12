package islandworkshop;
import java.io.*;
import java.util.Arrays;
import java.util.List;
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
        try (BufferedReader br = new BufferedReader(new FileReader("miennaChains.csv")))
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

    public static void generateBruteForceChains()
    {
        int minScheduleLength=20; //Exclusive
        int maxScheduleLength=24; //Inclusive
        List<ItemInfo> four = new ArrayList<>();
        List<ItemInfo> eight = new ArrayList<>();
        allEfficientChains = new ArrayList<>();
        for(var item1 : Solver.items)
        {
            int time1 = item1.time;
            for(var item2 : Solver.items)
            {
                if (!item2.getsEfficiencyBonus(item1))
                    continue;
                int time2 = time1 + item2.time;

                if(item1.time == 4 && item2.time == 8)
                {
                    four.add(item1);
                    eight.add(item2);
                }

                if(time2 > minScheduleLength && time2 <= maxScheduleLength)
                {
                    allEfficientChains.add(Arrays.asList(item1.item, item2.item));
                    continue;
                }

                for(var item3 : Solver.items)
                {
                    if (!item2.getsEfficiencyBonus(item3))
                        continue;

                    int time3 = time2 + item3.time;

                    if(time3 > minScheduleLength && time3 <= maxScheduleLength)
                    {
                        allEfficientChains.add(Arrays.asList(item1.item, item2.item, item3.item));
                        continue;
                    }

                    for(var item4 : Solver.items)
                    {
                        if (!item3.getsEfficiencyBonus(item4))
                            continue;

                        int time4 = time3 + item4.time;

                        if(time4 > minScheduleLength && time4 <= maxScheduleLength)
                        {
                            allEfficientChains.add(Arrays.asList(item1.item, item2.item, item3.item, item4.item));
                            continue;
                        }


                        for(var item5 : Solver.items)
                        {
                            if (!item5.getsEfficiencyBonus(item4))
                                continue;

                            int time5 = time4 + item5.time;

                            if(time5 > minScheduleLength && time5 <= maxScheduleLength)
                            {
                                allEfficientChains.add(Arrays.asList(item1.item, item2.item, item3.item, item4.item, item5.item));
                                continue;
                            }


                            for(var item6 : Solver.items)
                            {
                                if (!item5.getsEfficiencyBonus(item6))
                                    continue;

                                int time6 = time5 + item6.time;

                                if(time6 > minScheduleLength && time6 <= maxScheduleLength)
                                {
                                    allEfficientChains.add(Arrays.asList(item1.item, item2.item, item3.item, item4.item, item5.item, item6.item));
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Including all inefficient combos of "+four.size()+" 4-8 pairs");

        for(int i=0; i<four.size(); i++)
        {
            for(int j=0; j<four.size(); j++)
            {
                if(eight.get(i).getsEfficiencyBonus(four.get(j)))
                    continue;

                addChainToList(Arrays.asList(four.get(i).item, eight.get(i).item, four.get(j).item, eight.get(j).item));
            }
        }

        //Write to file

        String fileName = "miennaChains.csv";
        int chains = 0;
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            for(var chain : allEfficientChains)
            {
                var asStr = chain.stream().map(Item::toString)
                        .collect(Collectors.joining(","));
                writer.println(asStr);
                chains++;
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Wrote "+chains+" chains to file.");
    }

    private static void addChainToList(List<Item> efficientChain)
    {
        allEfficientChains.add(efficientChain);
    }
    
}