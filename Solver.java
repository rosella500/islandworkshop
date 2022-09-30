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
import java.util.stream.Stream;
public class Solver
{
    final static int WORKSHOP_BONUS = 120;
    final static int GROOVE_MAX = 35;
    
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
    public static int groovePerPartDay = 15;
    public static boolean rested = false;
    public static boolean allow4HrBorrowing = true;
    private static int islandRank = 10;
    public static double materialWeight = 0.5;

    public static void main(String[] args)
    {
        int week = 6;
        long time = System.currentTimeMillis();
        CSVImporter.initSupplyData(week);
        CSVImporter.initBruteForceChains();
        setInitialFromCSV();

        allow4HrBorrowing = true;
        Entry<WorkshopSchedule, Integer> d2 = getBestSchedule(1, null);
        boolean hasNextDay = setObservedFromCSV(1);
        addOrRest(d2, 1);
        
        if(hasNextDay) 
        { 
            Entry<WorkshopSchedule, Integer> d3 = getBestSchedule(2, null);
            hasNextDay = setObservedFromCSV(2);
            addOrRest(d3, 2); 
            if(hasNextDay) 
            { 
                Entry<WorkshopSchedule, Integer> d4 = getBestSchedule(3, null);
                hasNextDay = setObservedFromCSV(3);
                addOrRest(d4, 3); 
                
                if(hasNextDay) 
                { 
                    if(CSVImporter.currentPeaks[0] == null)
                        CSVImporter.writeCurrentPeaks(week);
          
                    setLateDays();
                }
            }
        }
         

        
        
//          addDay(Arrays.asList(BoiledEgg, BakedPumpkin, ParsnipSalad, GrilledClam,
//          SquidInk, TomatoRelish),1); addDay(Arrays.asList(BoiledEgg, BakedPumpkin,
//          ParsnipSalad, GrilledClam, SquidInk, TomatoRelish),3); rested = true;
//          //setLateDays(); addDay(Arrays.asList(SquidInk,Butter,Jam,Butter,Jam),4);
//          addDay(Arrays.asList(Rope,Bed,Rope,Bed),5);
//          addDay(Arrays.asList(Crook,GarnetRapier,Crook),6);
         
        

//        for (ItemInfo item : items)
//        {
//            System.out.println(item.peak.toDisplayName());
//        }

        System.out.println("Week total: " + totalGross + " (" + totalNet + ")\n" + "Took "
                + (System.currentTimeMillis() - time) + "ms.");

    }

    private static void addOrRest(Entry<WorkshopSchedule, Integer> rec, int day)
    {
        if (!rested && (rec == null || isWorseThanAllFollowing(rec, day)))
        {
            printRestDayInfo(rec.getKey().getItems(), day);
            rested = true;
        } else
        {

            addDay(rec.getKey().getItems(), day);
        }
    }

    private static void addBestSchedule(int day)
    {
        addDay(getBestSchedule(day, null).getKey().getItems(), day);
    }

    private static void printRestDayInfo(List<Item> rec, int day)
    {
        CycleSchedule restedDay = new CycleSchedule(day, 0);
        restedDay.setForAllWorkshops(rec);
        System.out.println("Think we're guaranteed to make more than "
                + restedDay.getValue() + " with " + rec + ". Rest day " + (day + 1));
    }

