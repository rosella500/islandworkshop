package islandworkshop;

import static islandworkshop.ItemCategory.*;
import static islandworkshop.RareMaterial.*;
import static islandworkshop.Supply.*;
import static islandworkshop.Popularity.*;
import static islandworkshop.DemandShift.*;
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
    final static double WORKSHOP_BONUS = 1.2;
    final static int GROOVE_MAX = 35;
    
    final static ItemInfo[] items = {
            new ItemInfo(Potion,Concoctions,Invalid,28,4,null),
            new ItemInfo(Firesand,Concoctions,UnburiedTreasures,28,4,null),
            new ItemInfo(WoodenChair,Furnishings,Woodworks,42,6,null),
            new ItemInfo(GrilledClam,Foodstuffs,MarineMerchandise,28,4,null),
            new ItemInfo(Necklace,Accessories,Woodworks,28,4,null),
            new ItemInfo(CoralRing,Accessories,MarineMerchandise,42,6,null),
            new ItemInfo(Barbut,Attire,Metalworks,42,6,null),
            new ItemInfo(Macuahuitl,Arms,Woodworks,42,6,null),
            new ItemInfo(Sauerkraut,PreservedFood,Invalid,40,4,Map.of(Cabbage,1)),
            new ItemInfo(BakedPumpkin,Foodstuffs,Invalid,40,4,Map.of(Pumpkin,1)),
            new ItemInfo(Tunic,Attire,Textiles,72,6,Map.of(Fleece,2)),
            new ItemInfo(CulinaryKnife,Sundries,CreatureCreations,44,4,Map.of(Claw,1)),
            new ItemInfo(Brush,Sundries,Woodworks,44,4,Map.of(Fur, 1)),
            new ItemInfo(BoiledEgg,Foodstuffs,CreatureCreations,44,4,Map.of(Egg, 1)),
            new ItemInfo(Hora,Arms,CreatureCreations,72,6,Map.of(Carapace, 2)),
            new ItemInfo(Earrings,Accessories,CreatureCreations,44,4,Map.of(Fang, 1)),
            new ItemInfo(Butter,Ingredients,CreatureCreations,44,4,Map.of(Milk, 1)),
            new ItemInfo(BrickCounter,Furnishings,UnburiedTreasures,48,6,null),
            new ItemInfo(BronzeSheep,Furnishings,Metalworks,64,8,null),
            new ItemInfo(GrowthFormula,Concoctions,Invalid,136,8,Map.of(Alyssum, 2)),
            new ItemInfo(GarnetRapier,Arms,UnburiedTreasures,136,8,Map.of(Garnet,2)),
            new ItemInfo(SpruceRoundShield,Attire,Woodworks,136,8,Map.of(Spruce,2)),
            new ItemInfo(SharkOil,Sundries,MarineMerchandise,136,8,Map.of(Shark,2)),
            new ItemInfo(SilverEarCuffs,Accessories,Metalworks,136,8,Map.of(Silver,2)),
            new ItemInfo(SweetPopoto,Confections,Invalid,72,6,Map.of(Popoto, 2, Milk,1)),
            new ItemInfo(ParsnipSalad,Foodstuffs,Invalid,48,4,Map.of(Parsnip,2)),
            new ItemInfo(Caramels,Confections,Invalid,81,6,Map.of(Milk,2)),
            new ItemInfo(Ribbon,Accessories,Textiles,54,6,null),
            new ItemInfo(Rope,Sundries,Textiles,36,4,null),
            new ItemInfo(CavaliersHat,Attire,Textiles,81,6,Map.of(Feather,2)),
            new ItemInfo(Item.Horn,Sundries,CreatureCreations,81,6,Map.of(RareMaterial.Horn,2)),
            new ItemInfo(SaltCod,PreservedFood,MarineMerchandise,54,6,null),
            new ItemInfo(SquidInk,Ingredients,MarineMerchandise,36,4,null),
            new ItemInfo(EssentialDraught,Concoctions,MarineMerchandise,54,6,null),
            new ItemInfo(Jam,Ingredients,Invalid,78,6,Map.of(Isleberry,3)),
            new ItemInfo(TomatoRelish,Ingredients,Invalid,52,4,Map.of(Tomato,2)),
            new ItemInfo(OnionSoup,Foodstuffs,Invalid,78,6,Map.of(Onion,3)),
            new ItemInfo(Pie,Confections,MarineMerchandise,78,6,Map.of(Wheat,3)),
            new ItemInfo(CornFlakes,PreservedFood,Invalid,52,4,Map.of(Corn,2)),
            new ItemInfo(PickledRadish,PreservedFood,Invalid,104,8,Map.of(Radish,4)),
            new ItemInfo(IronAxe,Arms,Metalworks,72,8,null),
            new ItemInfo(QuartzRing,Accessories,UnburiedTreasures,72,8,null),
            new ItemInfo(PorcelainVase,Sundries,UnburiedTreasures,72,8,null),
            new ItemInfo(VegetableJuice,Concoctions,Invalid,78,6,Map.of(Cabbage,3)),
            new ItemInfo(PumpkinPudding,Confections,Invalid,78,6,Map.of(Pumpkin, 3, Egg, 1, Milk,1)),
            new ItemInfo(SheepfluffRug,Furnishings,CreatureCreations,90,6,Map.of(Fleece,3)),
            new ItemInfo(GardenScythe,Sundries,Metalworks,90,6,Map.of(Claw,3)),
            new ItemInfo(Bed,Furnishings,Textiles,120,8,Map.of(Fur,4)),
            new ItemInfo(ScaleFingers,Attire,CreatureCreations,120,8,Map.of(Carapace,4)),
            new ItemInfo(Crook,Arms,Woodworks,120,8,Map.of(Fang,4))};
    
    public static int groove = 0;
    public static int totalGross = 0;
    public static int totalNet = 0;
    public static boolean verboseCalculatorLogging = false;
    public static boolean verboseSolverLogging = false;
    public static int alternativesToDisplay = 0;
    public static int groovePerDay = 30;
    public static boolean rested = false;
    
    //I don't actually use this but I know the moment I get rid of it, I'll need it and have to do this work over again
    public static final Item[][] itemsByCategory = {{}, 
            {Sauerkraut, SaltCod, CornFlakes, PickledRadish}, 
            {Barbut, Tunic, SpruceRoundShield, CavaliersHat, ScaleFingers},
            {GrilledClam, BakedPumpkin, BoiledEgg, ParsnipSalad, OnionSoup},
            {SweetPopoto, Caramels, Pie, PumpkinPudding},
            {CulinaryKnife, Brush, SharkOil, Rope, Item.Horn, PorcelainVase, GardenScythe},
            {WoodenChair, BrickCounter, BronzeSheep, SheepfluffRug,Bed},
            {Macuahuitl, Hora, GarnetRapier, IronAxe, Crook},
            {Potion, Firesand, GrowthFormula, EssentialDraught, VegetableJuice},
            {Butter, SquidInk, Jam, TomatoRelish},
            {Necklace, CoralRing, Earrings, SilverEarCuffs, Ribbon, QuartzRing},
            {Barbut, BronzeSheep, SilverEarCuffs, IronAxe, GardenScythe},
            {WoodenChair, Necklace, Macuahuitl, Brush, SpruceRoundShield, Crook},
            {Tunic, Ribbon, Rope, CavaliersHat, Bed},
            {CulinaryKnife, BoiledEgg, Hora, Earrings, Butter, Item.Horn, SheepfluffRug, ScaleFingers},
            {GrilledClam, CoralRing, SharkOil, SaltCod, SquidInk, EssentialDraught, Pie},
            {Firesand, BrickCounter, GarnetRapier, QuartzRing, PorcelainVase}};

    
    public static void main(String[] args)
    {
        //TODO: Figure out a better way to enter data that isn't super slow
        
        /*
         * setWeek3Initial();
         * items[CulinaryKnife.ordinal()].addObservedDay(Insufficient, Increasing);
         * 
         * 
         * alternativesToDisplay = 5; verboseSolverLogging = true;
         * getBestScheduleForCycle(1, null);
         */
        
        
        alternativesToDisplay = 0;
          setWeek4Initial();
          setOrRestEarlyWeek(1); 
          
          setWeek4Day2(); 
          setOrRestEarlyWeek(2) ;
          
          setWeek4Day3(); 
          setOrRestEarlyWeek(3) ;
          
          setWeek4Day4();
          setLateDays();
         

        System.out.println("Week total: " + totalGross + " (" + totalNet + ")");

    }
    
    private static void setLateDays()
    {
        Entry<List<Item>,Integer> cycle5Sched = getBestScheduleForCycle(4,null);
        Entry<List<Item>,Integer> cycle6Sched = getBestScheduleForCycle(5,null);
        Entry<List<Item>,Integer> cycle7Sched = getBestScheduleForCycle(6,null);
        
        //I'm just hardcoding this, This could probably be improved
        
        List<Entry<List<Item>,Integer>> endOfWeekSchedules = new ArrayList<>();
        endOfWeekSchedules.add(cycle5Sched);
        endOfWeekSchedules.add(cycle6Sched);
        endOfWeekSchedules.add(cycle7Sched);
        
        int bestDay = -1;
        int bestDayValue = -1;
        int worstDay = -1;
        int worstDayValue = -1;
        
        
        for(int i=0; i<3; i++)
        {
            int value = endOfWeekSchedules.get(i).getValue();
            if(bestDay == -1 || value > bestDayValue)
            {
                bestDay = i+4;
                bestDayValue = value;
            }
            if(worstDay == -1 || value < worstDayValue)
            {
                worstDay = i+4;
                worstDayValue = value;
            }
        }
        
        if(bestDay == 4) //Day 5 is best
        {
            //System.out.println("Day 5 is best. Adding as-is");
            addDay(cycle5Sched.getKey(), 4);
            
            if(worstDay == 5)
            {
                //Day 6 is worst, so recalculate it according to day 7
                Entry<List<Item>,Integer> recalcedCycle7Sched = getBestScheduleForCycle(6,null);

                //System.out.println("Recalcing day 7");
                if(rested)
                {
                    //System.out.println("Recalcing day 6 using only day 7's requirements as verboten and adding");
                    addDay(getBestScheduleForCycle(5, new HashSet<Item>(recalcedCycle7Sched.getKey())).getKey(), 5);
                }
               // System.out.println("Adding day 7");
                addDay(recalcedCycle7Sched.getKey(), 6);
            }
            else
            {
                //System.out.println("Day 6 is second best, just recalcing and adding");
                addDay(getBestScheduleForCycle(5,null).getKey(), 5);
                if(rested)
                {
                   // System.out.println("Day 6 is second best, just recalcing and adding 7 too");
                    addDay(getBestScheduleForCycle(6,null).getKey(), 6);
                }
            }                
        }
        else if(bestDay == 6) //Day 7 is best
        {
            //System.out.println("Day 7 is best");
            HashSet<Item> reserved7Items = new HashSet<>(cycle7Sched.getKey());
            if(worstDay == 4 || rested) //Day 6 is second best or we're using all the days anyway
            {
                //System.out.println("Day 6 is second best or we're using all the days anyway. Recalcing 6 based on 7.");
                //Recalculate it in case it's better just reserving day 7
                Entry<List<Item>,Integer> recalcedCycle6Sched = getBestScheduleForCycle(5, reserved7Items);
                //System.out.println("Recalced 6: "+Arrays.toString(recalcedCycle6Sched.getKey().toArray())+" value: "+recalcedCycle6Sched.getValue());
                HashSet<Item> reserved67Items = new HashSet<Item>();
                reserved67Items.addAll(reserved7Items);
                reserved67Items.addAll(recalcedCycle6Sched.getKey());
            
                if(rested)
                {
                    Entry<List<Item>,Integer> recalcedCycle5Sched = getBestScheduleForCycle(4,reserved67Items);
                    //System.out.println("Recalced 5: "+Arrays.toString(recalcedCycle5Sched.getKey().toArray())+" value: "+recalcedCycle5Sched.getValue());
                    //System.out.println("Recalcing 5 based on 6. Is it better?");
                    
                    
                    //Only use the recalculation if it doesn't ruin D5 too badly
                    if(recalcedCycle5Sched.getValue() + recalcedCycle6Sched.getValue() > cycle5Sched.getValue() + cycle6Sched.getValue())
                    {
                        //System.out.println("It is! Using recalced 5");
                        addDay(recalcedCycle5Sched.getKey(), 4);
                    }
                    else
                    {
                        addDay(cycle5Sched.getKey(), 4);
                    }
                   //System.out.println("Recalcing 6 AGAIN just in case 5 changed it, still only forbidding things used day 7");
                   addDay(getBestScheduleForCycle(5, reserved7Items).getKey(), 5);
                }
                else
                    addDay(recalcedCycle6Sched.getKey(), 5);                
            }
            if(worstDay == 5) //Day 5 is second best and we aren't using day 6
            {
                ///System.out.println("Day 6 isn't being used so just recalc 5 based on day 7");
                addDay(getBestScheduleForCycle(4,reserved7Items).getKey(), 4);
            }
            //System.out.println("Adding recalced day 7");
            addDay(getBestScheduleForCycle(6, null).getKey(), 6);
            
        }
        else //Best day is Day 6
        {
            //System.out.println("Day 6 is best");
            if(rested || worstDay != 4)
            {
                //System.out.println("Adding day 5 as-is");
                addDay(cycle5Sched.getKey(), 4);
            }
            //System.out.println("Recalcing day 6 and adding");
            addDay(getBestScheduleForCycle(5, null).getKey(), 5);
            if(rested || worstDay != 6)
            {
                //System.out.println("Recalcing day 7 and adding");
                addDay(getBestScheduleForCycle(6, null).getKey(), 6);
            }
        }
    }
    
    private static void setOrRestEarlyWeek(int day)
    {
        Entry<List<Item>,Integer> sched = getBestScheduleForCycle(day,null);
        if(!rested && (sched == null || isWorseThanAllFollowing(sched.getValue(), day)))
        {
            System.out.println("Rest day "+(day + 1));
            rested = true;
        }
        else
            addDay(sched.getKey(), day);
    }
    
    private static boolean isWorseThanAllFollowing(int value, int day)
    {
        int worstInFuture = 99999;
        if(verboseSolverLogging)
        System.out.println("Comparing d"+(day+1)+" ("+value+") to worst-case future days");
        for(int d=day+1; d < 7; d++)
        {
            Entry<List<Item>,Integer> solution =  getBestScheduleForCycle(d, null);
            if(verboseSolverLogging)
            System.out.println("Day "+(d+1)+", crafts: "+Arrays.toString(solution.getKey().toArray())+" value: "+solution.getValue());
            worstInFuture = Math.min(worstInFuture,solution.getValue());
        }
        if(verboseSolverLogging)
        System.out.println("Worst future day: "+worstInFuture);
        return value <= worstInFuture + 100;
            
    }

    public static void addDay(List<Item> crafts, int day)
    {
       
       System.out.println("Day "+(day+1)+", crafts: "+Arrays.toString(crafts.toArray()));
       
        addDay(crafts, crafts, crafts, day);
    }

    public static void addDay(List<Item> crafts0, List<Item> crafts1, List<Item> crafts2, int day)
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
        System.out.println("day " + (day + 1) + " total, "+startingGroove+" starting groove: " + gross +"(" + net + "). With 0 groove: "+schedule.getValue());
        verboseCalculatorLogging = oldVerbose;
        schedule.numCrafted.forEach((k, v) ->
        {
            items[k.ordinal()].addCrafted(v, day);
        });
    }
    
    private static Map.Entry<List<Item>, Integer> getBestScheduleForCycle(int day, Set<Item> reservedForLater)
    {
        HashMap<ItemInfo, Integer> itemsByValue = new HashMap<>();
        for(ItemInfo item : items)
        {
            itemsByValue.put(item, item.getValueWithSupply(item.getSupplyBucketOnDay(day)));
        }
        
        HashMap<List<Item>, Integer> safeSchedules = new HashMap<>();
        
        LinkedHashMap<ItemInfo, Integer> sortedItems;
        
        if(reservedForLater == null)
        {
            sortedItems = itemsByValue.entrySet().stream().filter(entry -> entry.getKey().peaksOnOrBeforeDay(day)) //Only get things that are peaking/have peaked
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new)); //Then sort by descending order
        }
        else
        {
            sortedItems = itemsByValue.entrySet().stream().filter(entry -> !reservedForLater.contains(entry.getKey().item)) //Just filter out things that are on the reserved list
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
        }
        
        Iterator<Entry<ItemInfo, Integer>> topItemIt = sortedItems.entrySet().iterator();
        
        CycleSchedule sch = new CycleSchedule(day, 0);
        
        
        //This is a hard-coded mess, I'm sorry.
        while(topItemIt.hasNext())
        {
            Map.Entry<ItemInfo, Integer> topItem = topItemIt.next();
            if(verboseSolverLogging)
            System.out.println("Top item peaking today: "+topItem.getKey().item+": value: "+topItem.getValue());
            
            
            if(topItem.getKey().time == 8)
            {
                //Try 4-8-4-8 schedules
                //Get best 4 to pair it with
                ItemInfo fourHour = null;
                ItemInfo sixHour = null;
                ItemInfo eightHour = null;
                Iterator<Entry<ItemInfo, Integer>> eightMatchIt = sortedItems.entrySet().iterator();
                while(eightMatchIt.hasNext())
                {
                    ItemInfo possibleMatch = eightMatchIt.next().getKey();
                    //Try all 4 hours in case they change supply
                    if(possibleMatch.time == 4 && possibleMatch.getsEfficiencyBonus(topItem.getKey()))
                    {
                        fourHour = possibleMatch;
                        List<Item> items = Arrays.asList(fourHour.item, topItem.getKey().item, fourHour.item, topItem.getKey().item);
                        sch.setForAllWorkshops(items);
                        safeSchedules.put(items, sch.getValueWithGrooveEstimate());
                        if(verboseSolverLogging)
                        System.out.println("Compatible 4-hour craft found: "+fourHour.item+" adding safe 4-8-4-8 schedule with a value of "+safeSchedules.get(items));
                    }
                        
                    //Only need to check top 8 hour because we make it once
                    if(eightHour == null && (possibleMatch.time == 8 && possibleMatch.getsEfficiencyBonus(topItem.getKey())))
                    {
                        eightHour = possibleMatch;
                        List<Item> items = Arrays.asList(topItem.getKey().item, eightHour.item, topItem.getKey().item);
                        sch.setForAllWorkshops(items);
                        safeSchedules.put(items, sch.getValueWithGrooveEstimate());
                        if(verboseSolverLogging)
                        System.out.println("Compatible 8-hour craft found: "+eightHour.item+" adding safe 8-8-8 schedule with a value of "+safeSchedules.get(items));
                    }
                        
                    //Try all 6 hours because we make them twice and also they have different pairs
                    if((possibleMatch.time == 6 && possibleMatch.getsEfficiencyBonus(topItem.getKey())))
                    {
                        sixHour = possibleMatch;
                        
                        //Try all 4-6-8-6 schedules
                        if(verboseSolverLogging)
                        System.out.println("Compatible 6-hour found: "+sixHour.item);
                        Iterator<Entry<ItemInfo, Integer>> sixMatchIt = sortedItems.entrySet().iterator();
                        ItemInfo fourSixMatch = null;
                        
                        //Only need to check top 4 hour because we only make it once
                        while(fourSixMatch == null  && sixMatchIt.hasNext())
                        {
                            ItemInfo possible46Match = sixMatchIt.next().getKey();
                            if(possible46Match.time == 4 && possible46Match.getsEfficiencyBonus(sixHour))
                                fourSixMatch = possible46Match;
                        }
                        if(fourSixMatch != null)
                        {
                            List<Item> items = Arrays.asList(fourSixMatch.item, sixHour.item, topItem.getKey().item, sixHour.item);
                            sch.setForAllWorkshops(items);
                            safeSchedules.put(items, sch.getValueWithGrooveEstimate());
                            if(verboseSolverLogging)
                            System.out.println("Compatible 4-6 combo found: "+fourSixMatch.item+" adding safe 4-6-8-6 schedule with a value of "+safeSchedules.get(items));
                        }
                    }
                        
                }
            }
            else if(topItem.getKey().time == 6)
            {
                //Try 4-6-8-6 schedules
                ItemInfo bestFourHour = null;
                ItemInfo fourHour = null;
                ItemInfo sixHour = null;
                ItemInfo eightHour = null;
                Iterator<Entry<ItemInfo, Integer>> sixMatchIt = sortedItems.entrySet().iterator();
                while(sixMatchIt.hasNext())
                {
                    ItemInfo possibleMatch = sixMatchIt.next().getKey();
                    //Need to try all 4-hours because we make them multiple times and they pair up
                    if(possibleMatch.time == 4 && possibleMatch.getsEfficiencyBonus(topItem.getKey()))
                    {
                        fourHour = possibleMatch;
                        if(bestFourHour == null)
                            bestFourHour = fourHour;
                        
                        //Might as well add 4-6-4-6-4, I guess
                        List<Item> items = Arrays.asList(fourHour.item, topItem.getKey().item, fourHour.item, topItem.getKey().item, fourHour.item);
                        sch.setForAllWorkshops(items);
                        safeSchedules.put(items, sch.getValueWithGrooveEstimate());
                        if(verboseSolverLogging)
                        System.out.println("Compatible 4 combo found: "+fourHour.item+" adding safe 4-6-4-6-4 schedule with a value of "+safeSchedules.get(items));
                        
                        //Try all 4-4-6-4-6
                        Iterator<Entry<ItemInfo, Integer>> fourMatchIt = sortedItems.entrySet().iterator();
                        ItemInfo fourFourMatch = null;
                        
                        //Get the best 4 to pair with the other 4
                        while(fourFourMatch == null  && fourMatchIt.hasNext())
                        {
                            ItemInfo possible44Match = fourMatchIt.next().getKey();
                            if(possible44Match.time == 4 && possible44Match.getsEfficiencyBonus(fourHour))
                                fourFourMatch = possible44Match;
                        }
                        if(fourFourMatch != null)
                        {
                            List<Item> items44 = Arrays.asList(fourFourMatch.item, fourHour.item, topItem.getKey().item, fourHour.item, topItem.getKey().item);
                            sch.setForAllWorkshops(items44);
                            safeSchedules.put(items44, sch.getValueWithGrooveEstimate());
                            if(verboseSolverLogging)
                            System.out.println("Compatible 4-4 combo found: "+fourFourMatch.item+" adding safe 4-4-6-4-6 schedule with a value of "+safeSchedules.get(items44));
                        }
                    }
                    if(eightHour == null && possibleMatch.time == 8 && possibleMatch.getsEfficiencyBonus(topItem.getKey()))
                    {
                        eightHour = possibleMatch;
                    }
                        
                    //Need to try all 6 hours in case they go up in supply
                    if(possibleMatch.time == 6 && possibleMatch.getsEfficiencyBonus(topItem.getKey()))
                    {
                        sixHour = possibleMatch; 
                        List<Item> items = Arrays.asList(sixHour.item, topItem.getKey().item, sixHour.item, topItem.getKey().item);
                        sch.setForAllWorkshops(items);
                        safeSchedules.put(items, sch.getValueWithGrooveEstimate());
                        if(verboseSolverLogging)
                        System.out.println("Compatible 6-hour craft found: "+sixHour.item+" adding safe 6-6-6-6 schedule with a value of "+safeSchedules.get(items));
                    }                    
                }
                
                //Only need first one because we only make them once
                //Try 4-6-8-6
                if(bestFourHour != null && eightHour != null)
                {
                    List<Item> items = Arrays.asList(bestFourHour.item, topItem.getKey().item, eightHour.item, topItem.getKey().item);
                    sch.setForAllWorkshops(items);
                    safeSchedules.put(items, sch.getValueWithGrooveEstimate());
                    if(verboseSolverLogging)
                    System.out.println("Compatible 8-hour craft found: "+eightHour.item+" adding safe 4-6-8-6 schedule with a value of "+safeSchedules.get(items));
                }
            }
            else
            {
                //I refuse to optimize for a 4-craft. 
            }
        }
        
        
        
        
        if(safeSchedules.size() == 0)
            return null;
        
        LinkedHashMap<List<Item>, Integer> sortedSchedules = 
                safeSchedules.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new)); 
        
        Iterator<Entry<List<Item>, Integer>> finalIterator = sortedSchedules.entrySet().iterator();
        Entry<List<Item>,Integer> bestSchedule = finalIterator.next();
        if(alternativesToDisplay > 0)
        {
            System.out.println("Best rec: "+Arrays.toString(bestSchedule.getKey().toArray())+": "+bestSchedule.getValue());
            for(int c=0; c<alternativesToDisplay && finalIterator.hasNext(); c++)
            {
                Entry<List<Item>,Integer> alt = finalIterator.next();
                System.out.println("Alternative rec: "+Arrays.toString(alt.getKey().toArray())+": "+alt.getValue());
            }
        }
        
        return bestSchedule;
    }
    
    
    private static void setWeek4Initial()
    {
        items[Potion.ordinal()].setInitialData(High,Cycle7Weak);
        items[Firesand.ordinal()].setInitialData(VeryHigh,Cycle6Strong);
        items[WoodenChair.ordinal()].setInitialData(VeryHigh,Cycle5Strong);
        items[GrilledClam.ordinal()].setInitialData(Average,Cycle4Strong);
        items[Necklace.ordinal()].setInitialData(Low,Cycle7Strong);
        items[CoralRing.ordinal()].setInitialData(Average,Cycle7Weak,Decreasing);
        items[Barbut.ordinal()].setInitialData(VeryHigh,Cycle4Strong);
        items[Macuahuitl.ordinal()].setInitialData(VeryHigh,Cycle4Weak,Skyrocketing);
        items[Sauerkraut.ordinal()].setInitialData(Average,Cycle3Strong);
        items[BakedPumpkin.ordinal()].setInitialData(Low,Cycle6Weak);
        items[Tunic.ordinal()].setInitialData(Average,Cycle6Strong);
        items[CulinaryKnife.ordinal()].setInitialData(Average,Cycle2Weak);
        items[Brush.ordinal()].setInitialData(Low,Cycle6Strong);
        items[BoiledEgg.ordinal()].setInitialData(High,Cycle5Weak);
        items[Hora.ordinal()].setInitialData(High,Cycle5Weak);
        items[Earrings.ordinal()].setInitialData(High,Cycle3Weak);
        items[Butter.ordinal()].setInitialData(High,Cycle7Weak,Decreasing);
        items[BrickCounter.ordinal()].setInitialData(High,Cycle7Weak,Decreasing);
        items[BronzeSheep.ordinal()].setInitialData(VeryHigh,Cycle5Strong);
        items[GrowthFormula.ordinal()].setInitialData(Average,Cycle3Strong);
        items[GarnetRapier.ordinal()].setInitialData(Low,Cycle7Weak);
        items[SpruceRoundShield.ordinal()].setInitialData(Low,Cycle6Weak);
        items[SharkOil.ordinal()].setInitialData(Average,Cycle4Weak);
        items[SilverEarCuffs.ordinal()].setInitialData(High,Cycle5Weak);
        items[SweetPopoto.ordinal()].setInitialData(VeryHigh,Cycle6Strong);
        items[ParsnipSalad.ordinal()].setInitialData(VeryHigh,Cycle3Weak);
        items[Caramels.ordinal()].setInitialData(Average,Cycle3Weak);
        items[Ribbon.ordinal()].setInitialData(High,Cycle5Strong);
        items[Rope.ordinal()].setInitialData(Average,Cycle7Strong);
        items[CavaliersHat.ordinal()].setInitialData(Average,Cycle7Strong);
        items[Item.Horn.ordinal()].setInitialData(Low,Cycle3Weak);
        items[SaltCod.ordinal()].setInitialData(High,Cycle7Strong,Plummeting);
        items[SquidInk.ordinal()].setInitialData(VeryHigh,Cycle2Strong);
        items[EssentialDraught.ordinal()].setInitialData(VeryHigh,Cycle2Strong);
        items[Jam.ordinal()].setInitialData(High,Cycle2Strong,Skyrocketing);
        items[TomatoRelish.ordinal()].setInitialData(Average,Cycle5Strong,Skyrocketing);
        items[OnionSoup.ordinal()].setInitialData(Average,Cycle3Strong);
        items[Pie.ordinal()].setInitialData(Average,Cycle4Weak);
        items[CornFlakes.ordinal()].setInitialData(High,Cycle6Weak,Skyrocketing);
        items[PickledRadish.ordinal()].setInitialData(High,Cycle5Weak);
        items[IronAxe.ordinal()].setInitialData(VeryHigh,Cycle7Strong);
        items[QuartzRing.ordinal()].setInitialData(Average,Cycle6Weak);
        items[PorcelainVase.ordinal()].setInitialData(Low,Cycle4Weak);
        items[VegetableJuice.ordinal()].setInitialData(Average,Cycle2Weak);
        items[PumpkinPudding.ordinal()].setInitialData(High,Cycle4Strong);
        items[SheepfluffRug.ordinal()].setInitialData(High,Cycle2Strong);
        items[GardenScythe.ordinal()].setInitialData(High,Cycle4Strong);
        items[Bed.ordinal()].setInitialData(Low,Cycle2Weak);
        items[ScaleFingers.ordinal()].setInitialData(VeryHigh,Cycle2Weak);
        items[Crook.ordinal()].setInitialData(VeryHigh,Cycle3Strong);
    }
    
    private static void setWeek3Initial()
    {
        items[Potion.ordinal()].setInitialData(Low,Cycle4Weak);
        items[Firesand.ordinal()].setInitialData(VeryHigh,Cycle3Weak);
        items[WoodenChair.ordinal()].setInitialData(Average,Cycle7Strong);
        items[GrilledClam.ordinal()].setInitialData(High,Cycle3Weak);
        items[Necklace.ordinal()].setInitialData(Low,Cycle6Weak);
        items[CoralRing.ordinal()].setInitialData(High,Cycle7Weak);
        items[Barbut.ordinal()].setInitialData(Low,Cycle4Weak);
        items[Macuahuitl.ordinal()].setInitialData(Average,Cycle3Weak);
        items[Sauerkraut.ordinal()].setInitialData(High,Cycle6Strong);
        items[BakedPumpkin.ordinal()].setInitialData(Average,Cycle2Strong);
        items[Tunic.ordinal()].setInitialData(VeryHigh,Cycle5Strong);
        items[CulinaryKnife.ordinal()].setInitialData(Average,Cycle2Weak,Skyrocketing);
        items[Brush.ordinal()].setInitialData(VeryHigh,Cycle5Weak);
        items[BoiledEgg.ordinal()].setInitialData(Average,Cycle6Weak);
        items[Hora.ordinal()].setInitialData(High,Cycle3Strong);
        items[Earrings.ordinal()].setInitialData(High,Cycle5Strong);
        items[Butter.ordinal()].setInitialData(VeryHigh,Cycle7Strong);
        items[BrickCounter.ordinal()].setInitialData(VeryHigh,Cycle2Strong);
        items[BronzeSheep.ordinal()].setInitialData(VeryHigh,Cycle2Weak);
        items[GrowthFormula.ordinal()].setInitialData(Average,Cycle5Strong);
        items[GarnetRapier.ordinal()].setInitialData(VeryHigh,Cycle7Strong);
        items[SpruceRoundShield.ordinal()].setInitialData(High,Cycle5Weak);
        items[SharkOil.ordinal()].setInitialData(Low,Cycle2Strong);
        items[SilverEarCuffs.ordinal()].setInitialData(Average,Cycle7Weak);
        items[SweetPopoto.ordinal()].setInitialData(High,Cycle3Weak);
        items[ParsnipSalad.ordinal()].setInitialData(High,Cycle2Weak);
        items[Caramels.ordinal()].setInitialData(VeryHigh,Cycle6Strong);
        items[Ribbon.ordinal()].setInitialData(VeryHigh,Cycle7Weak);
        items[Rope.ordinal()].setInitialData(High,Cycle2Weak);
        items[CavaliersHat.ordinal()].setInitialData(High,Cycle6Weak);
        items[Item.Horn.ordinal()].setInitialData(Average,Cycle6Strong);
        items[SaltCod.ordinal()].setInitialData(VeryHigh,Cycle7Strong);
        items[SquidInk.ordinal()].setInitialData(High,Cycle6Strong,Skyrocketing);
        items[EssentialDraught.ordinal()].setInitialData(High,Cycle4Strong,Skyrocketing);
        items[Jam.ordinal()].setInitialData(Average,Cycle2Strong,Skyrocketing);
        items[TomatoRelish.ordinal()].setInitialData(Average,Cycle6Weak);
        items[OnionSoup.ordinal()].setInitialData(Low,Cycle7Strong);
        items[Pie.ordinal()].setInitialData(High,Cycle3Strong);
        items[CornFlakes.ordinal()].setInitialData(Average,Cycle4Weak);
        items[PickledRadish.ordinal()].setInitialData(VeryHigh,Cycle4Strong);
        items[IronAxe.ordinal()].setInitialData(Low,Cycle4Weak);
        items[QuartzRing.ordinal()].setInitialData(High,Cycle4Strong);
        items[PorcelainVase.ordinal()].setInitialData(Average,Cycle5Weak);
        items[VegetableJuice.ordinal()].setInitialData(Low,Cycle7Weak,Decreasing);
        items[PumpkinPudding.ordinal()].setInitialData(VeryHigh,Cycle4Strong);
        items[SheepfluffRug.ordinal()].setInitialData(Average,Cycle5Strong,Skyrocketing);
        items[GardenScythe.ordinal()].setInitialData(High,Cycle7Weak);
        items[Bed.ordinal()].setInitialData(Low,Cycle3Strong,Increasing);
        items[ScaleFingers.ordinal()].setInitialData(Average,Cycle3Strong,Increasing);
        items[Crook.ordinal()].setInitialData(Average,Cycle5Weak);
    }
    
    
    private static void setWeek5Initial()
    {
        items[Potion.ordinal()].setInitialData(Average,Cycle4Weak);
        items[Firesand.ordinal()].setInitialData(High,Cycle6Weak);
        items[WoodenChair.ordinal()].setInitialData(High,Cycle7Weak);
        items[GrilledClam.ordinal()].setInitialData(Average,Cycle6Weak);
        items[Necklace.ordinal()].setInitialData(High,Cycle7Strong);
        items[CoralRing.ordinal()].setInitialData(Average,Cycle2Weak);
        items[Barbut.ordinal()].setInitialData(VeryHigh,Cycle7Strong);
        items[Macuahuitl.ordinal()].setInitialData(Low,Cycle2Strong);
        items[Sauerkraut.ordinal()].setInitialData(Low,Cycle7Weak);
        items[BakedPumpkin.ordinal()].setInitialData(VeryHigh,Cycle5Weak);
        items[Tunic.ordinal()].setInitialData(Average,Cycle4Weak);
        items[CulinaryKnife.ordinal()].setInitialData(High,Cycle3Strong);
        items[Brush.ordinal()].setInitialData(VeryHigh,Cycle4Strong);
        items[BoiledEgg.ordinal()].setInitialData(Low,Cycle3Weak);
        items[Hora.ordinal()].setInitialData(Average,Cycle5Strong);
        items[Earrings.ordinal()].setInitialData(High,Cycle6Strong);
        items[Butter.ordinal()].setInitialData(VeryHigh,Cycle2Weak);
        items[BrickCounter.ordinal()].setInitialData(Low,Cycle2Weak);
        items[BronzeSheep.ordinal()].setInitialData(High,Cycle6Strong);
        items[GrowthFormula.ordinal()].setInitialData(Average,Cycle4Weak);
        items[GarnetRapier.ordinal()].setInitialData(High,Cycle7Strong);
        items[SpruceRoundShield.ordinal()].setInitialData(Average,Cycle6Strong);
        items[SharkOil.ordinal()].setInitialData(VeryHigh,Cycle3Weak);
        items[SilverEarCuffs.ordinal()].setInitialData(VeryHigh,Cycle3Weak);
        items[SweetPopoto.ordinal()].setInitialData(Average,Cycle3Weak);
        items[ParsnipSalad.ordinal()].setInitialData(High,Cycle3Strong);
        items[Caramels.ordinal()].setInitialData(High,Cycle6Strong);
        items[Ribbon.ordinal()].setInitialData(Low,Cycle6Weak);
        items[Rope.ordinal()].setInitialData(Average,Cycle5Strong);
        items[CavaliersHat.ordinal()].setInitialData(VeryHigh,Cycle4Weak);
        items[Item.Horn.ordinal()].setInitialData(Average,Cycle7Weak);
        items[SaltCod.ordinal()].setInitialData(VeryHigh,Cycle2Weak);
        items[SquidInk.ordinal()].setInitialData(VeryHigh,Cycle7Strong);
        items[EssentialDraught.ordinal()].setInitialData(VeryHigh,Cycle7Weak);
        items[Jam.ordinal()].setInitialData(VeryHigh,Cycle2Strong);
        items[TomatoRelish.ordinal()].setInitialData(High,Cycle2Strong);
        items[OnionSoup.ordinal()].setInitialData(Low,Cycle4Strong);
        items[Pie.ordinal()].setInitialData(Average,Cycle5Weak);
        items[CornFlakes.ordinal()].setInitialData(Average,Cycle2Strong);
        items[PickledRadish.ordinal()].setInitialData(VeryHigh,Cycle5Weak);
        items[IronAxe.ordinal()].setInitialData(High,Cycle4Strong);
        items[QuartzRing.ordinal()].setInitialData(Low,Cycle3Strong);
        items[PorcelainVase.ordinal()].setInitialData(Average,Cycle5Strong);
        items[VegetableJuice.ordinal()].setInitialData(Average,Cycle5Strong);
        items[PumpkinPudding.ordinal()].setInitialData(Low,Cycle5Weak);
        items[SheepfluffRug.ordinal()].setInitialData(High,Cycle6Weak);
        items[GardenScythe.ordinal()].setInitialData(High,Cycle3Strong);
        items[Bed.ordinal()].setInitialData(VeryHigh,Cycle4Strong);
        items[ScaleFingers.ordinal()].setInitialData(Average,Cycle7Strong);
        items[Crook.ordinal()].setInitialData(High,Cycle7Weak);
    }
    
    private static void setWeek4Day2()
    {
        items[Potion.ordinal()].addObservedDay(Sufficient, None);
        items[Firesand.ordinal()].addObservedDay(Insufficient, None);
        items[WoodenChair.ordinal()].addObservedDay(Insufficient, None);
        items[GrilledClam.ordinal()].addObservedDay(Insufficient, None);
        items[Necklace.ordinal()].addObservedDay(Insufficient, None);
        items[CoralRing.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Barbut.ordinal()].addObservedDay(Insufficient, None);
        items[Macuahuitl.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[Sauerkraut.ordinal()].addObservedDay(Insufficient, None);
        items[BakedPumpkin.ordinal()].addObservedDay(Sufficient, None);
        items[Tunic.ordinal()].addObservedDay(Sufficient, None);
        items[CulinaryKnife.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[Brush.ordinal()].addObservedDay(Sufficient, None);
        items[BoiledEgg.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Hora.ordinal()].addObservedDay(Sufficient, None);
        items[Earrings.ordinal()].addObservedDay(Insufficient, None);
        items[Butter.ordinal()].addObservedDay(Insufficient, Increasing);
        items[BrickCounter.ordinal()].addObservedDay(Insufficient, Increasing);
        items[BronzeSheep.ordinal()].addObservedDay(Insufficient, None);
        items[GrowthFormula.ordinal()].addObservedDay(Sufficient, None);
        items[GarnetRapier.ordinal()].addObservedDay(Insufficient, None);
        items[SpruceRoundShield.ordinal()].addObservedDay(Insufficient, None);
        items[SharkOil.ordinal()].addObservedDay(Insufficient, Increasing);
        items[SilverEarCuffs.ordinal()].addObservedDay(Insufficient, Increasing);
        items[SweetPopoto.ordinal()].addObservedDay(Insufficient, Increasing);
        items[ParsnipSalad.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[Caramels.ordinal()].addObservedDay(Insufficient, None);
        items[Ribbon.ordinal()].addObservedDay(Insufficient, None);
        items[Rope.ordinal()].addObservedDay(Sufficient, None);
        items[CavaliersHat.ordinal()].addObservedDay(Sufficient, None);
        items[Item.Horn.ordinal()].addObservedDay(Insufficient, None);
        items[SaltCod.ordinal()].addObservedDay(Insufficient, Increasing);
        items[SquidInk.ordinal()].addObservedDay(Insufficient, None);
        items[EssentialDraught.ordinal()].addObservedDay(Insufficient, None);
        items[Jam.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[TomatoRelish.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[OnionSoup.ordinal()].addObservedDay(Sufficient, None);
        items[Pie.ordinal()].addObservedDay(Sufficient, None);
        items[CornFlakes.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[PickledRadish.ordinal()].addObservedDay(Sufficient, None);
        items[IronAxe.ordinal()].addObservedDay(Sufficient, None);
        items[QuartzRing.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[PorcelainVase.ordinal()].addObservedDay(Sufficient, None);
        items[VegetableJuice.ordinal()].addObservedDay(Sufficient, None);
        items[PumpkinPudding.ordinal()].addObservedDay(Sufficient, None);
        items[SheepfluffRug.ordinal()].addObservedDay(Insufficient, None);
        items[GardenScythe.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[Bed.ordinal()].addObservedDay(Sufficient, None);
        items[ScaleFingers.ordinal()].addObservedDay(Insufficient, None);
        items[Crook.ordinal()].addObservedDay(Insufficient, None);
    }
    
    private static void setWeek4Day3()
    {
        items[Potion.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Firesand.ordinal()].addObservedDay(Sufficient, Decreasing);
        items[WoodenChair.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[GrilledClam.ordinal()].addObservedDay(Sufficient, Decreasing);
        items[Necklace.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[CoralRing.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Barbut.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Macuahuitl.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Sauerkraut.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[BakedPumpkin.ordinal()].addObservedDay(Sufficient, None);
        items[Tunic.ordinal()].addObservedDay(Insufficient, Increasing);
        items[CulinaryKnife.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[Brush.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[BoiledEgg.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Hora.ordinal()].addObservedDay(Sufficient, None);
        items[Earrings.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Butter.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[BrickCounter.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[BronzeSheep.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[GrowthFormula.ordinal()].addObservedDay(Insufficient, Increasing);
        items[GarnetRapier.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[SpruceRoundShield.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[SharkOil.ordinal()].addObservedDay(Insufficient, Increasing);
        items[SilverEarCuffs.ordinal()].addObservedDay(Insufficient, Increasing);
        items[SweetPopoto.ordinal()].addObservedDay(Insufficient, Increasing);
        items[ParsnipSalad.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[Caramels.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Ribbon.ordinal()].addObservedDay(Sufficient, Decreasing);
        items[Rope.ordinal()].addObservedDay(Sufficient, None);
        items[CavaliersHat.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Item.Horn.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[SaltCod.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[SquidInk.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[EssentialDraught.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Jam.ordinal()].addObservedDay(Surplus, Plummeting);
        items[TomatoRelish.ordinal()].addObservedDay(Surplus, Plummeting);
        items[OnionSoup.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[Pie.ordinal()].addObservedDay(Sufficient, None);
        items[CornFlakes.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[PickledRadish.ordinal()].addObservedDay(Sufficient, None);
        items[IronAxe.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[QuartzRing.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[PorcelainVase.ordinal()].addObservedDay(Sufficient, None);
        items[VegetableJuice.ordinal()].addObservedDay(Sufficient, None);
        items[PumpkinPudding.ordinal()].addObservedDay(Sufficient, None);
        items[SheepfluffRug.ordinal()].addObservedDay(Sufficient, Decreasing);
        items[GardenScythe.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[Bed.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[ScaleFingers.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Crook.ordinal()].addObservedDay(Sufficient, Plummeting);
    }
    
    private static void setWeek4Day4()
    {
        items[Potion.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Firesand.ordinal()].addObservedDay(Sufficient, Increasing);
        items[WoodenChair.ordinal()].addObservedDay(Sufficient, Increasing);
        items[GrilledClam.ordinal()].addObservedDay(Sufficient, Increasing);
        items[Necklace.ordinal()].addObservedDay(Sufficient, None);
        items[CoralRing.ordinal()].addObservedDay(Sufficient, None);
        items[Barbut.ordinal()].addObservedDay(Sufficient, None);
        items[Macuahuitl.ordinal()].addObservedDay(Sufficient, None);
        items[Sauerkraut.ordinal()].addObservedDay(Sufficient, Increasing);
        items[BakedPumpkin.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Tunic.ordinal()].addObservedDay(Insufficient, Increasing);
        items[CulinaryKnife.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Brush.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[BoiledEgg.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Hora.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[Earrings.ordinal()].addObservedDay(Sufficient, Skyrocketing);
        items[Butter.ordinal()].addObservedDay(Sufficient, None);
        items[BrickCounter.ordinal()].addObservedDay(Sufficient, None);
        items[BronzeSheep.ordinal()].addObservedDay(Sufficient, Skyrocketing);
        items[GrowthFormula.ordinal()].addObservedDay(Insufficient, Increasing);
        items[GarnetRapier.ordinal()].addObservedDay(Sufficient, None);
        items[SpruceRoundShield.ordinal()].addObservedDay(Sufficient, Skyrocketing);
        items[SharkOil.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[SilverEarCuffs.ordinal()].addObservedDay(Surplus, Plummeting);
        items[SweetPopoto.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[ParsnipSalad.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[Caramels.ordinal()].addObservedDay(Sufficient, Skyrocketing);
        items[Ribbon.ordinal()].addObservedDay(Sufficient, Increasing);
        items[Rope.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[CavaliersHat.ordinal()].addObservedDay(Insufficient, Increasing);
        items[Item.Horn.ordinal()].addObservedDay(Sufficient, Increasing);
        items[SaltCod.ordinal()].addObservedDay(Sufficient, None);
        items[SquidInk.ordinal()].addObservedDay(Sufficient, None);
        items[EssentialDraught.ordinal()].addObservedDay(Sufficient, Increasing);
        items[Jam.ordinal()].addObservedDay(Surplus, None);
        items[TomatoRelish.ordinal()].addObservedDay(Surplus, None);
        items[OnionSoup.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[Pie.ordinal()].addObservedDay(Insufficient, Increasing);
        items[CornFlakes.ordinal()].addObservedDay(Sufficient, None);
        items[PickledRadish.ordinal()].addObservedDay(Insufficient, Increasing);
        items[IronAxe.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[QuartzRing.ordinal()].addObservedDay(Sufficient, Plummeting);
        items[PorcelainVase.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[VegetableJuice.ordinal()].addObservedDay(Insufficient, Skyrocketing);
        items[PumpkinPudding.ordinal()].addObservedDay(Insufficient, Increasing);
        items[SheepfluffRug.ordinal()].addObservedDay(Sufficient, Increasing);
        items[GardenScythe.ordinal()].addObservedDay(Surplus, Plummeting);
        items[Bed.ordinal()].addObservedDay(Nonexistent, Skyrocketing);
        items[ScaleFingers.ordinal()].addObservedDay(Sufficient, None);
        items[Crook.ordinal()].addObservedDay(Sufficient, Increasing);
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
