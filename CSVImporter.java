package islandworkshop;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVImporter
{
    
    public static List<List<Item>> allEfficientChains;

    public static Popularity[] currentPopularity = new Popularity[Solver.items.length]; // item
    public static PeakCycle[][] currentPeaks; //item, day

    private static final int SCHEDULES_TO_AVERAGE = 1000;
    
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

        int skipAmount=0;
        if(week<21)
            skipAmount = ((week-1)*50);
        else if(week < 40)
            skipAmount = (20*50+(week-21)*60);
        else
            skipAmount = 20*50 + 19*60 + week-40 * 72;
        try (BufferedReader br = new BufferedReader(new FileReader("craft_peaks.csv"))) {

            var peaks = br.lines().skip(skipAmount).limit(Solver.items.length)
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
            int index = br.lines().skip(skipAmount).limit(1).map(line -> Arrays.stream(line.split(",")).skip(6).map(Integer::parseInt).findFirst().get()).findFirst().get();
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

    /*public static void generateUsefulChains()
    {
        allEfficientChains = new ArrayList<>();
        var fourHour = new ArrayList<ItemInfo>();
        var eightHour = new ArrayList<ItemInfo>();
        var sixHour = new ArrayList<ItemInfo>();

        for (ItemInfo item : Solver.items)
        {
            List<ItemInfo> bucket = null;

            if (item.time == 4)
                bucket = fourHour;
            else if (item.time == 6)
                bucket = sixHour;
            else if (item.time == 8)
                bucket = eightHour;

            if(bucket != null)
                bucket.add(item);
        }

        List<Item> four = new ArrayList<>();
        List<Item> eight = new ArrayList<>();
        //Find schedules based on 8-hour crafts
        for (ItemInfo topItem : eightHour) {
            //PluginLog.LogVerbose("Building schedule around : " + topItem.item + ", peak: " + topItem.peak);


            List<ItemInfo> eightMatches = new ArrayList<>();
            //8-8-8
            for (ItemInfo eightMatchMatch : eightHour) {
                if (!eightMatchMatch.getsEfficiencyBonus(topItem))
                    continue;
                eightMatches.add(eightMatchMatch);
                allEfficientChains.add(List.of(topItem.item, eightMatchMatch.item, topItem.item));
            }

            //4-8-4-8 and 4-4-4-4-8
            for (ItemInfo firstFourMatch : fourHour) {
                if (!firstFourMatch.getsEfficiencyBonus(topItem))
                    continue;

                //Add all efficient 4-8 pairs to parallel lists. We'll deal with them later
                four.add(firstFourMatch.item);
                eight.add(topItem.item);

                for (ItemInfo secondFourMatch : fourHour) {
                    if (!secondFourMatch.getsEfficiencyBonus(firstFourMatch))
                        continue;

                    //4-4-8-8
                    for (var eightMatch : eightMatches)
                        allEfficientChains.add(List.of(secondFourMatch.item, firstFourMatch.item, topItem.item, eightMatch.item));

                    //4-4-4-4-8
                    for (ItemInfo thirdFourMatch : fourHour) {
                        if (!secondFourMatch.getsEfficiencyBonus(thirdFourMatch))
                            continue;

                        for (ItemInfo fourthFourMatch : fourHour) {
                            if (fourthFourMatch.getsEfficiencyBonus(thirdFourMatch))
                                allEfficientChains.add(List.of(fourthFourMatch.item, thirdFourMatch.item, secondFourMatch.item, firstFourMatch.item, topItem.item));
                        }
                    }
                }
            }


            for (ItemInfo sixHourMatch : sixHour) {
                if (!sixHourMatch.getsEfficiencyBonus(topItem))
                    continue;

                //4-6-6-8
                for (ItemInfo sixSixMatch : sixHour) {
                    if (!sixSixMatch.getsEfficiencyBonus(sixHourMatch))
                        continue;


                    for(var fourSixMatch : fourHour)
                    {
                        if(fourSixMatch.getsEfficiencyBonus(sixSixMatch))
                            allEfficientChains.add(List.of(fourSixMatch.item, sixSixMatch.item, sixHourMatch.item, topItem.item));
                    }
                }

                //4-6-8-6
                for (ItemInfo fourMatch : fourHour) {
                    if (!fourMatch.getsEfficiencyBonus(sixHourMatch))
                        continue;

                    for(var other6Match : sixHour)
                    {
                        if(other6Match.getsEfficiencyBonus(topItem))
                        {
                            allEfficientChains.add(List.of(fourMatch.item, sixHourMatch.item, topItem.item, other6Match.item));
                        }
                    }
                }
            }
        }

        System.out.println("Including all inefficient combos of "+four.size()+" 4-8 pairs");

        for(int i=0; i<four.size(); i++)
        {
            System.out.println("Adding all combos with "+four.get(i)+" - "+eight.get(i));
            for(int j=0; j<four.size(); j++)
            {

                allEfficientChains.add(List.of(four.get(i), eight.get(i), four.get(j), eight.get(j)));
            }
        }

        //Find schedules based on 6-hour crafts
        for(var topItem : sixHour)
        {
            //6-6-6-6
            HashSet<ItemInfo> sixMatches = new HashSet<>();
            for (ItemInfo sixMatch : sixHour) {
                if (!sixMatch.getsEfficiencyBonus(topItem))
                    continue;
                sixMatches.add(sixMatch);
            }
            for (ItemInfo firstSix : sixMatches)
            {
                for (ItemInfo secondSix : sixMatches)
                {
                    allEfficientChains.add(List.of( secondSix.item, topItem.item, firstSix.item, topItem.item ));
                }
            }


            for (ItemInfo firstFourMatch : fourHour) {
                if (!firstFourMatch.getsEfficiencyBonus(topItem))
                    continue;

                for (ItemInfo secondFourMatch : fourHour) {
                    if (!secondFourMatch.getsEfficiencyBonus(firstFourMatch))
                        continue;

                    for (ItemInfo thirdFourMatch : fourHour)
                    {
                        //4-4-6-4-6
                        if(!thirdFourMatch.getsEfficiencyBonus(topItem))
                            continue;
                        for(var sixMatch : sixHour)
                        {
                            if(sixMatch.getsEfficiencyBonus(thirdFourMatch))
                                allEfficientChains.add(List.of(secondFourMatch.item, firstFourMatch.item, topItem.item, thirdFourMatch.item, sixMatch.item));
                        }

                    }
                }
            }
        }

        System.out.println("All combos before adding 6-chains: "+allEfficientChains.size());

        for(var topItem : fourHour)
        {
            for(var fourMatch : fourHour)
            {
                if(!fourMatch.getsEfficiencyBonus(topItem))
                    continue;
                for(var secondFourMatch : fourHour)
                {
                    if(!secondFourMatch.getsEfficiencyBonus(fourMatch))
                        continue;
                    for(var thirdFourMatch : fourHour)
                    {
                        if(!secondFourMatch.getsEfficiencyBonus(thirdFourMatch))
                            continue;
                        for(var fourthFourMatch : fourHour)
                        {
                            if(fourthFourMatch.getsEfficiencyBonus(topItem))
                                allEfficientChains.add(List.of(thirdFourMatch.item, secondFourMatch.item, fourMatch.item,
                                        topItem.item, fourthFourMatch.item, topItem.item));
                        }
                    }
                }
            }
        }

        writeEfficientChainsToFile();
    }*/
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

        writeEfficientChainsToFile();
    }

    private static void writeEfficientChainsToFile()
    {
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


    public static int getAverageTrueGroovelessValue()
    {
        Map<List<Item>, Integer> schedulesByValue = new HashMap<>();
        for(var schedule : allEfficientChains)
        {
            CycleSchedule cycleSchedule = new CycleSchedule(3, 0);
            cycleSchedule.setForAllWorkshops(schedule);

            schedulesByValue.put(schedule, cycleSchedule.getTrueGroovelessValue());
        }
        var sortedSchedules  = schedulesByValue
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))//.limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));

        var finalIterator = sortedSchedules.entrySet().iterator();
        int totalValue = 0;
        for (int c = 0; c < SCHEDULES_TO_AVERAGE && finalIterator.hasNext(); c++)
        {
            var alt = finalIterator.next();
            //System.out.println("Top schedule: " + Arrays.toString(alt.getKey().toArray()) + ": " + alt.getValue());
            totalValue += alt.getValue();
        }
        int average = totalValue / SCHEDULES_TO_AVERAGE;
        System.out.println("Average: "+average);
        return average;
    }

    private static void addChainToList(List<Item> efficientChain)
    {
        allEfficientChains.add(efficientChain);
    }
    
}