    private static void setLateDays()
    {
        Entry<WorkshopSchedule, Integer> cycle5Sched = getBestSchedule(4, null);
        Entry<WorkshopSchedule, Integer> cycle6Sched = getBestSchedule(5, null);
        Entry<WorkshopSchedule, Integer> cycle7Sched = getBestSchedule(6, null);

        // I'm just hardcoding this, This could almost certainly be improved

        List<Entry<WorkshopSchedule, Integer>> endOfWeekSchedules = new ArrayList<>();
        endOfWeekSchedules.add(cycle5Sched);
        endOfWeekSchedules.add(cycle6Sched);
        endOfWeekSchedules.add(cycle7Sched);

        int bestDay = -1;
        int bestDayValue = -1;
        int worstDay = -1;
        int worstDayValue = -1;

        for (int i = 0; i < 3; i++)
        {
            int value = endOfWeekSchedules.get(i).getValue();
            if (bestDay == -1 || value > bestDayValue)
            {
                bestDay = i + 4;
                bestDayValue = value;
            }
            if (worstDay == -1 || value < worstDayValue)
            {
                worstDay = i + 4;
                worstDayValue = value;
            }
        }

        if (bestDay == 4) // Day 5 is best
        {
            // System.out.println("Day 5 is best. Adding as-is");
            addDay(cycle5Sched.getKey().getItems(), 4);

            if (worstDay == 5)
            {
                // Day 6 is worst, so recalculate it according to day 7
                Entry<WorkshopSchedule, Integer> recalcedCycle7Sched = getBestSchedule(6,
                        null);

                Entry<WorkshopSchedule, Integer> recalced6Sched = getBestSchedule(5,
                        new HashSet<Item>(recalcedCycle7Sched.getKey().getItems()));
                // System.out.println("Recalcing day 7");
                if (rested)
                {
                    // System.out.println("Recalcing day 6 using only day 7's requirements
                    // as verboten and adding");
                    addDay(recalced6Sched.getKey().getItems(), 5);
                } else
                {
                    printRestDayInfo(recalced6Sched.getKey().getItems(), 5);
                }
                // System.out.println("Adding day 7");
                addDay(recalcedCycle7Sched.getKey().getItems(), 6);
            } else
            {
                // System.out.println("Day 6 is second best, just recalcing and adding");
                addDay(getBestSchedule(5, null).getKey().getItems(), 5);

                Entry<WorkshopSchedule, Integer> recalced7Sched = getBestSchedule(6,
                        null);
                if (rested)
                {
                    // System.out.println("Day 6 is second best, just recalcing and adding
                    // 7 too");
                    addDay(recalced7Sched.getKey().getItems(), 6);
                } else
                {
                    printRestDayInfo(recalced7Sched.getKey().getItems(), 6);
                }
            }
        } else if (bestDay == 6) // Day 7 is best
        {
            // System.out.println("Day 7 is best");
            HashSet<Item> reserved7Items = new HashSet<>(cycle7Sched.getKey().getItems());
            if (worstDay == 4 || rested) // Day 6 is second best or we're using all the
                                         // days anyway
            {
                 //System.out.println("Day 6 is second best or we're using all the days anyway. Recalcing 6 based on 7.");
                // Recalculate it in case it's better just reserving day 7
                Entry<WorkshopSchedule, Integer> recalcedCycle6Sched = getBestSchedule(5,
                        reserved7Items);
                 //System.out.println("Recalced 6:"+Arrays.toString(recalcedCycle6Sched.getKey().getItems().toArray())+" value:"+recalcedCycle6Sched.getValue());
                HashSet<Item> reserved67Items = new HashSet<Item>();
                reserved67Items.addAll(reserved7Items);
                reserved67Items.addAll(recalcedCycle6Sched.getKey().getItems());

                if (rested)
                {
                    Entry<WorkshopSchedule, Integer> recalcedCycle5Sched = getBestSchedule(
                            4, reserved67Items);
                     //System.out.println("Recalced 5:"+Arrays.toString(recalcedCycle5Sched.getKey().getItems().toArray())+" value:"+recalcedCycle5Sched.getValue());
                     //System.out.println("Recalcing 5 based on 6. Is it better?");

                    // Only use the recalculation if it doesn't ruin D5 too badly
                    if (recalcedCycle5Sched.getValue() + recalcedCycle6Sched
                            .getValue() > cycle5Sched.getValue() + cycle6Sched.getValue())
                    {
                         //System.out.println("It is! Using recalced 5");
                        addDay(recalcedCycle5Sched.getKey().getItems(), 4);
                    }

                    else
                    {
                        //This case is buggy. 
                        //System.out.println("It isn't, using original");
                        addDay(cycle5Sched.getKey().getItems(), 4);
                    }
                    //System.out.println("Recalcing 6 AGAIN just in case 5 changed it, still only forbidding things used day 7");
                    addDay(getBestSchedule(5, reserved7Items).getKey().getItems(), 5);
                } else
                {
                    printRestDayInfo(cycle5Sched.getKey().getItems(), 4);
                    addDay(recalcedCycle6Sched.getKey().getItems(), 5);
                }
            }
            else if (worstDay == 5) // Day 5 is second best and we aren't using day 6
            {
                printRestDayInfo(cycle6Sched.getKey().getItems(), 5);
                /// System.out.println("Day 6 isn't being used so just recalc 5 based on
                /// day 7");
                addDay(getBestSchedule(4, reserved7Items).getKey().getItems(), 4);
            }
            // System.out.println("Adding recalced day 7");
            addDay(getBestSchedule(6, null).getKey().getItems(), 6);

        } else // Best day is Day 6
        {
            // System.out.println("Day 6 is best");
            if (rested || worstDay != 4)
            {
                // System.out.println("Adding day 5 as-is");
                addDay(cycle5Sched.getKey().getItems(), 4);
            } else
                printRestDayInfo(cycle5Sched.getKey().getItems(), 4);
            // System.out.println("Recalcing day 6 and adding");
            addDay(getBestSchedule(5, null).getKey().getItems(), 5);

            Entry<WorkshopSchedule, Integer> recalcedCycle7Sched = getBestSchedule(6,
                    null);
            if (rested || worstDay != 6)
            {
                // System.out.println("Recalcing day 7 and adding");
                addDay(recalcedCycle7Sched.getKey().getItems(), 6);
            } else
                printRestDayInfo(recalcedCycle7Sched.getKey().getItems(), 6);
        }
    }

