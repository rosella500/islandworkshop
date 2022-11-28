package islandworkshop;

import static islandworkshop.ItemCategory.*;
import static islandworkshop.RareMaterial.*;
import static islandworkshop.PeakCycle.*;
import static islandworkshop.Item.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Solver
{
    final static int WORKSHOP_BONUS = 120;
    final static int GROOVE_MAX = 35;

    final static int NUM_WORKSHOPS = 3;
    final static int helperPenalty = 5;
    final static int averageDayValue = 4404;
    
    final static ItemInfo[] items = {
            new ItemInfo(Potion,Concoctions,Invalid,28,4,1,null),
            new ItemInfo(Firesand,Concoctions,UnburiedTreasures,28,4,1,null),
            new ItemInfo(WoodenChair,Furnishings,Woodworks,42,6,1,null),
            new ItemInfo(GrilledClam,Foodstuffs,MarineMerchandise,28,4,1,null),
            new ItemInfo(Necklace,Accessories,Woodworks,28,4,1,null),
            new ItemInfo(CoralRing,Accessories,MarineMerchandise,42,6,1,null),
            new ItemInfo(Barbut,Attire,Metalworks,42,6,1,null),
            new ItemInfo(Macuahuitl,Arms,Woodworks,42,6,1,null),
            new ItemInfo(Sauerkraut,PreservedFood,Invalid,40,4,1,Map.of(Cabbage,1)),
            new ItemInfo(BakedPumpkin,Foodstuffs,Invalid,40,4,1,Map.of(Pumpkin,1)),
            new ItemInfo(Tunic,Attire,Textiles,72,6,1,Map.of(Fleece,2)),
            new ItemInfo(CulinaryKnife,Sundries,CreatureCreations,44,4,1,Map.of(Claw,1)),
            new ItemInfo(Brush,Sundries,Woodworks,44,4,1,Map.of(Fur, 1)),
            new ItemInfo(BoiledEgg,Foodstuffs,CreatureCreations,44,4,1,Map.of(Egg, 1)),
            new ItemInfo(Hora,Arms,CreatureCreations,72,6,1,Map.of(Carapace, 2)),
            new ItemInfo(Earrings,Accessories,CreatureCreations,44,4,1,Map.of(Fang, 1)),
            new ItemInfo(Butter,Ingredients,CreatureCreations,44,4,1,Map.of(Milk, 1)),
            new ItemInfo(BrickCounter,Furnishings,UnburiedTreasures,48,6,5,null),
            new ItemInfo(BronzeSheep,Furnishings,Metalworks,64,8,5,null),
            new ItemInfo(GrowthFormula,Concoctions,Invalid,136,8,5,Map.of(Alyssum, 2)),
            new ItemInfo(GarnetRapier,Arms,UnburiedTreasures,136,8,5,Map.of(Garnet,2)),
            new ItemInfo(SpruceRoundShield,Attire,Woodworks,136,8,5,Map.of(Spruce,2)),
            new ItemInfo(SharkOil,Sundries,MarineMerchandise,136,8,5,Map.of(Shark,2)),
            new ItemInfo(SilverEarCuffs,Accessories,Metalworks,136,8,5,Map.of(Silver,2)),
            new ItemInfo(SweetPopoto,Confections,Invalid,72,6,5,Map.of(Popoto, 2, Milk,1)),
            new ItemInfo(ParsnipSalad,Foodstuffs,Invalid,48,4,5,Map.of(Parsnip,2)),
            new ItemInfo(Caramels,Confections,Invalid,81,6,6,Map.of(Milk,2)),
            new ItemInfo(Ribbon,Accessories,Textiles,54,6,6,null),
            new ItemInfo(Rope,Sundries,Textiles,36,4,6,null),
            new ItemInfo(CavaliersHat,Attire,Textiles,81,6,6,Map.of(Feather,2)),
            new ItemInfo(Item.Horn,Sundries,CreatureCreations,81,6,6,Map.of(RareMaterial.Horn,2)),
            new ItemInfo(SaltCod,PreservedFood,MarineMerchandise,54,6,7,null),
            new ItemInfo(SquidInk,Ingredients,MarineMerchandise,36,4,7,null),
            new ItemInfo(EssentialDraught,Concoctions,MarineMerchandise,54,6,7,null),
            new ItemInfo(Jam,Ingredients,Invalid,78,6,7,Map.of(Isleberry,3)),
            new ItemInfo(TomatoRelish,Ingredients,Invalid,52,4,7,Map.of(Tomato,2)),
            new ItemInfo(OnionSoup,Foodstuffs,Invalid,78,6,7,Map.of(Onion,3)),
            new ItemInfo(Pie,Confections,MarineMerchandise,78,6,7,Map.of(Wheat,3)),
            new ItemInfo(CornFlakes,PreservedFood,Invalid,52,4,7,Map.of(Corn,2)),
            new ItemInfo(PickledRadish,PreservedFood,Invalid,104,8,7,Map.of(Radish,4)),
            new ItemInfo(IronAxe,Arms,Metalworks,72,8,8,null),
            new ItemInfo(QuartzRing,Accessories,UnburiedTreasures,72,8,8,null),
            new ItemInfo(PorcelainVase,Sundries,UnburiedTreasures,72,8,8,null),
            new ItemInfo(VegetableJuice,Concoctions,Invalid,78,6,8,Map.of(Cabbage,3)),
            new ItemInfo(PumpkinPudding,Confections,Invalid,78,6,8,Map.of(Pumpkin, 3, Egg, 1, Milk,1)),
            new ItemInfo(SheepfluffRug,Furnishings,CreatureCreations,90,6,8,Map.of(Fleece,3)),
            new ItemInfo(GardenScythe,Sundries,Metalworks,90,6,9,Map.of(Claw,3)),
            new ItemInfo(Bed,Furnishings,Textiles,120,8,9,Map.of(Fur,4)),
            new ItemInfo(ScaleFingers,Attire,CreatureCreations,120,8,9,Map.of(Carapace,4)),
            new ItemInfo(Crook,Arms,Woodworks,120,8,9,Map.of(Fang,4))};
    
    private static int groove = 0;
    private static int totalGross = 0;
    private static int totalNet = 0;
    public static boolean verboseCalculatorLogging = false;
    public static boolean verboseSolverLogging = false;
    private static int alternatives = 0;
    public static int groovePerFullDay = 40;
    public static int groovePerPartDay = 20;
    public static boolean rested = false;
    private static int islandRank = 10;
    public static double materialWeight = 0.5;
    public static boolean guaranteeRestD5 = false;
    public static Set<Item> reservedItems = new HashSet<>();
    public static Map<Item, Item> reservedHelpers = new HashMap<>();
    private static boolean valuePerHour = true;
    private static int itemsToReserve = 15;

    private static Map<Integer, CycleSchedule> scheduledDays = new HashMap<>();

    public static void main(String[] args)
    {

        for(int week = 1; week <= 14; week++)
        {
          System.out.println("__**Season "+week+" schedule:**__");
          groove = 0;
          totalGross = 0;
          totalNet = 0;
          rested = false;
          guaranteeRestD5 = false;
          for(ItemInfo item : items)
          {
              item.craftedPerDay = new int[7];
              item.peak = Unknown;
          }
          reservedItems.clear();
          reservedHelpers.clear();
          scheduledDays.clear();
          
          
        long time = System.currentTimeMillis();
        CSVImporter.initSupplyData(week);
        CSVImporter.initBruteForceChains();
        setInitialFromCSV();
        
        //get reserved item list
        Map<Item, Integer> itemValues = new HashMap<Item, Integer>();
        for(ItemInfo item : items)
        {
            if(item.time == 4)
                continue;
            int value = item.getValueWithSupply(Supply.Sufficient);
            if(valuePerHour)
                value = value * 8 / item.time;
            itemValues.put(item.item, value);
        }
        LinkedHashMap<Item,Integer> bestItems = itemValues
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));
        Iterator<Entry<Item, Integer>> itemIterator = bestItems.entrySet().iterator();

        List<Item> itemsThatGetHelpers = new ArrayList<>();
        for(int i=0;i<itemsToReserve && itemIterator.hasNext(); i++)
        {
            Item next = itemIterator.next().getKey();
            //System.out.println("Reserving "+next);
            reservedItems.add(next);
            if(i<10)
                itemsThatGetHelpers.add(next);
        }
        for(var itemEnum : itemsThatGetHelpers)
        {
            ItemInfo mainItem = items[itemEnum.ordinal()];
            if(mainItem.time != 8)
                continue;
            int bestValue = 0;
            Item bestHelper = Macuahuitl; //This is the most useless thing I can think of
            boolean hasMultiple = false;
            for(ItemInfo helper : items)
            {
                if(helper.time != 4 || !helper.getsEfficiencyBonus(mainItem))
                    continue;

                int value = helper.getValueWithSupply(Supply.Sufficient);
                if(value > bestValue)
                {
                    hasMultiple = false;
                    bestValue = value;
                    bestHelper = helper.item;
                }
                else if(value == bestValue)
                {
                    hasMultiple = true;
                }
            }
            if(!hasMultiple)
            {
                //System.out.println("Reserving helper "+bestHelper+" to go with main item "+itemEnum);
                reservedHelpers.put(itemEnum, bestHelper);
            }
            else
            {
                //System.out.println("Main item "+itemEnum+" has multiple best helpers, so it gets none");
            }
        }

        Entry<WorkshopSchedule, Integer> d2 = getBestSchedule(1, groove);
        alternatives = 0;
        boolean hasNextDay;
        //verboseSolverLogging = true;
        if(isWorseThanAllFollowing(d2, 1)) {
            restDay(d2, 1);
            hasNextDay = setObservedFromCSV(1);
        }
        else
        {
            hasNextDay = setObservedFromCSV(1);
            recalculateTodayAndSet(1, d2.getKey());
            //setDay(d2.getKey().getItems(), 1);
        }
        verboseSolverLogging = false;

        if(hasNextDay)
        {
            Entry<WorkshopSchedule, Integer> d3 = getBestSchedule(2, groove);
            if(!rested && isWorseThanAllFollowing(d3,2)) {
                restDay(d3, 2);
                hasNextDay = setObservedFromCSV(2);
            }
            else
            {
                hasNextDay = setObservedFromCSV(2);
                //setDay(d3.getKey().getItems(), 2);
                recalculateTodayAndSet(2, d3.getKey());
            }
            if(hasNextDay) 
            {
                Entry<WorkshopSchedule, Integer> d4 = getBestSchedule(3, groove);
                
                //verboseSolverLogging = true;
                boolean worst = isWorseThanAllFollowing(d4, 3, true);

                verboseSolverLogging = false;
                //System.out.println("Rested? "+rested+". Resting D5? " +guaranteeRestD5);
                if(!rested && guaranteeRestD5)
                {
                    System.out.println("Guaranteed resting C5 so recalculating C4");
                    Entry<WorkshopSchedule, Integer> d4Again = getBestSchedule(3, groove, null, 4);
                    hasNextDay = setObservedFromCSV(3);
                    recalculateTodayAndSet(3, d4.getKey());
                }
                else if(!rested && worst)
                {
                    restDay(d4, 3);
                    hasNextDay = setObservedFromCSV(3);
                }
                else //We either rested already or we aren't the worst so add it
                {
                    hasNextDay = setObservedFromCSV(3);
                    recalculateTodayAndSet(3, d4.getKey());
                }
                
                 
                if(hasNextDay) 
                {
                    setLateDays();
                }
            }
        }


        //addDay(Arrays.asList(Potion, GrowthFormula, Potion, GrowthFormula), 2);

        System.out.println("Season total: " + totalGross + " (" + totalNet + ")\n" + "Took "
                + (System.currentTimeMillis() - time) + "ms.\n\n");
        
        }

    }

    private static void recalculateTodayAndSet(int day, WorkshopSchedule prevSchedule)
    {

        var newSchedule = getBestBruteForceSchedule(day, groove, null, day, prevSchedule.getItems().get(0));

        if(!prevSchedule.getItems().equals(newSchedule.getKey().getItems()))
        {
            System.out.println("Updated rec detected!");
        }
        setDay(newSchedule.getKey().getItems(), day, groove, true);
    }

    
    private static void restDay(Entry<WorkshopSchedule, Integer> rec, int day)
    {
        printRestDayInfo(rec.getKey().getItems(), day);
        rested = true;
    }

    private static void printRestDayInfo(List<Item> rec, int day)
    {
        CycleSchedule restedDay = new CycleSchedule(day, 0);
        restedDay.setForAllWorkshops(rec);
        System.out.println("Rest cycle " + (day + 1)+". Think we'll make more than "
                + restedDay.getValue() + " grooveless with " + rec + ". ");
    }

    private static void setLateDays()
    {
        int startingGroove = groove;
        Entry<WorkshopSchedule, Integer> cycle5Sched = getBestSchedule(4, startingGroove, null, 6);
        Entry<WorkshopSchedule, Integer> cycle6Sched = getBestSchedule(5, startingGroove, null, 6);
        Entry<WorkshopSchedule, Integer> cycle7Sched = getBestSchedule(6, startingGroove);

        // I'm just hardcoding this, This could almost certainly be improved

        List<Entry<WorkshopSchedule, Integer>> endOfWeekSchedules = new ArrayList<>();
        endOfWeekSchedules.add(cycle5Sched);
        endOfWeekSchedules.add(cycle6Sched);
        endOfWeekSchedules.add(cycle7Sched);

        int bestDay = -1;
        int bestDayValue = -1;

        for (int i = 0; i < 3; i++)
        {
            int value = endOfWeekSchedules.get(i).getValue();
            if (bestDay == -1 || value > bestDayValue)
            {
                bestDay = i + 4;
                bestDayValue = value;
            }
        }

        if (bestDay == 4) // Day 5 is best
        {
            // System.out.println("Day 5 is best. Adding as-is");
            setDay(cycle5Sched.getKey().getItems(), 4);
            startingGroove = groove;
            Entry<WorkshopSchedule, Integer> recalced6Sched = getBestSchedule(5, startingGroove, null, 6);
            Entry<WorkshopSchedule, Integer> recalcedCycle7Sched = getBestSchedule(6, startingGroove);
            if(!rested)
            {
                if(recalced6Sched.getValue() > recalcedCycle7Sched.getValue())
                {
                    setDay(recalced6Sched.getKey().getItems(), 5);
                    printRestDayInfo(recalcedCycle7Sched.getKey().getItems(), 6);
                }
                else
                {
                    printRestDayInfo(recalced6Sched.getKey().getItems(), 5);
                    setDay(recalcedCycle7Sched.getKey().getItems(), 6);
                }
            }
            else
            {

                setDay(recalced6Sched.getKey().getItems(), 5, startingGroove, false); //Temporarily set it so we can get more accurate values for D7

                Entry<WorkshopSchedule, Integer> onlyCycle6Sched = getBestSchedule(5, startingGroove);

                recalcedCycle7Sched = getBestSchedule(6, startingGroove);

                if(recalced6Sched.getValue() + recalcedCycle7Sched.getValue() < onlyCycle6Sched.getValue() + cycle7Sched.getValue())
                {
                    //Cycle 6 only won because it was cheating.
                    setDay(onlyCycle6Sched.getKey().getItems(), 5);
                    setDay(cycle7Sched.getKey().getItems(), 6);
                }
                else
                {
                    setDay(recalced6Sched.getKey().getItems(), 5);
                    setDay(recalcedCycle7Sched.getKey().getItems(), 6);
                }
            }

        }
        else if (bestDay == 6) // Day 7 is best
        {
            //System.out.println("Day 7 is best");
            Map<Item,Integer> reserved7Set = cycle7Sched.getKey().getLimitedUses();

            if(!rested)//We only care about one of 5 or 6
            {
                Entry<WorkshopSchedule, Integer> recalcedCycle6Sched = getBestSchedule(5, startingGroove, reserved7Set, 6);
                Entry<WorkshopSchedule, Integer> recalcedCycle5Sched = getBestSchedule(4, startingGroove, reserved7Set, 6);

                if(recalcedCycle5Sched.getValue() > recalcedCycle6Sched.getValue())
                {
                    setDay(recalcedCycle5Sched.getKey().getItems(), 4, startingGroove, true);
                    printRestDayInfo(recalcedCycle6Sched.getKey().getItems(), 5);
                }
                else
                {
                    printRestDayInfo(recalcedCycle5Sched.getKey().getItems(), 4);
                    setDay(recalcedCycle6Sched.getKey().getItems(), 5, startingGroove, true);
                }
            }
            else
            {
                Entry<WorkshopSchedule, Integer> recalcedCycle6Sched = getBestSchedule(5, startingGroove + 9, reserved7Set, 6);
                //try deriving 5 from 6
                int total65 = 0;
                Map<Item,Integer> reserved67Items = recalcedCycle6Sched.getKey().getLimitedUses(reserved7Set);
                Entry<WorkshopSchedule, Integer> recalcedCycle5Sched = getBestSchedule(4, startingGroove, reserved67Items, 6);
                setDay(recalcedCycle5Sched.getKey().getItems(), 4, startingGroove, false);
                setDay(recalcedCycle6Sched.getKey().getItems(), 5, groove, false);
                total65 = totalGross + totalNet;
                /*System.out.println("Trying to prioritize day 6:"+Arrays.toString(recalcedCycle6Sched.getKey().getItems().toArray())
                        +" ("+recalcedCycle6Sched.getValue()+"), so day 5: "+Arrays.toString(recalcedCycle5Sched.getKey().getItems().toArray())
                        +" ("+recalcedCycle5Sched.getValue()+") total: "+total65);*/

                //Try deriving 6 from 5
                setDay(cycle5Sched.getKey().getItems(), 4, startingGroove, false);
                Entry<WorkshopSchedule, Integer> basedOn56Sched = getBestSchedule(5, groove, reserved7Set, 6);
                setDay(basedOn56Sched.getKey().getItems(), 5, groove, false);
                int total56 = totalGross + totalNet;
                /*System.out.println("Trying to prioritize day 5:"+Arrays.toString(cycle5Sched.getKey().getItems().toArray())
                        +" ("+cycle5Sched.getValue()+"), so day 6: "+Arrays.toString(basedOn56Sched.getKey().getItems().toArray())
                        +" ("+basedOn56Sched.getValue()+") total: "+total56);*/

                if(total65 > total56)
                {
                    //System.out.println("Basing on 6 is better");
                    setDay(recalcedCycle5Sched.getKey().getItems(), 4, startingGroove, true);
                    setDay(recalcedCycle6Sched.getKey().getItems(), 5);
                }
                else
                {
                    //System.out.println("Basing on 5 is better");
                    setDay(cycle5Sched.getKey().getItems(), 4, startingGroove, true);
                    setDay(basedOn56Sched.getKey().getItems(), 5, groove, true);
                }
            }

            setDay(getBestSchedule(6, groove).getKey().getItems(), 6);
        }
        else // Best day is Day 6
        {
             //System.out.println("Day 6 is best");
            setDay(cycle6Sched.getKey().getItems(), 5, startingGroove, false); //Temporarily set it so we can get more accurate values for D7
            Map<Item,Integer> reserved6 = cycle6Sched.getKey().getLimitedUses();
            Entry<WorkshopSchedule, Integer> recalcedCycle5Sched = getBestSchedule(4, startingGroove,
                    reserved6, 5);
            Entry<WorkshopSchedule, Integer> recalcedCycle7Sched = getBestSchedule(6, startingGroove);

            Entry<WorkshopSchedule, Integer> onlyCycle6Sched = getBestSchedule(5, startingGroove);
            setDay(onlyCycle6Sched.getKey().getItems(), 5, startingGroove, false);
            Entry<WorkshopSchedule, Integer> onlyCycle7Sched = getBestSchedule(6, startingGroove);
            Map<Item,Integer> reservedOnly6 = onlyCycle6Sched.getKey().getLimitedUses();
            Entry<WorkshopSchedule, Integer> onlyCycle5Sched = getBestSchedule(4, startingGroove,
                    reservedOnly6, 5);

            if(!rested)
            {
                //We only care about either 5 or 7, not both
                int best56Combo = cycle6Sched.getValue() + recalcedCycle5Sched.getValue();
                int best67Combo = cycle6Sched.getValue() + recalcedCycle7Sched.getValue();
                int best76Combo = onlyCycle6Sched.getValue() + onlyCycle7Sched.getValue();

                int bestOverall = Math.max(best76Combo, Math.max(best67Combo, best56Combo));
                if(bestOverall == best56Combo)
                {
                    setDay(recalcedCycle5Sched.getKey().getItems(), 4, startingGroove, true);
                    setDay(getBestSchedule(5, groove, null, 6).getKey().getItems(), 5);
                    printRestDayInfo(recalcedCycle7Sched.getKey().getItems(), 6);
                }
                else
                {
                    printRestDayInfo(recalcedCycle5Sched.getKey().getItems(), 4);
                    if(bestOverall == best67Combo)
                    {
                        setDay(cycle6Sched.getKey().getItems(), 5, startingGroove, true);
                        setDay(recalcedCycle7Sched.getKey().getItems(), 6);
                    }
                    else
                    {
                        setDay(onlyCycle6Sched.getKey().getItems(), 5, startingGroove, true);
                        setDay(onlyCycle7Sched.getKey().getItems(), 6);
                    }
                }
            }
            else
            {
                //We're using all 3 days
                if(cycle6Sched.getValue() + recalcedCycle5Sched.getValue() + recalcedCycle7Sched.getValue() > onlyCycle5Sched.getValue() + onlyCycle6Sched.getValue() + onlyCycle7Sched.getValue())
                {
                    //Using 6 first
                    setDay(recalcedCycle5Sched.getKey().getItems(), 4, startingGroove, true);
                    setDay(cycle6Sched.getKey().getItems(), 5);
                    setDay(recalcedCycle7Sched.getKey().getItems(), 6);

                }
                else
                {
                    //6 takes too much from 7 so we just do it straight
                    setDay(onlyCycle5Sched.getKey().getItems(), 4, startingGroove, true);
                    setDay(onlyCycle6Sched.getKey().getItems(), 5);
                    setDay(onlyCycle7Sched.getKey().getItems(), 6);
                }

            }
        }
    }
    
    private static boolean isWorseThanAllFollowing(Entry<WorkshopSchedule, Integer> rec,
            int day)
    {
        return isWorseThanAllFollowing(rec, day, false);
    }

    private static boolean isWorseThanAllFollowing(Entry<WorkshopSchedule, Integer> rec,
            int day, boolean checkD5)
    {
        int worstInFuture = 99999;
        boolean bestD5IsWorst = true;
        int bestD5 = 0;
        if(verboseSolverLogging) System.out.println("Comparing c" + (day + 1) + " (" + rec.getValue()+ ") to worst-case future days");
        
        Map<Item,Integer> reservedSet = new HashMap<Item,Integer>();
        for(Item item : rec.getKey().getItems())
            reservedSet.put(item, 0);
        for (int d = day + 1; d < 7; d++)
        {
            Entry<WorkshopSchedule, Integer> solution;
            if (day == 3 && d == 4) // We have a lot of info about this specific pair so
                                    // we might as well use it
            {
                solution = getD5EV();   
                bestD5 = solution.getValue();
            }
            else
                solution = getBestSchedule(d, groove, reservedSet, d);
            if (verboseSolverLogging)
                System.out.println("Cycle " + (d + 1) + ", crafts: "
                        + Arrays.toString(solution.getKey().getItems().toArray())
                        + " value: " + solution.getValue());
            worstInFuture = Math.min(worstInFuture, solution.getValue());
            
            for(Item item : solution.getKey().getItems())
                reservedSet.put(item, 0);
            
            if (bestD5 > 0 && d > 4 && solution.getValue() < bestD5) //If we're checking a later day and it's worse than our best D5
                bestD5IsWorst = false;
        }
        //System.out.println("Best D5 "+bestD5+". Worst? "+bestD5IsWorst);
        if (checkD5 && day == 3 && bestD5IsWorst && rec.getValue() >= bestD5)
        {
            guaranteeRestD5 = true;
            //System.out.println("Best D5 "+bestD5+" is the worst value I can find, so recalcing D4 with its crafts too. Resting d5? "+guaranteeRestD5);
        }
        
           // System.out.println("Worst future day: " + worstInFuture);

        return rec.getValue() <= worstInFuture;
    }

    // Specifically for comparing D4 to D5
    public static Entry<WorkshopSchedule, Integer> getD5EV()
    {
        Entry<WorkshopSchedule, Integer> solution = getBestSchedule(4, groove);
        if (verboseSolverLogging)
            System.out.println(
                    "Testing against C5 solution " + solution.getKey().getItems());
        List<ItemInfo> c5Peaks = new ArrayList<>();
        for (Item item : solution.getKey().getItems())
            if (items[item.ordinal()].peak == Cycle5
                    && !c5Peaks.contains(items[item.ordinal()]))
                c5Peaks.add(items[item.ordinal()]);
        int sum = solution.getValue();
        int permutations = (int) Math.pow(2, c5Peaks.size());
        if (verboseSolverLogging)
            System.out.println(
                    "C5 peaks: " + c5Peaks.size() + ", permutations: " + permutations);

        for (int p = 1; p < permutations; p++)
        {
            for (int i = 0; i < c5Peaks.size(); i++)
            {
                boolean strong = ((p) & (1 << i)) != 0; // I can't believe I'm using a
                                                        // bitwise and
                if (verboseSolverLogging)
                    System.out.println("Checking permutation " + p + " for item "
                            + c5Peaks.get(i).item + " " + (strong ? "strong" : "weak"));
                if (strong)
                    c5Peaks.get(i).peak = Cycle5Strong;
                else
                    c5Peaks.get(i).peak = Cycle5Weak;
            }

            int toAdd = solution.getKey().getValueWithGrooveEstimate(4, groove);
            if (verboseSolverLogging)
                System.out.println("Permutation " + p + " has value " + toAdd);
            sum += toAdd;
        }

        if (verboseSolverLogging)
            System.out.println("Sum: " + sum + " average: " + sum / permutations);
        sum /= permutations;
        solution.setValue(sum);

        for (ItemInfo item : c5Peaks)
        {
            item.peak = Cycle5; // Set back to normal
        }

        return solution;
    }

    public static void setDay(List<Item> crafts, int day) {
    setDay(crafts, day, groove, true);
    }
    public static void setDay(List<Item> crafts, int day, int groove, boolean real)
    {
        if(real)
            System.out.println("Cycle " + (day + 1) + ", crafts: " + Arrays.toString(crafts.toArray()));

        setDay(crafts, crafts, crafts, day, groove, real);
    }

    public static void setDay(List<Item> crafts0, List<Item> crafts1, List<Item> crafts2,
                              int day, int groove, boolean real)
    {
        CycleSchedule schedule = new CycleSchedule(day, groove);
        schedule.setWorkshop(0, crafts0);
        schedule.setWorkshop(1, crafts1);
        schedule.setWorkshop(2, crafts2);

        if(scheduledDays.containsKey(day))
        {
            //System.out.println("Removing old schedule for day "+(day+1));
            int prevGross = scheduledDays.get(day).getValue();
            totalGross-=prevGross;
            totalNet-=(prevGross - scheduledDays.get(day).getMaterialCost());
            for (var item : items)
                item.setCrafted(0, day);
            scheduledDays.remove(day);
        }
        int startingGroove = schedule.startingGroove;
        schedule.startingGroove = 0;
        int groovelessValue = schedule.getValue();
        schedule.startingGroove = startingGroove;

        int gross = schedule.getValue();
        verboseCalculatorLogging = false;
        totalGross += gross;

        int net = gross - schedule.getMaterialCost();
        totalNet += net;
        Solver.groove = schedule.endingGroove;

        boolean oldVerbose = verboseCalculatorLogging;
        verboseCalculatorLogging = false;
        if(real)
            System.out.println("Cycle " + (day + 1) + " total, 0 groove: " + groovelessValue
                + ". Starting groove " + startingGroove + ": " + gross + ", net " + net
                + ".");
        verboseCalculatorLogging = oldVerbose;
        schedule.numCrafted.forEach((k, v) ->
        {
            items[k.ordinal()].setCrafted(v, day);
        });
        scheduledDays.put(day,schedule);
    }
    private static Map.Entry<WorkshopSchedule, Integer> getBestBruteForceSchedule(int day, int groove,
                                                                                  Map<Item,Integer> limitedUse, int allowUpToDay)
    {
        return getBestBruteForceSchedule(day, groove, limitedUse, allowUpToDay, null);
    }
    private static Map.Entry<WorkshopSchedule, Integer> getBestBruteForceSchedule(int day, int groove,
            Map<Item,Integer> limitedUse, int allowUpToDay, Item startingItem) {

        HashMap<WorkshopSchedule, Integer> safeSchedules = new HashMap<>();
        List<List<Item>> filteredItemLists;



        filteredItemLists = CSVImporter.allEfficientChains.stream()
                .filter(list -> list.stream().allMatch(
                        item -> items[item.ordinal()].rankUnlocked <= islandRank))
                .filter(list -> list.stream().allMatch(
                        item -> items[item.ordinal()].peaksOnOrBeforeDay(allowUpToDay))).collect(Collectors.toList());

        if (startingItem != null)
        {
            filteredItemLists = filteredItemLists.stream().filter (list -> list.stream().limit(1).allMatch(item -> item == startingItem)).collect(Collectors.toList());
        }




        for (List<Item> list : filteredItemLists)
        {
            addToScheduleMap(list, day, limitedUse, safeSchedules, groove);
        }

        LinkedHashMap<WorkshopSchedule, Integer> sortedSchedules = safeSchedules
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));

        Iterator<Entry<WorkshopSchedule, Integer>> finalIterator = sortedSchedules
                .entrySet().iterator();
        Entry<WorkshopSchedule, Integer> bestSchedule = finalIterator.next();
        if (alternatives > 0)
        {
            System.out.println("Best rec: "
                    + Arrays.toString(bestSchedule.getKey().getItems().toArray()) + ": "
                    + bestSchedule.getValue());
            for (int c = 0; c < alternatives && finalIterator.hasNext(); c++)
            {
                Entry<WorkshopSchedule, Integer> alt = finalIterator.next();
                System.out.println("Alternative rec: "
                        + Arrays.toString(alt.getKey().getItems().toArray()) + ": "
                        + alt.getValue());
            }
        }

        return bestSchedule;

    }

    private static Map.Entry<WorkshopSchedule, Integer> getBestSchedule(int day, int groove)
    {
        return getBestSchedule(day, groove, null, day);
    }

    private static Map.Entry<WorkshopSchedule, Integer> getBestSchedule(int day, int groove,
            Map<Item,Integer> limitedUse, int allowUpToDay)
    {
            return getBestBruteForceSchedule(day, groove, limitedUse, allowUpToDay);
    }
    
    private static int addToScheduleMap(List<Item> list, int day,Map<Item,Integer> limitedUse,
            HashMap<WorkshopSchedule, Integer> safeSchedules, int groove)
    {
        WorkshopSchedule workshop = new WorkshopSchedule(list);
        if(workshop.usesTooMany(limitedUse))
            return 0;
        
        int value = workshop.getValueWithGrooveEstimate(day, groove);
        // Only add if we don't already have one with this schedule or ours is better
        int oldValue = safeSchedules.getOrDefault(workshop, -1);

        if (oldValue < value)
        {
//            if (verboseSolverLogging && oldValue > 0)
//                System.out.println("Replacing schedule with mats "
//                        + workshop.rareMaterialsRequired + " with " + list + " because "
//                        + value + " is higher than " + oldValue);
            safeSchedules.remove(workshop); // It doesn't seem to update the key when
                                            // updating the value, so we delete the key
                                            // first
            safeSchedules.put(workshop, value);
        } else
        {
//            if (verboseSolverLogging)
//                System.out.println("Not replacing schedule with mats "
//                        + workshop.rareMaterialsRequired + " with " + list + " because "
//                        + value + " is lower than " + oldValue);

            value = 0;
        }

        return value;

    }

    private static void setInitialFromCSV()
    {
        for (int i = 0; i < items.length; i++)
        {
            items[i].setInitialData(CSVImporter.currentPopularity[i],
                    CSVImporter.currentPeaks[i][0]);
        }
    }

    private static boolean setObservedFromCSV(int day)
    {
        if (day >= CSVImporter.currentPeaks[0].length)
            return false;

        for (int i = 0; i < items.length; i++)
        {
            items[i].peak = CSVImporter.currentPeaks[i][day];
        }
        return true;
    }

    public static Supply getSupplyBucket(int supply)
    {
        if (supply < -8)
            return Supply.Nonexistent;
        if (supply < 0)
            return Supply.Insufficient;
        if (supply < 8)
            return Supply.Sufficient;
        if (supply < 16)
            return Supply.Surplus;
        return Supply.Overflowing;
    }

    public static DemandShift getDemandShift(int prevSupply, int newSupply)
    {
        int diff = newSupply - prevSupply;
        if (diff < -5)
            return DemandShift.Skyrocketing;
        if (diff < -1)
            return DemandShift.Increasing;
        if (diff < 2)
            return DemandShift.None;
        if (diff < 6)
            return DemandShift.Decreasing;
        return DemandShift.Plummeting;
    }
}
