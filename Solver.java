package islandworkshop;

import static islandworkshop.ItemCategory.*;
import static islandworkshop.RareMaterial.*;
import static islandworkshop.Supply.*;
import static islandworkshop.Popularity.*;
import static islandworkshop.DemandShift.*;
import static islandworkshop.PeakCycle.*;
import static islandworkshop.Item.*;
import java.util.List;
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
        boolean rested = false;
        
        setWeek4Initial();
        rested = setOrRestEarlyWeek(rested, 1) || rested; //Must be in this order or we'll short-circuit out of actually adding the day

        setWeek4Day2();
        rested = setOrRestEarlyWeek(rested, 2) || rested; //Must be in this order or we'll short-circuit out of actually adding the day

        setWeek4Day3();
        rested = setOrRestEarlyWeek(rested, 3) || rested; //Must be in this order or we'll short-circuit out of actually adding the day

        setWeek4Day4();
        
        Entry<List<Item>,Integer> cycle7Sched = getBestScheduleForCycle(6,null);
        Set<Item> reservedItems = new HashSet<Item>(cycle7Sched.getKey());
        Entry<List<Item>,Integer> cycle6Sched = getBestScheduleForCycle(5,reservedItems);
        reservedItems.addAll(cycle6Sched.getKey());
        Entry<List<Item>,Integer> cycle5Sched = getBestScheduleForCycle(4,reservedItems);
        
        if(!rested)
        {
            if(cycle5Sched.getValue() <= cycle6Sched.getValue() && cycle5Sched.getValue() <= cycle7Sched.getValue())
            {
                //Double check to make sure that 5 wouldn't be better if it weren't for 6
                cycle5Sched = getBestScheduleForCycle(4,new HashSet<Item>(cycle7Sched.getKey()));
                if(cycle5Sched.getValue() <= cycle6Sched.getValue())
                {
                    System.out.println("Rest day 5");
                    addDay(cycle6Sched.getKey(), 5);
                    addDay(cycle7Sched.getKey(), 6);
                }
                else
                {
                    //Cycle 5 lives again!!!
                    addDay(cycle5Sched.getKey(), 4);
                    System.out.println("Rest day 6");
                    addDay(cycle7Sched.getKey(), 6);
                }
            }
            else if(cycle6Sched.getValue() <= cycle5Sched.getValue() && cycle6Sched.getValue() <= cycle7Sched.getValue())
            {
                //Redo cycle 5 to reserve only cycle 7 items
                cycle5Sched = getBestScheduleForCycle(4,new HashSet<Item>(cycle7Sched.getKey()));
                addDay(cycle5Sched.getKey(), 4);
                System.out.println("Rest day 6");
                addDay(cycle7Sched.getKey(), 6);
            }
            else
            {
                //Redo cycles 5 and 6 to give them free-ish reign
                cycle6Sched = getBestScheduleForCycle(5, new HashSet<Item>());
                cycle5Sched = getBestScheduleForCycle(4,new HashSet<Item>(cycle6Sched.getKey()));
                addDay(cycle5Sched.getKey(), 4);
                addDay(cycle6Sched.getKey(), 5);
                System.out.println("Rest day 7");
            }
        }
        else
        {
            addDay(cycle5Sched.getKey(), 4);
            addDay(cycle6Sched.getKey(), 5);
            addDay(cycle7Sched.getKey(), 6);
        }

        
          //addDay(Arrays.asList(Butter,TomatoRelish,Jam,TomatoRelish,Jam), 1);
          //addDay(Arrays.asList(CulinaryKnife,GardenScythe,SilverEarCuffs,GardenScythe), 2); 
          //addDay(Arrays.asList(BoiledEgg, ParsnipSalad, OnionSoup, ParsnipSalad, OnionSoup),3);
          //addDay(Arrays.asList(SheepfluffRug, Hora, SheepfluffRug, Hora),4);
         //addDay(Arrays.asList(Crook,SpruceRoundShield,Crook),5);
          //addDay(Arrays.asList(BoiledEgg, ScaleFingers, BoiledEgg, ScaleFingers),6);
         

        System.out.println("Week total: " + totalGross + " (" + totalNet + ")");

    }
    
    private static boolean setOrRestEarlyWeek(boolean rested, int day)
    {
        Entry<List<Item>,Integer> sched = getBestScheduleForCycle(day,null);
        if(!rested && (sched == null || isWorseThanAllFollowing(sched.getValue(), day)))
        {
            System.out.println("Rest day "+(day + 1));
            return true;
        }
        else
            addDay(sched.getKey(), day);
        
        return false;
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
            sortedItems = itemsByValue.entrySet().stream().filter(entry -> entry.getKey().peaksOnOrBeforeDay(day))
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
        }
        else
        {
            sortedItems = itemsByValue.entrySet().stream().filter(entry -> !reservedForLater.contains(entry.getKey().item))
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
        }
        
        Iterator<Entry<ItemInfo, Integer>> topItemIt = sortedItems.entrySet().iterator();
        
        CycleSchedule sch = new CycleSchedule(day, 0);
        
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
                        safeSchedules.put(items, sch.getValue());
                        if(verboseSolverLogging)
                        System.out.println("Compatible 4-hour craft found: "+fourHour.item+" adding safe 4-8-4-8 schedule with a value of "+safeSchedules.get(items));
                    }
                        
                    //Only need to check top 8 hour because we make it once
                    if(eightHour == null && (possibleMatch.time == 8 && possibleMatch.getsEfficiencyBonus(topItem.getKey())))
                    {
                        eightHour = possibleMatch;
                        List<Item> items = Arrays.asList(topItem.getKey().item, eightHour.item, topItem.getKey().item);
                        sch.setForAllWorkshops(items);
                        safeSchedules.put(items, sch.getValue());
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
                            safeSchedules.put(items, sch.getValue());
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
                        safeSchedules.put(items, sch.getValue());
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
                            safeSchedules.put(items44, sch.getValue());
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
                        safeSchedules.put(items, sch.getValue());
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
                    safeSchedules.put(items, sch.getValue());
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
        
        return sortedSchedules.entrySet().iterator().next();
    }
    
    
    private static void setWeek4Initial()
    {
        items[0].setInitialData(High,Cycle7Weak,Sufficient,None);
        items[1].setInitialData(VeryHigh,Cycle6Strong,Sufficient,None);
        items[2].setInitialData(VeryHigh,Cycle5Strong,Sufficient,None);
        items[3].setInitialData(Average,Cycle4Strong,Sufficient,None);
        items[4].setInitialData(Low,Cycle7Strong,Sufficient,None);
        items[5].setInitialData(Average,Cycle7Weak,Insufficient,Decreasing);
        items[6].setInitialData(VeryHigh,Cycle4Strong,Sufficient,None);
        items[7].setInitialData(VeryHigh,Cycle4Weak,Insufficient,Skyrocketing);
        items[8].setInitialData(Average,Cycle3Strong,Sufficient,None);
        items[9].setInitialData(Low,Cycle6Weak,Sufficient,None);
        items[10].setInitialData(Average,Cycle6Strong,Sufficient,None);
        items[11].setInitialData(Average,Cycle2Weak,Sufficient,None);
        items[12].setInitialData(Low,Cycle6Strong,Sufficient,None);
        items[13].setInitialData(High,Cycle5Weak,Sufficient,None);
        items[14].setInitialData(High,Cycle5Weak,Sufficient,None);
        items[15].setInitialData(High,Cycle3Weak,Sufficient,None);
        items[16].setInitialData(High,Cycle7Weak,Insufficient,Decreasing);
        items[17].setInitialData(High,Cycle7Weak,Insufficient,Decreasing);
        items[18].setInitialData(VeryHigh,Cycle5Strong,Sufficient,None);
        items[19].setInitialData(Average,Cycle3Strong,Sufficient,None);
        items[20].setInitialData(Low,Cycle7Weak,Sufficient,None);
        items[21].setInitialData(Low,Cycle6Weak,Sufficient,None);
        items[22].setInitialData(Average,Cycle4Weak,Sufficient,None);
        items[23].setInitialData(High,Cycle5Weak,Sufficient,None);
        items[24].setInitialData(VeryHigh,Cycle6Strong,Sufficient,None);
        items[25].setInitialData(VeryHigh,Cycle3Weak,Sufficient,None);
        items[26].setInitialData(Average,Cycle3Weak,Sufficient,None);
        items[27].setInitialData(High,Cycle5Strong,Sufficient,None);
        items[28].setInitialData(Average,Cycle7Strong,Sufficient,None);
        items[29].setInitialData(Average,Cycle7Strong,Sufficient,None);
        items[30].setInitialData(Low,Cycle3Weak,Sufficient,None);
        items[31].setInitialData(High,Cycle7Strong,Insufficient,Plummeting);
        items[32].setInitialData(VeryHigh,Cycle2Strong,Sufficient,None);
        items[33].setInitialData(VeryHigh,Cycle2Strong,Sufficient,None);
        items[34].setInitialData(High,Cycle2Strong,Insufficient,Skyrocketing);
        items[35].setInitialData(Average,Cycle5Strong,Insufficient,Skyrocketing);
        items[36].setInitialData(Average,Cycle3Strong,Sufficient,None);
        items[37].setInitialData(Average,Cycle4Weak,Sufficient,None);
        items[38].setInitialData(High,Cycle6Weak,Insufficient,Skyrocketing);
        items[39].setInitialData(High,Cycle5Weak,Sufficient,None);
        items[40].setInitialData(VeryHigh,Cycle7Strong,Sufficient,None);
        items[41].setInitialData(Average,Cycle6Weak,Sufficient,None);
        items[42].setInitialData(Low,Cycle4Weak,Sufficient,None);
        items[43].setInitialData(Average,Cycle2Weak,Sufficient,None);
        items[44].setInitialData(High,Cycle4Strong,Sufficient,None);
        items[45].setInitialData(High,Cycle2Strong,Sufficient,None);
        items[46].setInitialData(High,Cycle4Strong,Sufficient,None);
        items[47].setInitialData(Low,Cycle2Weak,Sufficient,None);
        items[48].setInitialData(VeryHigh,Cycle2Weak,Sufficient,None);
        items[49].setInitialData(VeryHigh,Cycle3Strong,Sufficient,None);
    }
    
    private static void setWeek4Day2()
    {
        items[0].addObservedDay(Sufficient, None);
        items[1].addObservedDay(Insufficient, None);
        items[2].addObservedDay(Insufficient, None);
        items[3].addObservedDay(Insufficient, None);
        items[4].addObservedDay(Insufficient, None);
        items[5].addObservedDay(Insufficient, Increasing);
        items[6].addObservedDay(Insufficient, None);
        items[7].addObservedDay(Nonexistent, Skyrocketing);
        items[8].addObservedDay(Insufficient, None);
        items[9].addObservedDay(Sufficient, None);
        items[10].addObservedDay(Sufficient, None);
        items[11].addObservedDay(Insufficient, Skyrocketing);
        items[12].addObservedDay(Sufficient, None);
        items[13].addObservedDay(Insufficient, Increasing);
        items[14].addObservedDay(Sufficient, None);
        items[15].addObservedDay(Insufficient, None);
        items[16].addObservedDay(Insufficient, Increasing);
        items[17].addObservedDay(Insufficient, Increasing);
        items[18].addObservedDay(Insufficient, None);
        items[19].addObservedDay(Sufficient, None);
        items[20].addObservedDay(Insufficient, None);
        items[21].addObservedDay(Insufficient, None);
        items[22].addObservedDay(Insufficient, Increasing);
        items[23].addObservedDay(Insufficient, Increasing);
        items[24].addObservedDay(Insufficient, Increasing);
        items[25].addObservedDay(Insufficient, Skyrocketing);
        items[26].addObservedDay(Insufficient, None);
        items[27].addObservedDay(Insufficient, None);
        items[28].addObservedDay(Sufficient, None);
        items[29].addObservedDay(Sufficient, None);
        items[30].addObservedDay(Insufficient, None);
        items[31].addObservedDay(Insufficient, Increasing);
        items[32].addObservedDay(Insufficient, None);
        items[33].addObservedDay(Insufficient, None);
        items[34].addObservedDay(Nonexistent, Skyrocketing);
        items[35].addObservedDay(Nonexistent, Skyrocketing);
        items[36].addObservedDay(Sufficient, None);
        items[37].addObservedDay(Sufficient, None);
        items[38].addObservedDay(Nonexistent, Skyrocketing);
        items[39].addObservedDay(Sufficient, None);
        items[40].addObservedDay(Sufficient, None);
        items[41].addObservedDay(Insufficient, Skyrocketing);
        items[42].addObservedDay(Sufficient, None);
        items[43].addObservedDay(Sufficient, None);
        items[44].addObservedDay(Sufficient, None);
        items[45].addObservedDay(Insufficient, None);
        items[46].addObservedDay(Insufficient, Skyrocketing);
        items[47].addObservedDay(Sufficient, None);
        items[48].addObservedDay(Insufficient, None);
        items[49].addObservedDay(Insufficient, None);
    }
    
    private static void setWeek4Day3()
    {
        items[0].addObservedDay(Insufficient, Increasing);
        items[1].addObservedDay(Sufficient, Decreasing);
        items[2].addObservedDay(Sufficient, Plummeting);
        items[3].addObservedDay(Sufficient, Decreasing);
        items[4].addObservedDay(Sufficient, Plummeting);
        items[5].addObservedDay(Sufficient, Plummeting);
        items[6].addObservedDay(Sufficient, Plummeting);
        items[7].addObservedDay(Sufficient, Plummeting);
        items[8].addObservedDay(Sufficient, Plummeting);
        items[9].addObservedDay(Sufficient, None);
        items[10].addObservedDay(Insufficient, Increasing);
        items[11].addObservedDay(Nonexistent, Skyrocketing);
        items[12].addObservedDay(Insufficient, Skyrocketing);
        items[13].addObservedDay(Insufficient, Increasing);
        items[14].addObservedDay(Sufficient, None);
        items[15].addObservedDay(Sufficient, Plummeting);
        items[16].addObservedDay(Sufficient, Plummeting);
        items[17].addObservedDay(Sufficient, Plummeting);
        items[18].addObservedDay(Sufficient, Plummeting);
        items[19].addObservedDay(Insufficient, Increasing);
        items[20].addObservedDay(Sufficient, Plummeting);
        items[21].addObservedDay(Sufficient, Plummeting);
        items[22].addObservedDay(Insufficient, Increasing);
        items[23].addObservedDay(Insufficient, Increasing);
        items[24].addObservedDay(Insufficient, Increasing);
        items[25].addObservedDay(Nonexistent, Skyrocketing);
        items[26].addObservedDay(Sufficient, Plummeting);
        items[27].addObservedDay(Sufficient, Decreasing);
        items[28].addObservedDay(Sufficient, None);
        items[29].addObservedDay(Insufficient, Increasing);
        items[30].addObservedDay(Sufficient, Plummeting);
        items[31].addObservedDay(Sufficient, Plummeting);
        items[32].addObservedDay(Sufficient, Plummeting);
        items[33].addObservedDay(Sufficient, Plummeting);
        items[34].addObservedDay(Surplus, Plummeting);
        items[35].addObservedDay(Surplus, Plummeting);
        items[36].addObservedDay(Insufficient, Skyrocketing);
        items[37].addObservedDay(Sufficient, None);
        items[38].addObservedDay(Sufficient, Plummeting);
        items[39].addObservedDay(Sufficient, None);
        items[40].addObservedDay(Insufficient, Skyrocketing);
        items[41].addObservedDay(Nonexistent, Skyrocketing);
        items[42].addObservedDay(Sufficient, None);
        items[43].addObservedDay(Sufficient, None);
        items[44].addObservedDay(Sufficient, None);
        items[45].addObservedDay(Sufficient, Decreasing);
        items[46].addObservedDay(Nonexistent, Skyrocketing);
        items[47].addObservedDay(Insufficient, Skyrocketing);
        items[48].addObservedDay(Sufficient, Plummeting);
        items[49].addObservedDay(Sufficient, Plummeting);
    }
    
    private static void setWeek4Day4()
    {
        items[0].addObservedDay(Insufficient, Increasing);
        items[1].addObservedDay(Sufficient, Increasing);
        items[2].addObservedDay(Sufficient, Increasing);
        items[3].addObservedDay(Sufficient, Increasing);
        items[4].addObservedDay(Sufficient, None);
        items[5].addObservedDay(Sufficient, None);
        items[6].addObservedDay(Sufficient, None);
        items[7].addObservedDay(Sufficient, None);
        items[8].addObservedDay(Sufficient, Increasing);
        items[9].addObservedDay(Insufficient, Increasing);
        items[10].addObservedDay(Insufficient, Increasing);
        items[11].addObservedDay(Sufficient, Plummeting);
        items[12].addObservedDay(Nonexistent, Skyrocketing);
        items[13].addObservedDay(Sufficient, Plummeting);
        items[14].addObservedDay(Insufficient, Skyrocketing);
        items[15].addObservedDay(Sufficient, Skyrocketing);
        items[16].addObservedDay(Sufficient, None);
        items[17].addObservedDay(Sufficient, None);
        items[18].addObservedDay(Sufficient, Skyrocketing);
        items[19].addObservedDay(Insufficient, Increasing);
        items[20].addObservedDay(Sufficient, None);
        items[21].addObservedDay(Sufficient, Skyrocketing);
        items[22].addObservedDay(Sufficient, Plummeting);
        items[23].addObservedDay(Surplus, Plummeting);
        items[24].addObservedDay(Sufficient, Plummeting);
        items[25].addObservedDay(Sufficient, Plummeting);
        items[26].addObservedDay(Sufficient, Skyrocketing);
        items[27].addObservedDay(Sufficient, Increasing);
        items[28].addObservedDay(Insufficient, Skyrocketing);
        items[29].addObservedDay(Insufficient, Increasing);
        items[30].addObservedDay(Sufficient, Increasing);
        items[31].addObservedDay(Sufficient, None);
        items[32].addObservedDay(Sufficient, None);
        items[33].addObservedDay(Sufficient, Increasing);
        items[34].addObservedDay(Surplus, None);
        items[35].addObservedDay(Surplus, None);
        items[36].addObservedDay(Nonexistent, Skyrocketing);
        items[37].addObservedDay(Insufficient, Increasing);
        items[38].addObservedDay(Sufficient, None);
        items[39].addObservedDay(Insufficient, Increasing);
        items[40].addObservedDay(Nonexistent, Skyrocketing);
        items[41].addObservedDay(Sufficient, Plummeting);
        items[42].addObservedDay(Insufficient, Skyrocketing);
        items[43].addObservedDay(Insufficient, Skyrocketing);
        items[44].addObservedDay(Insufficient, Increasing);
        items[45].addObservedDay(Sufficient, Increasing);
        items[46].addObservedDay(Surplus, Plummeting);
        items[47].addObservedDay(Nonexistent, Skyrocketing);
        items[48].addObservedDay(Sufficient, None);
        items[49].addObservedDay(Sufficient, Increasing);
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