    private static boolean isWorseThanAllFollowing(Entry<WorkshopSchedule, Integer> rec,
            int day)
    {
        int worstInFuture = 99999;
        if (verboseSolverLogging)
            System.out.println("Comparing d" + (day + 1) + " (" + rec.getValue()
                    + ") to worst-case future days");
        HashSet<Item> reservedSet = new HashSet<>(rec.getKey().getItems());
        for (int d = day + 1; d < 7; d++)
        {
            Entry<WorkshopSchedule, Integer> solution;
            if (day == 3 && d == 4) // We have a lot of info about this specific pair so
                                    // we might as well use it
                solution = getD5EV();
            else
                solution = getBestSchedule(d, reservedSet, false);
            if (verboseSolverLogging)
                System.out.println("Day " + (d + 1) + ", crafts: "
                        + Arrays.toString(solution.getKey().getItems().toArray())
                        + " value: " + solution.getValue());
            worstInFuture = Math.min(worstInFuture, solution.getValue());
            reservedSet.addAll(solution.getKey().getItems());
        }
        if (verboseSolverLogging)
            System.out.println("Worst future day: " + worstInFuture);

        return rec.getValue() <= worstInFuture;
    }

    // Specifically for comparing D4 to D5
    public static Entry<WorkshopSchedule, Integer> getD5EV()
    {
        Entry<WorkshopSchedule, Integer> solution = getBestSchedule(4, null);
        if (verboseSolverLogging)
            System.out.println(
                    "Testing against D5 solution " + solution.getKey().getItems());
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

    public static void addDay(List<Item> crafts, int day)
    {

        System.out.println(
                "Day " + (day + 1) + ", crafts: " + Arrays.toString(crafts.toArray()));

        addDay(crafts, crafts, crafts, day);
    }

    public static void addDay(List<Item> crafts0, List<Item> crafts1, List<Item> crafts2,
            int day)
    {
        CycleSchedule schedule = new CycleSchedule(day, groove);
        schedule.setWorkshop(0, crafts0);
        schedule.setWorkshop(1, crafts1);
        schedule.setWorkshop(2, crafts2);

        int gross = schedule.getValue();
        totalGross += gross;

        int net = gross - schedule.getMaterialCost();
        totalNet += net;
        int startingGroove = groove;
        groove = schedule.endingGroove;

        schedule.startingGroove = 0;
        boolean oldVerbose = verboseCalculatorLogging;
        verboseCalculatorLogging = false;
        System.out.println("day " + (day + 1) + " total, 0 groove: " + schedule.getValue()
                + ". Starting groove " + startingGroove + ": " + gross + ", net " + net
                + ".");
        verboseCalculatorLogging = oldVerbose;
        schedule.numCrafted.forEach((k, v) ->
        {
            items[k.ordinal()].addCrafted(v, day);
        });
    }

    private static Map.Entry<WorkshopSchedule, Integer> getBestBruteForceSchedule(int day,
            Set<Item> cannotUse, boolean allowAllOthers)
    {

        HashMap<WorkshopSchedule, Integer> safeSchedules = new HashMap<>();
        List<List<Item>> filteredItemLists;

        if (cannotUse == null)
        {
            filteredItemLists = CSVImporter.allEfficientChains.stream()
                    .filter(list -> list.stream().allMatch(
                            item -> items[item.ordinal()].rankUnlocked <= islandRank))
                    .filter(list -> list.stream().allMatch(
                            item -> items[item.ordinal()].peaksOnOrBeforeDay(day)))
                    .collect(Collectors.toList());

        } else
        {
            Stream<List<Item>> filteredStream = CSVImporter.allEfficientChains.stream()
                    .filter(list -> list.stream().allMatch(
                            item -> items[item.ordinal()].rankUnlocked <= islandRank))
                    .filter(list -> list.stream()
                            .allMatch(item -> !cannotUse.contains(item)));
            if (!allowAllOthers)
                filteredStream = filteredStream.filter(list -> list.stream()
                        .allMatch(item -> items[item.ordinal()].peaksOnOrBeforeDay(day)));

            filteredItemLists = filteredStream.collect(Collectors.toList());

        }

        for (List<Item> list : filteredItemLists)
        {
            addToScheduleMap(list, day, safeSchedules);
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

    private static Map.Entry<WorkshopSchedule, Integer> getBestSchedule(int day,
            Set<Item> reservedForLater)
    {
        return getBestSchedule(day, reservedForLater, true);
    }

    private static Map.Entry<WorkshopSchedule, Integer> getBestSchedule(int day,
            Set<Item> reservedForLater, boolean allowAllOthers)
    {
            return getBestBruteForceSchedule(day, reservedForLater, allowAllOthers);
    }
    
    private static int addToScheduleMap(List<Item> list, int day,
            HashMap<WorkshopSchedule, Integer> safeSchedules)
    {
        WorkshopSchedule workshop = new WorkshopSchedule(list);
        int value = workshop.getValueWithGrooveEstimate(day, groove);
        // Only add if we don't already have one with this schedule or ours is better
        int oldValue = safeSchedules.getOrDefault(workshop, -1);

        if (oldValue < value)
        {
            if (verboseSolverLogging && list.contains(Jam))
                System.out.println("Replacing schedule with mats "
                        + workshop.rareMaterialsRequired + " with " + list + " because "
                        + value + " is higher than " + oldValue);
            safeSchedules.remove(workshop); // It doesn't seem to update the key when
                                            // updating the value, so we delete the key
                                            // first
            safeSchedules.put(workshop, value);
        } else
        {
            if (verboseSolverLogging && list.contains(Jam))
                System.out.println("Not replacing schedule with mats "
                        + workshop.rareMaterialsRequired + " with " + list + " because "
                        + value + " is lower than " + oldValue);

            value = 0;
        }

        return value;

    }

    private static void setInitialFromCSV()
    {
        for (int i = 0; i < items.length; i++)
        {
            items[i].setInitialData(CSVImporter.currentPopularity[i],
                    CSVImporter.lastWeekPeaks[i],
                    CSVImporter.observedSupplies.get(i).get(0));
        }
    }

    private static boolean setObservedFromCSV(int day)
    {
        if (day >= CSVImporter.observedSupplies.get(0).size())
        {
            return false;
        }
        for (int i = 0; i < items.length; i++)
        {
            ObservedSupply ob = CSVImporter.observedSupplies.get(i).get(day);
            items[i].addObservedDay(ob);
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
