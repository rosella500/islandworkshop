package islandworkshop;

import static islandworkshop.ItemCategory.*;
import static islandworkshop.RareMaterial.*;
import static islandworkshop.PeakCycle.*;
import static islandworkshop.Item.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Solver
{
    static int WORKSHOP_BONUS = 130;
    final static int GROOVE_MAX = 45;

    static int NUM_WORKSHOPS = 4;
    final static int helperPenalty = 5;
    static int averageDayValue = 1123 * WORKSHOP_BONUS * NUM_WORKSHOPS / 100;
    
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
            new ItemInfo(Crook,Arms,Woodworks,120,8,9,Map.of(Fang,4)),
            new ItemInfo(CoralSword,Arms,MarineMerchandise,72,8,10,null),
            new ItemInfo(CoconutJuice,Confections,Concoctions,36,4,10,null),
            new ItemInfo(Honey,Confections,Ingredients,36,4,10,null),
            new ItemInfo(SeashineOpal,UnburiedTreasures,Invalid,80,8,10,null),
            new ItemInfo(DriedFlowers,Sundries,Furnishings,54,6,10,null),
            new ItemInfo(PowderedPaprika,Ingredients,Concoctions,52,4,11,Map.of(Paprika,2)),
            new ItemInfo(CawlCennin,Concoctions,CreatureCreations,90,6,11,Map.of(Leek,3,Milk,1)),
            new ItemInfo(Isloaf,Foodstuffs,Concoctions,52,4,11,Map.of(Wheat,2)),
            new ItemInfo(PopotoSalad,Foodstuffs,Invalid,52,4,11,Map.of(Popoto,2)),
            new ItemInfo(Dressing,Ingredients,Invalid,52,4,11,Map.of(Onion,2)),
            new ItemInfo(Stove, Furnishings, Metalworks, 54, 6, 12, null),
            new ItemInfo(Lantern, Sundries, Invalid, 80, 8, 12, null),
            new ItemInfo(Natron, Sundries, Concoctions, 36, 4,12,null),
            new ItemInfo(Bouillabaisse, Foodstuffs, MarineMerchandise, 136, 8,12,Map.of(CaveShrimp, 2, Tomato, 2)),
            new ItemInfo(FossilDisplay, CreatureCreations, UnburiedTreasures, 54,6,13,null),
            new ItemInfo(Bathtub, Furnishings, UnburiedTreasures, 72, 8,13,null),
            new ItemInfo(Spectacles, Attire, Sundries, 54, 6,13,null),
            new ItemInfo(CoolingGlass, UnburiedTreasures, Invalid, 80, 8,13,null),
            new ItemInfo(RunnerBeanSaute, Foodstuffs, Invalid, 52, 4, 14,Map.of(RunnerBean, 2)),
            new ItemInfo(BeetSoup, Foodstuffs, Invalid, 78, 6, 14,Map.of(Beet, 3, Popoto, 1, Milk, 1)),
            new ItemInfo(ImamBayildi, Foodstuffs, Invalid, 90, 6, 14,Map.of(Eggplant, 2, Onion, 2, Tomato, 2)),
            new ItemInfo(PickledZucchini, PreservedFood, Invalid, 104, 8, 14,Map.of(Zucchini, 4)),
    };
    
    private static int groove = 0;
    private static int totalGross = 0;
    private static int totalNet = 0;

    private static int totalGrooveless = 0;
    public static boolean verboseCalculatorLogging = false;
    public static boolean verboseSolverLogging = false;
    public static boolean verboseReservations = false;
    private static int alternatives = 0;
    public static boolean rested = false;
    private static int islandRank = 15;
    public static double materialWeight = 0.5;
    public static boolean bestD5IsWorst = false;
    public static int bestD5 = 0;
    public static Set<Item> reservedItems = new HashSet<>();
    public static Map<Item, ReservedHelper> reservedHelpers = new HashMap<>();
    public static CSVExporter csvexp;
    public static WeekSchedule weekSchedule = new WeekSchedule();
    private static boolean valuePerHour = true;
    private static int itemsToReserve = 15;

    public static Map<Integer, List<List<Item>>> schedulesToCheck;

    private static Map<Integer, TempSchedule> scheduledDays = new HashMap<>();

    private static Map<RareMaterial, Integer> matsUsed = new TreeMap<>();
    private static boolean logMats = false;
    private static boolean writeCraftsToCSV = false;

    public static void main(String[] args)
    {
        CSVImporter.initBruteForceChains();

        if(writeCraftsToCSV)
            csvexp = new CSVExporter("output.csv");

        //Things that need to be regenerated in 6.3
        //CSVImporter.generateBruteForceChains();

        /*schedulesToCheck = new HashMap<>();
        List<List<Item>> schedules = new ArrayList<>();
        schedules.add(Arrays.asList(Butter, Item.Horn, Butter, Item.Horn, Butter));
        schedules.add(Arrays.asList(TomatoRelish, Butter, Item.Horn, Butter, Item.Horn));
        schedulesToCheck.put(5, schedules);

        verboseReservations = true;*/
       /* List<Integer> list = Arrays.asList(0, 1, 2, 3, 4);
        List<List<Integer>> result = new ArrayList<>();
        iteratePermutations(list, result::add);
        List<Integer> bestOrder = null;
        int bestAverage = -1;
        for(int orderIndex = 0; orderIndex < result.size(); orderIndex++)
        {

            var order = result.get(orderIndex);
            System.out.println("Order: "+order);*/
        int totalCowries = 0;
        int totalTotalNet = 0;
        totalGrooveless = 0;
        int startWeek = 39;
        int endWeek = 39;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        var hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour < 3)
            hour += 24;
        hour = (hour - 3) % 24;
        int hoursLeft = 24 - (((hour / 2) + 1) * 2);
        //System.out.println("Hours left in schedule: "+hoursLeft);

        int lowestWeek = 99999;
        int highestWeek = 0;

            for(int week = startWeek; week <= endWeek; week++)
            {
                weekSchedule.clear();
                if(week<=20)
                {
                    WORKSHOP_BONUS = 120;
                    islandRank = 9;
                    NUM_WORKSHOPS = 3;
                }
                else if(week <= 39)
                {
                    WORKSHOP_BONUS = 120;
                    islandRank=11;
                    NUM_WORKSHOPS = 3;
                }
                else
                {
                    WORKSHOP_BONUS = 130;
                    islandRank = 15;
                    NUM_WORKSHOPS = 4;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d");

                calendar.setTime(new Date(1661241600000L));
                calendar.add(Calendar.DAY_OF_YEAR, (week-1)*7);
                var month = calendar.get(Calendar.MONTH);
                String dateStr = sdf.format(calendar.getTime());

                calendar.add(Calendar.DAY_OF_YEAR, 6);
                if(calendar.get(Calendar.MONTH) == month)
                    sdf = new SimpleDateFormat("d");



                dateStr += " - " + sdf.format(calendar.getTime());


                System.out.println("Season "+week+" ("+dateStr+"):");
                groove = 0;
                totalGross = 0;
                totalNet = 0;
                rested = false;
                bestD5IsWorst = false;
                bestD5 = 0;
                for(ItemInfo item : items)
                {
                    item.craftedPerDay = new int[7];
                    item.peak = Unknown;
                }
                reservedItems.clear();
                reservedHelpers.clear();
                scheduledDays.clear();


                CSVImporter.initSupplyData(week);
                setInitialFromCSV();

                //solveRecsWithNoSupply();
                solveRecsForWeek();
                //bruteForceWeek();
                //solveCrimeTime(week);


                /*var schedule = getBestBruteForceSchedule(2, 18, null, 3, null, 16);
                System.out.println("Best C3 schedule: "+schedule.getKey().getItems()+" ("+schedule.getValue()+")");*/
                if(totalGross > highestWeek)
                    highestWeek = totalGross;
                if(totalGross < lowestWeek)
                    lowestWeek = totalGross;

                totalCowries += totalGross;
                totalTotalNet += totalNet;

                if(writeCraftsToCSV)
                {
                    weekSchedule.setMetaData(week,totalGross,totalNet);
                    csvexp.printCSVweek(weekSchedule);
                }

            }
            int averageGross = (totalCowries/(endWeek-startWeek+1));
            System.out.println("Average cowries/week: "+averageGross+" Average net: "+(totalTotalNet/(endWeek-startWeek+1))+" Lowest week: "+lowestWeek+" Highest week: "+highestWeek);

            System.out.println("Average true grooveless: "+(totalGrooveless/((endWeek-startWeek+1)*5)));

            if(writeCraftsToCSV)
                csvexp.close();
            if(logMats)
            {
                for(var mat : RareMaterial.values())
                {
                    System.out.println("Total "+mat+" used: "+matsUsed.getOrDefault(mat,0)+" Per week: "+matsUsed.getOrDefault(mat,0)/((double)endWeek-startWeek+1));
                }
            }


        /*items[Brush.ordinal()].setInitialData(Popularity.VeryHigh, Cycle7Strong);
        items[GardenScythe.ordinal()].setInitialData(Popularity.VeryHigh, Cycle7Strong);
        items[SilverEarCuffs.ordinal()].setInitialData(Popularity.VeryHigh, Cycle7Strong);
        groove = 35;
        setDay(Arrays.asList(Brush, GardenScythe, SilverEarCuffs, GardenScythe), 6);*/



            /*if(averageGross > bestAverage) {
                bestAverage = averageGross;
                bestOrder = order;
            }
        }

        System.out.println("Best average: "+bestAverage+", so best order: "+bestOrder);*/


    }

    /*public static void solveRestOfWeek(int currentDay)
    {
        int worstIndex = -1;
        int worstValue = 99999;
        int estGroove = (groove + GROOVE_MAX) / 2;
        setObservedFromCSV(Math.min(currentDay, 3));
        Map<Item,Integer> reservedSet = new HashMap<>();
        List<List<Item>> restOfWeek = new ArrayList<>();

        for (int d = currentDay + 2; d < 7; d++)
        {
            var solution = getBestSchedule(d, estGroove, reservedSet, d);
            if(solution.getValue() < worstValue)
            {
                worstValue = solution.getValue();
                worstIndex = restOfWeek.size();
            }

            restOfWeek.add(solution.getKey().getItems());
            reservedSet = solution.getLimitedUses(reservedSet);
            System.out.println("day "+(d+1)+" schedule: "+solution.getKey().getItems()+" ("+solution.getValue()+")");
        }

        if(!rested)
        {
            //If we haven't rested, rest the worst day
            restOfWeek.remove(worstIndex);
            restOfWeek.add(worstIndex, new ArrayList<>());
        }


        if(restOfWeek.size() == 5) //If we're at day 1, we have no real idea, so put our best guess at C6, the second-best day to craft
        {
            //Swap C3 and C6
            var best = restOfWeek.get(0);
            var c6 = restOfWeek.get(3);
            restOfWeek.set(0, c6);
            restOfWeek.set(3, best);
        }

        setObservedFromCSV(3);

        for(int i=0; i<restOfWeek.size();i++)
        {
            setDay(restOfWeek.get(i), i+currentDay+2, groove, true);
        }

    }*/

    /*private static void solveRecsWithNoSupply()
    {
        for(ItemInfo item : items)
        {
            item.peak = Unknown;
        }
        verboseSolverLogging = true;

        long time = System.currentTimeMillis();
        Map<Item,Integer> reservedSet = new HashMap<>();
        List<List<Item>> scheduleList = new ArrayList<>();

        for (int d = 0; d < 5; d++)
        {
            Entry<WorkshopSchedule, Integer> solution = getBestSchedule(2, 15, reservedSet, 2);
            scheduleList.add(solution.getKey().getItems());

            if (verboseSolverLogging)
                System.out.println("Schedule " + (d+1) + ", crafts: "
                        + Arrays.toString(solution.getKey().getItems().toArray())
                        + " value: " + solution.getValue());

            reservedSet = solution.getKey().getLimitedUses(reservedSet);
        }
        if(verboseSolverLogging)
            System.out.println();

        setObservedFromCSV(3);

        //Committing to the bit
        setDay(scheduleList.get(1), 1, 0, verboseSolverLogging);
        setDay(scheduleList.get(4), 2, groove, verboseSolverLogging);
        setDay(scheduleList.get(2), 3, groove, verboseSolverLogging);
        setDay(scheduleList.get(3), 4, groove, verboseSolverLogging);
        setDay(scheduleList.get(0), 5, groove, verboseSolverLogging);

        System.out.println("Season total: " + totalGross + " (" + totalNet + ")\n" + "Took "+ (System.currentTimeMillis() - time) + "ms.\n");
    }*/

    private static void solveRecsForWeek()
    {
        long time = System.currentTimeMillis();


        CycleSchedule d2 = getBestSchedule(1, groove);
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
            recalculateTodayAndSet(1, d2);
        }
        verboseSolverLogging = false;
        if(hasNextDay)
        {
            //alternatives = 10;
            var d3 = getBestSchedule(2, groove);
            alternatives = 0;
            if(!rested && isWorseThanAllFollowing(d3,2)) {
                restDay(d3, 2);
                hasNextDay = setObservedFromCSV(2);
            }
            else
            {
                hasNextDay = setObservedFromCSV(2);
                //setDay(d3.getKey().getItems(), 2);
                recalculateTodayAndSet(2, d3);
            }
            if(hasNextDay)
            {
                var d4 = getBestSchedule(3, groove);
                int justC4Value = d4.getValue();


                //verboseSolverLogging = true;
                boolean worst = isWorseThanAllFollowing(d4, 3);

                //verboseSolverLogging = false;
                //System.out.println("Rested? "+rested+". Resting D5? " +guaranteeRestD5);
                if(!rested && bestD5IsWorst)
                {
                    //System.out.println("C5 is the worst future day, so seeing if C4+C5 is better than C4 alone");
                    var d4Again = getBestSchedule(3, groove, null, 4);
                    if(d4Again.getValue() > bestD5)
                    {
                        //System.out.println("It is! Using C4 schedule "+d4Again);
                        hasNextDay = setObservedFromCSV(3);
                        recalculateTodayAndSet(3, d4Again);
                    }
                    else
                    {
                        //System.out.println("It isn't!");
                        restDay(d4, 3);
                        hasNextDay = setObservedFromCSV(3);
                    }

                }
                else if(!rested && worst)
                {
                    restDay(d4, 3);
                    hasNextDay = setObservedFromCSV(3);
                }
                else //We either rested already or we aren't the worst so add it
                {
                    hasNextDay = setObservedFromCSV(3);
                    recalculateTodayAndSet(3, d4);
                }

                if(hasNextDay)
                {
                    setLateDays();
                }
            }
        }

        /*if(!hasNextDay)
            solveRestOfWeek(scheduledDays.size()-1 + (rested? 1 : 0));*/

        System.out.println("Total: " + totalGross + " (" + totalNet + ")\n" + "Took " + (System.currentTimeMillis() - time) + "ms.\n");
        //setDay(Arrays.asList(Rope,SharkOil,CulinaryKnife,SharkOil), 4);
    }

    private static void populateReservedItems(int day)
    {
        int resFullWeek = 16;
        int res45=6;
        int res67=8;
        int resSingle=4;

        reservedItems.clear();
        Map<ItemInfo, Integer> itemValues = new HashMap<>();
        for (ItemInfo item : items)
        {
            if (item.peaksOnOrBeforeDay(day))
                continue;
            int value = item.getValueWithSupply(Supply.Sufficient);
            value = value * 8 / item.time;
            itemValues.put(item, value);
        }
        LinkedHashMap<ItemInfo, Integer> bestItems = itemValues
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));
        var bestItemsEntries = bestItems.entrySet();
        List<Item> itemsThatGetReservations = new ArrayList<>();
        int currFullWeek = 0;
        int curr45 = 0;
        int curr67 = 0;
        int curr5 = 0;
        int curr6 = 0;
        int curr7 = 0;
        int current = 0;
        int cap = 0;
        for(var next : bestItemsEntries)
        {
            if(day==1 && next.getKey().peaksOnDay(day+1))
            {
                currFullWeek++;
                current = currFullWeek;
                cap = resFullWeek;
            }
            else if(day==2)
            {
                if(next.getKey().peaksOnDay(3))
                {
                    curr45++;
                    current = curr45;
                    cap = res45;
                }
                else if(next.getKey().peaksOnDay(5))
                {
                    curr67++;
                    current = curr67;
                    cap = res67;
                }
                else
                    cap=-1;
            }
            else if(day==3)
            {
                if(next.getKey().peaksOnDay(4))
                {
                    curr5++;
                    current = curr5;
                    cap = resSingle;
                }
                else if(next.getKey().peaksOnDay(5))
                {
                    curr67++;
                    current = curr67;
                    cap = res67;
                }
                else
                    cap = -1;
            }
            else if(day==4)
            {
                if(next.getKey().peaksOnDay(5))
                {
                    curr6++;
                    current = curr6;
                    cap = resSingle;
                }
                else if(next.getKey().peaksOnDay(6))
                {
                    curr7++;
                    current = curr7;
                    cap = resSingle;
                }
                else
                    cap = -1;
            }

            if(current <= cap)
            {
                //System.out.printf("Reserving item %s (%d)\n", next.getKey(), next.getValue());
                reservedItems.add(next.getKey().item);
            }
            if (current <= cap/2)
                itemsThatGetReservations.add(next.getKey().item);
        }

        reservedHelpers.clear();
        for (int i = 0; i < itemsThatGetReservations.size(); i++)
        {
            Item itemEnum = itemsThatGetReservations.get(i);
            ItemInfo mainItem = items[itemEnum.ordinal()];
            if (mainItem.time != 8)
                continue;
            int bestValue = 0;
            Item bestHelper = Macuahuitl; //This is the most useless thing I can think of
            int secondBest = 0;
            Item secondHelper = Macuahuitl;
            for (ItemInfo helper : items)
            {
                if (helper.time != 4 || !helper.getsEfficiencyBonus(mainItem))
                    continue;

                int value = helper.getValueWithSupply(Supply.Sufficient);
                if (value > bestValue)
                {
                    secondBest = bestValue;
                    secondHelper = bestHelper;
                    bestValue = value;
                    bestHelper = helper.item;
                }
                else if (value > secondBest)
                {
                    secondBest = value;
                    secondHelper = helper.item;
                }
            }
            int swap = bestValue - secondBest;
            int stepDown = bestValue - (int) (bestValue * .6);
            if (swap > 0)
            {
                int penalty = Math.min(swap, stepDown);
                int finalPenalty = penalty / Math.max(i, 1) + 1;
                finalPenalty=Math.max((int)(finalPenalty*.3), 1); //Nerfing this hard since it doesn't seem to help
                //System.out.println("Reserving helper " + bestHelper + " to go with main item " + itemEnum + " (#" + (i + 1) + "), difference between " + bestHelper + " and " + secondHelper + "? " + swap + " cost of stepping down? " + stepDown + " Penalty: " + finalPenalty);

                reservedHelpers.put(itemEnum, new ReservedHelper(bestHelper, finalPenalty));
            }
        }
    }

    private static void recalculateTodayAndSet(int day, CycleSchedule prevSchedule)
    {

        /*var newSchedule = getBestBruteForceSchedule(day, groove, null, day, prevSchedule.getItems().get(0), 24);
        int prevValue = prevSchedule.getValueWithGrooveEstimate(day, groove);

        if(!prevSchedule.getItems().equals(newSchedule.getKey().getItems()) && newSchedule.getValue() > prevValue)
        {
            System.out.println("Updated rec detected!");

            setDay(newSchedule.getKey().getItems(), day, groove, true);
        }
        else*/
            setDay(prevSchedule, day, true);
    }

    
    private static void restDay(CycleSchedule rec, int day)
    {
        printRestDayInfo(rec, day);
        rested = true;
    }

    private static void printRestDayInfo(CycleSchedule rec, int day)
    {
        rec.startingGroove = 0;
        if(!writeCraftsToCSV)
            System.out.println("Rest cycle " + (day + 1)+". Think we'll make more than " + rec.getValue() + " grooveless with " + rec + ". ");
    }

    private static void setLateDays()
    {
        int startingGroove = groove;
        CycleSchedule cycle5Sched = getBestSchedule(4, startingGroove, null, 6);
        CycleSchedule cycle6Sched = getBestSchedule(5, startingGroove, null, 6);
        CycleSchedule cycle7Sched = getBestSchedule(6, startingGroove);

        // I'm just hardcoding this, This could almost certainly be improved

        List<CycleSchedule> endOfWeekSchedules = new ArrayList<>();
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
            setDay(cycle5Sched, 4);
            startingGroove = groove;
            CycleSchedule recalced6Sched = getBestSchedule(5, startingGroove, null, 6);
            CycleSchedule recalcedCycle7Sched = getBestSchedule(6, startingGroove);
            if(!rested)
            {
                if(recalced6Sched.getValue() > recalcedCycle7Sched.getValue())
                {
                    setDay(recalced6Sched, 5);
                    printRestDayInfo(recalcedCycle7Sched, 6);
                }
                else
                {
                    printRestDayInfo(recalced6Sched, 5);
                    setDay(recalcedCycle7Sched, 6);
                }
            }
            else
            {

                setDay(recalced6Sched, 5, false); //Temporarily set it so we can get more accurate values for D7

                var onlyCycle6Sched = getBestSchedule(5, startingGroove);

                recalcedCycle7Sched = getBestSchedule(6, startingGroove);

                if(recalced6Sched.getValue() + recalcedCycle7Sched.getValue() < onlyCycle6Sched.getValue() + cycle7Sched.getValue())
                {
                    //Cycle 6 only won because it was cheating.
                    setDay(onlyCycle6Sched, 5, true);
                    cycle7Sched.startingGroove = groove;
                    setDay(cycle7Sched, 6);
                }
                else
                {
                    setDay(recalced6Sched, 5, true);
                    recalcedCycle7Sched.startingGroove = groove;
                    setDay(recalcedCycle7Sched, 6);
                }
            }

        }
        else if (bestDay == 6) // Day 7 is best
        {
            //System.out.println("Day 7 is best");
            Map<Item,Integer> reserved7Set = cycle7Sched.getLimitedUses(null);

            if(!rested)//We only care about one of 5 or 6
            {
                var recalcedCycle6Sched = getBestSchedule(5, startingGroove, reserved7Set, 6);
                var recalcedCycle5Sched = getBestSchedule(4, startingGroove, reserved7Set, 6);

                if(recalcedCycle5Sched.getValue() > recalcedCycle6Sched.getValue())
                {
                    setDay(recalcedCycle5Sched, 4, true);
                    printRestDayInfo(recalcedCycle6Sched, 5);
                }
                else
                {
                    printRestDayInfo(recalcedCycle5Sched, 4);
                    setDay(recalcedCycle6Sched, 5, true);
                }
            }
            else
            {
                var recalcedCycle6Sched = getBestSchedule(5, startingGroove + 9, reserved7Set, 6);
                //try deriving 5 from 6
                int total65 = 0;
                var reserved67Items = recalcedCycle6Sched.getLimitedUses(reserved7Set);
                var recalcedCycle5Sched = getBestSchedule(4, startingGroove, reserved67Items, 6);
                setDay(recalcedCycle5Sched, 4, false);
                recalcedCycle6Sched.startingGroove = groove;
                setDay(recalcedCycle6Sched, 5, false);
                total65 = totalGross + totalNet;
                /*System.out.println("Trying to prioritize day 6:"+Arrays.toString(recalcedCycle6Sched.getKey().getItems().toArray())
                        +" ("+recalcedCycle6Sched.getValue()+"), so day 5: "+Arrays.toString(recalcedCycle5Sched.getKey().getItems().toArray())
                        +" ("+recalcedCycle5Sched.getValue()+") total: "+total65);*/

                //Try deriving 6 from 5
                setDay(cycle5Sched, 4, false);
                var basedOn56Sched = getBestSchedule(5, groove, reserved7Set, 6);
                setDay(basedOn56Sched, 5, false);

                int total56 = totalGross + totalNet;
                /*System.out.println("Trying to prioritize day 5:"+Arrays.toString(cycle5Sched.getKey().getItems().toArray())
                        +" ("+cycle5Sched.getValue()+"), so day 6: "+Arrays.toString(basedOn56Sched.getKey().getItems().toArray())
                        +" ("+basedOn56Sched.getValue()+") total: "+total56);*/

                if(total65 > total56)
                {
                    //System.out.println("Basing on 6 is better");
                    setDay(recalcedCycle5Sched, 4, true);
                    setDay(recalcedCycle6Sched, 5);
                }
                else
                {
                    //System.out.println("Basing on 5 is better");
                    setDay(cycle5Sched, 4, true);
                    setDay(basedOn56Sched, 5, true);
                }
            }

            setDay(getBestSchedule(6, groove), 6);
        }
        else // Best day is Day 6
        {
            //System.out.println("Day 6 is best");
            setDay(cycle6Sched, 5, false); //Temporarily set it so we can get more accurate values for D7
            Map<Item,Integer> reserved6 = cycle6Sched.getLimitedUses(null);
            var recalcedCycle5Sched = getBestSchedule(4, startingGroove, reserved6, 5);
            var recalcedCycle7Sched = getBestSchedule(6, startingGroove);


            var onlyCycle6Sched = getBestSchedule(5, startingGroove);
            setDay(onlyCycle6Sched, 5, false);
            var onlyCycle7Sched = getBestSchedule(6, startingGroove);
            Map<Item,Integer> reservedOnly6 = onlyCycle6Sched.getLimitedUses(null);
            var onlyCycle5Sched = getBestSchedule(4, startingGroove, reservedOnly6, 5);

            if(!rested)
            {
                //We only care about either 5 or 7, not both
                int best56Combo = cycle6Sched.getValue() + recalcedCycle5Sched.getValue();
                int best67Combo = cycle6Sched.getValue() + recalcedCycle7Sched.getValue();
                int best76Combo = onlyCycle6Sched.getValue() + onlyCycle7Sched.getValue();

                int bestOverall = Math.max(best76Combo, Math.max(best67Combo, best56Combo));
                if(bestOverall == best56Combo)
                {
                    setDay(recalcedCycle5Sched, 4, true);
                    setDay(getBestSchedule(5, groove, null, 6), 5);
                    printRestDayInfo(recalcedCycle7Sched, 6);
                }
                else
                {
                    printRestDayInfo(recalcedCycle5Sched, 4);
                    if(bestOverall == best67Combo)
                    {
                        setDay(cycle6Sched, 5, true);
                        recalcedCycle7Sched.startingGroove = groove;
                        setDay(recalcedCycle7Sched, 6);
                    }
                    else
                    {
                        setDay(onlyCycle6Sched, 5, true);
                        onlyCycle7Sched.startingGroove = groove;
                        setDay(onlyCycle7Sched, 6);
                    }
                }
            }
            else
            {
                //We're using all 3 days
                if(cycle6Sched.getValue() + recalcedCycle5Sched.getValue() + recalcedCycle7Sched.getValue() > onlyCycle5Sched.getValue() + onlyCycle6Sched.getValue() + onlyCycle7Sched.getValue())
                {
                    //Using 6 first
                    setDay(recalcedCycle5Sched, 4, true);
                    cycle6Sched.startingGroove = groove;
                    setDay(cycle6Sched, 5);
                    recalcedCycle7Sched.startingGroove = groove;
                    setDay(recalcedCycle7Sched, 6);

                }
                else
                {
                    //6 takes too much from 7 so we just do it straight
                    setDay(onlyCycle5Sched, 4, true);
                    onlyCycle6Sched.startingGroove = groove;
                    setDay(onlyCycle6Sched, 5);
                    onlyCycle7Sched.startingGroove = groove;
                    setDay(onlyCycle7Sched, 6);
                }

            }
        }
    }
    private static boolean isWorseThanAllFollowing(CycleSchedule rec, int day)
    {
        int worstInFuture = 99999;
        bestD5IsWorst = true;
        bestD5 = 0;
        if(verboseSolverLogging) System.out.println("Comparing c" + (day + 1) + " (" + rec.getValue()+ ") to worst-case future days");
        
        Map<Item,Integer> reservedSet = new HashMap<>(); //rec.getKey().getLimitedUses();

        /*for(Item item : rec.getKey().getItems())
            reservedSet.put(item, 0);*/
        for (int d = day + 1; d < 7; d++)
        {
            CycleSchedule solution = getBestSchedule(d, groove, reservedSet, d);
            if(solution == null)
            {
                System.out.println("Null schedule attempting to get future schedule for cycle "+(d+1));
                continue;
            }

            int weightedValue = solution.getWeightedValue();
            if (day == 3 && d == 4) // We have a lot of info about this specific pair so
                                    // we might as well use it
            {
                bestD5 = getD5EV(solution);
                weightedValue = bestD5;

            }


            if (verboseSolverLogging)
                System.out.println("Cycle " + (d + 1) + ", crafts: " + Arrays.toString(solution.getItems().toArray())+
                        ", subcrafts: " + solution.getSubItems() + " value: " + weightedValue);
            worstInFuture = Math.min(worstInFuture, weightedValue);

            reservedSet = solution.getLimitedUses(reservedSet);
            
            if (bestD5 > 0 && d > 4 && weightedValue < bestD5) //If we're checking a later day and it's worse than our best D5
                bestD5IsWorst = false;
        }

        return rec.getWeightedValue() <= worstInFuture;
    }

    // Specifically for comparing D4 to D5
    public static int getD5EV(CycleSchedule solution)
    {
        if (verboseSolverLogging)
            System.out.println("Testing against C5 solution " + solution);

        List<ItemInfo> c5Peaks = new ArrayList<>();
        for (Item item : solution.getItems())
            if (items[item.ordinal()].peak == Cycle5 && !c5Peaks.contains(items[item.ordinal()]))
                c5Peaks.add(items[item.ordinal()]);
        for (Item item : solution.getSubItems())
            if (items[item.ordinal()].peak == Cycle5 && !c5Peaks.contains(items[item.ordinal()]))
                c5Peaks.add(items[item.ordinal()]);

        int sum = solution.getWeightedValue();
        int permutations = (int) Math.pow(2, c5Peaks.size());
        if (verboseSolverLogging)
            System.out.println("C5 peaks: " + c5Peaks.size() + ", permutations: " + permutations);

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

            int toAdd = solution.getWeightedValue();
            if (verboseSolverLogging)
                System.out.println("Permutation " + p + " has value " + toAdd);
            sum += toAdd;
        }

        if (verboseSolverLogging)
            System.out.println("Sum: " + sum + " average: " + sum / permutations);
        sum /= permutations;

        for (ItemInfo item : c5Peaks)
        {
            item.peak = Cycle5; // Set back to normal
        }

        return sum;
    }

    public static void setDay(CycleSchedule schedule, int day) {
    setDay(schedule, day, true);
    }
    public static void setDay(CycleSchedule schedule, int day, boolean real)
    {
        if(real)
        {
            if(writeCraftsToCSV)
                weekSchedule.setCycle(day-1,schedule.getItems(),schedule.getSubItems());
            else
                System.out.println("Cycle " + (day + 1) + ", crafts: " + Arrays.toString(schedule.getItems().toArray())+". Subcrafts: "+schedule.getSubItems());
        }

        if (scheduledDays.containsKey(day)) {
            //System.out.println("Removing old schedule for day "+(day+1));
            int prevGross = scheduledDays.get(day).getGrossValue();
            totalGross -= prevGross;
            //System.out.println("Subtracting "+prevGross+ " from total. Now "+totalGross+" Removed schedule for day "+(day+1));
            totalNet -= scheduledDays.get(day).getNetValue();
            for (var item : items)
                item.setCrafted(0, day);
            scheduledDays.remove(day);
        }
        int startingGroove = schedule.startingGroove;
        int groovelessValue = 0;

        if (real)
        {
            boolean oldVerbose = verboseCalculatorLogging;
            verboseCalculatorLogging = false;
            schedule.startingGroove = 0;
            groovelessValue = schedule.getValue();
            schedule.startingGroove = startingGroove;
            if(logMats)
                schedule.addMaterials(matsUsed);
            //verboseCalculatorLogging = true;
            totalGrooveless+=schedule.getTrueGroovelessValue();
            verboseCalculatorLogging = oldVerbose;

        }

        int gross = schedule.getValue();
        totalGross += gross;


        int net = gross - schedule.getMaterialCost();
        totalNet += net;
        Solver.groove = schedule.endingGroove;

        if(real && !writeCraftsToCSV)
            System.out.println("Cycle " + (day + 1) + " total, 0 groove: " + groovelessValue
                + ". Starting groove " + startingGroove + ": " + gross + ", net " + net
                + ".");
        /*else
            System.out.println("Adding "+gross+ " to total. Now "+totalGross+" Added schedule "+Arrays.toString(schedule.workshops[0].getItems().toArray())+" for day "+(day+1));*/

        schedule.numCrafted.forEach((k, v) ->
        {
            items[k.ordinal()].setCrafted(v, day);
        });
        scheduledDays.put(day,new TempSchedule(gross, net));
    }
    private static CycleSchedule getBestBruteForceSchedule(int day, int groove,
                                                                                  Map<Item,Integer> limitedUse, int allowUpToDay)
    {
        return getBestBruteForceSchedule(day, groove, limitedUse, allowUpToDay, null, 24);
    }
    private static CycleSchedule getBestBruteForceSchedule(int day, int groove,
            Map<Item,Integer> limitedUse, int allowUpToDay, Item startingItem, int hoursLeft) {

        var sortedSchedules = getBestSchedules(day, groove, limitedUse, allowUpToDay, startingItem, alternatives, false, hoursLeft);

        Iterator<Entry<WorkshopSchedule, Integer>> finalIterator = sortedSchedules
                .iterator();
        if(!finalIterator.hasNext())
            return null;
       CycleSchedule bestSchedule = sortedSchedules.getBestRec();

        if (alternatives > 1)
        {
            System.out.println("Best rec: "
                    + Arrays.toString(sortedSchedules.get(0).getKey().getItems().toArray()) + ": "
                    + sortedSchedules.get(0).getValue());
            for (int c = 1; c<sortedSchedules.size(); c++)
            {
                Entry<WorkshopSchedule, Integer> alt = sortedSchedules.get(c);
                System.out.println("Alternative rec: "
                        + Arrays.toString(alt.getKey().getItems().toArray()) + ": "
                        + alt.getValue());
            }
        }

        return bestSchedule;
    }

    private static Collection<List<Item>> getGeneratedSchedules(int day, int allowUpToDay, int islandRank, Map<Item,Integer> limitedUse)
    {
        Set<List<Item>> allEfficientChains = new HashSet<>();
        var fourHour = new ArrayList<ItemInfo>();
        var eightHour = new ArrayList<ItemInfo>();
        var sixHour = new ArrayList<ItemInfo>();

        int topEightsAllowed=5;
        int topSixesAllowed=4;
        int topFoursAllowed=3;

        int eightMatchesAllowed = 3;
        int sixMatchesAllowed = 4;
        int fourMatchesAllowed = 5;

        for (ItemInfo item : items)
        {
            List<ItemInfo> bucket = null;


            if (item.time == 4)
                bucket = fourHour;
            else if (item.time == 6)
                bucket = sixHour;
            else if (item.time == 8)
                bucket = eightHour;

            if(!item.peaksOnOrBeforeDay(allowUpToDay) || item.rankUnlocked > islandRank)
                bucket = null;
            else if(limitedUse != null && limitedUse.containsKey(item.item) && limitedUse.get(item.item) == 0)
                bucket = null;

            if(bucket != null)
                bucket.add(item);
        }
        Comparator<ItemInfo> compareByValue = Comparator.comparingInt(o -> -1 * o.getValueWithSupply(o.getSupplyBucketOnDay(day)));
        fourHour.sort(compareByValue);
        sixHour.sort(compareByValue);
        eightHour.sort(compareByValue);

        List<Item> four = new ArrayList<>();
        List<Item> eight = new ArrayList<>();
        //Find schedules based on 8-hour crafts
        for (int i=0; i<topEightsAllowed && i < eightHour.size(); i++)
        {
            var topItem = eightHour.get(i);
            List<ItemInfo> eightMatches = new ArrayList<>();
            //8-8-8

            for (ItemInfo eightMatchMatch : eightHour) {
                if (!eightMatchMatch.getsEfficiencyBonus(topItem))
                    continue;
                eightMatches.add(eightMatchMatch);
                allEfficientChains.add(List.of(topItem.item, eightMatchMatch.item, topItem.item));
            }

            //4-8-4-8 and 4-4-4-4-8
            int firstFourMatchCount = 0;
            for (ItemInfo firstFourMatch : fourHour)
            {
                if(firstFourMatchCount > fourMatchesAllowed)
                    break;
                if (!firstFourMatch.getsEfficiencyBonus(topItem))
                    continue;

                firstFourMatchCount++;
                //Add all efficient 4-8 pairs to parallel lists. We'll deal with them later
                four.add(firstFourMatch.item);
                eight.add(topItem.item);

                int secondFourMatchCount = 0;
                for (ItemInfo secondFourMatch : fourHour) {
                    if(secondFourMatchCount > fourMatchesAllowed)
                        break;
                    if (!secondFourMatch.getsEfficiencyBonus(firstFourMatch))
                        continue;

                    secondFourMatchCount++;

                    //4-4-8-8
                    for (var eightMatch : eightMatches)
                        allEfficientChains.add(List.of(secondFourMatch.item, firstFourMatch.item, topItem.item, eightMatch.item));

                    //4-4-4-4-8
                    int thirdFourMatchCount=0;
                    for (ItemInfo thirdFourMatch : fourHour) {
                        if(thirdFourMatchCount > fourMatchesAllowed)
                            break;
                        if (!secondFourMatch.getsEfficiencyBonus(thirdFourMatch))
                            continue;

                        thirdFourMatchCount++;

                        int fourthFourMatchCount = 0;
                        for (ItemInfo fourthFourMatch : fourHour)
                        {
                            if(fourthFourMatchCount > fourMatchesAllowed)
                                break;

                            if (fourthFourMatch.getsEfficiencyBonus(thirdFourMatch))
                            {
                                fourthFourMatchCount++;
                                allEfficientChains.add(List.of(fourthFourMatch.item, thirdFourMatch.item, secondFourMatch.item, firstFourMatch.item, topItem.item));
                            }
                        }
                    }
                }
            }


            int sixHourMatchCount = 0;
            for (ItemInfo sixHourMatch : sixHour)
            {
                if(sixHourMatchCount > sixMatchesAllowed)
                    break;
                if (!sixHourMatch.getsEfficiencyBonus(topItem))
                    continue;
                sixHourMatchCount++;

                //4-6-6-8
                int sixSixMatchCount = 0;
                for (ItemInfo sixSixMatch : sixHour)
                {
                    if(sixSixMatchCount > sixMatchesAllowed)
                        break;
                    if (!sixSixMatch.getsEfficiencyBonus(sixHourMatch))
                        continue;
                    sixSixMatchCount++;

                    int fourSixMatchCount = 0;
                    for(var fourSixMatch : fourHour)
                    {
                        if(fourSixMatchCount > fourMatchesAllowed)
                            break;
                        if(fourSixMatch.getsEfficiencyBonus(sixSixMatch))
                        {
                            fourSixMatchCount++;
                            allEfficientChains.add(List.of(fourSixMatch.item, sixSixMatch.item, sixHourMatch.item, topItem.item));
                        }
                    }
                    int fourEightMatchCount = 0;
                    for(var fourEightMatch : fourHour)
                    {
                        if(fourEightMatchCount > fourMatchesAllowed)
                            break;
                        if(fourEightMatch.getsEfficiencyBonus(topItem))
                        {
                            fourEightMatchCount++;
                            allEfficientChains.add(List.of(fourEightMatch.item, topItem.item, sixHourMatch.item, sixSixMatch.item));
                        }
                    }
                }

                //4-6-8-6
                int fourMatchCount = 0;
                for (ItemInfo fourMatch : fourHour)
                {
                    if(fourMatchCount > fourMatchesAllowed)
                        break;
                    if (!fourMatch.getsEfficiencyBonus(sixHourMatch))
                        continue;
                    fourMatchCount++;
                    int other6MatchCount = 0;
                    for(var other6Match : sixHour)
                    {
                        if(other6MatchCount > sixMatchesAllowed)
                            break;
                        if(other6Match.getsEfficiencyBonus(topItem))
                        {
                            other6MatchCount++;
                            allEfficientChains.add(List.of(fourMatch.item, sixHourMatch.item, topItem.item, other6Match.item));
                        }
                    }
                }
            }
        }

        for(int i=0; i<four.size(); i++)
        {
            for(int j=0; j<four.size(); j++)
            {
                allEfficientChains.add(List.of(four.get(i), eight.get(i), four.get(j), eight.get(j)));
            }
        }

        //Find schedules based on 6-hour crafts
        for (int i=0; i<topSixesAllowed && i < sixHour.size(); i++)
        {
            var topItem = sixHour.get(i);
            //6-6-6-6

            HashSet<ItemInfo> sixMatches = new HashSet<>();
            int sixMatchCount = 0;
            for (ItemInfo sixMatch : sixHour) {
                if(sixMatchCount > sixMatchesAllowed)
                    break;
                if (!sixMatch.getsEfficiencyBonus(topItem))
                    continue;
                sixMatchCount++;
                sixMatches.add(sixMatch);
            }
            for (ItemInfo firstSix : sixMatches)
            {
                for (ItemInfo secondSix : sixMatches)
                {
                    allEfficientChains.add(List.of( secondSix.item, topItem.item, firstSix.item, topItem.item ));
                }
            }


            for (ItemInfo firstFourMatch : fourHour)
            {
                if (!firstFourMatch.getsEfficiencyBonus(topItem))
                    continue;
                for(var sixMatch : sixHour)
                {
                    if(!sixMatch.getsEfficiencyBonus(firstFourMatch))
                        continue;

                    for (ItemInfo secondFourMatch : fourHour)
                    {
                        if (!secondFourMatch.getsEfficiencyBonus(sixMatch))
                            continue;

                        for (ItemInfo thirdFourMatch : fourHour)
                        {
                            //4-4-6-4-6
                            if(!thirdFourMatch.getsEfficiencyBonus(secondFourMatch))
                                continue;

                            allEfficientChains.add(List.of(thirdFourMatch.item, secondFourMatch.item, sixMatch.item, firstFourMatch.item, topItem.item));
                        }

                    }
                }
            }

            //4-6-6-8
            int eightMatchCount = 0;
            for(var eightMatch : eightHour)
            {
                if(eightMatchCount > eightMatchesAllowed)
                    break;
                if(!eightMatch.getsEfficiencyBonus(topItem))
                    continue;
                eightMatchCount++;
                for (ItemInfo sixSixMatch : sixMatches)
                {
                    int fourSixMatchCount = 0;
                    for(var fourSixMatch : fourHour)
                    {
                        if(fourSixMatchCount > fourMatchesAllowed)
                            break;
                        if(fourSixMatch.getsEfficiencyBonus(sixSixMatch))
                        {
                            fourSixMatchCount++;
                            allEfficientChains.add(List.of(fourSixMatch.item, sixSixMatch.item, topItem.item, eightMatch.item));
                        }
                    }
                }
            }


            //4-6-8-6
            eightMatchCount = 0;
            for(var eightMatch : eightHour)
            {
                if(eightMatchCount > eightMatchesAllowed)
                    break;
                if(!eightMatch.getsEfficiencyBonus(topItem))
                    continue;
                eightMatchCount++;

                int sixEightMatchCount = 0;
                for(var sixEightMatch : sixHour)
                {
                    if(sixEightMatchCount > sixMatchesAllowed)
                        break;
                    if(!sixEightMatch.getsEfficiencyBonus(eightMatch))
                        continue;
                    sixEightMatchCount++;
                    int fourMatchCount = 0;
                    for (ItemInfo fourMatch : fourHour)
                    {
                        if(fourMatchCount > fourMatchesAllowed)
                            break;
                        if (fourMatch.getsEfficiencyBonus(sixEightMatch)) {
                            allEfficientChains.add(List.of(fourMatch.item, sixEightMatch.item, eightMatch.item, topItem.item));
                            fourMatchCount++;
                        }
                    }
                }
            }
        }

        for (int i=0; i<topFoursAllowed && i < fourHour.size(); i++)
        {
            var topItem = fourHour.get(i);
            int fourMatchCount = 0;
            for(var fourMatch : fourHour)
            {
                if(fourMatchCount > fourMatchesAllowed)
                    break;
                if(!fourMatch.getsEfficiencyBonus(topItem))
                    continue;
                fourMatchCount++;
                int secondFourMatchCount = 0;
                for(var secondFourMatch : fourHour)
                {
                    if(secondFourMatchCount > fourMatchesAllowed)
                        break;
                    if(!secondFourMatch.getsEfficiencyBonus(fourMatch))
                        continue;
                    secondFourMatchCount++;
                    int thirdFourMatchCount = 0;
                    for(var thirdFourMatch : fourHour)
                    {
                        if(thirdFourMatchCount > fourMatchesAllowed)
                            break;
                        if(!secondFourMatch.getsEfficiencyBonus(thirdFourMatch))
                            continue;
                        thirdFourMatchCount++;
                        int fourthFourMatchCount = 0;
                        for(var fourthFourMatch : fourHour)
                        {
                            if(fourthFourMatchCount > fourMatchesAllowed)
                                break;
                            if(fourthFourMatch.getsEfficiencyBonus(topItem))
                            {
                                fourthFourMatchCount++;
                                allEfficientChains.add(List.of(thirdFourMatch.item, secondFourMatch.item, fourMatch.item,
                                        topItem.item, fourthFourMatch.item, topItem.item));
                            }
                        }
                    }
                }
            }
        }

        return allEfficientChains;
    }

    public static BruteForceSchedules getBestSchedules(int day, int groove,
                                                                     Map<Item,Integer> limitedUse, int allowUpToDay, Item startingItem, int limit, boolean forcePeaks, int hoursLeft)
    {
        HashMap<WorkshopSchedule, Integer> safeSchedules = new HashMap<>();
        Collection<List<Item>> filteredItemLists;

        if(limit == 0)
            limit = 1;

        if(startingItem != null || hoursLeft < 24)
        {
            filteredItemLists = CSVImporter.allEfficientChains.stream()
                    .filter(list -> list.stream().allMatch(item -> items[item.ordinal()].rankUnlocked <= islandRank))
                    .filter(list -> list.stream().allMatch(item -> items[item.ordinal()].peaksOnOrBeforeDay(allowUpToDay))).collect(Collectors.toList());

            if(forcePeaks)
                filteredItemLists = CSVImporter.allEfficientChains.stream()
                        .filter(list -> list.stream().anyMatch(item -> items[item.ordinal()].peaksOnDay(day)))
                        .collect(Collectors.toList());
            if (startingItem != null)
            {
                filteredItemLists = filteredItemLists.stream().filter (list -> list.stream().limit(1).allMatch(item -> item == startingItem)).collect(Collectors.toList());
            }

            if(hoursLeft < 24)
            {
                for(int i=0; i<filteredItemLists.size(); i++)
                {
                    for(var schedule : filteredItemLists)
                    {
                        while(getHoursUsed(schedule)>hoursLeft && schedule.size() > 0)
                        {
                            schedule.remove(schedule.size()-1);
                        }
                    }
                }
            }
        }
        else
            filteredItemLists = getGeneratedSchedules(day, allowUpToDay, islandRank, limitedUse);





        for (List<Item> list : filteredItemLists)
        {
            addToScheduleMap(list, day, limitedUse, safeSchedules, groove);
        }

        var sortedSchedules = safeSchedules
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        List<Item> firstNonInterfering = new ArrayList<>();
        if(islandRank >= 15)
        {
            for (Entry<WorkshopSchedule, Integer>  sortedSchedule : sortedSchedules)
            {
                var subItems = sortedSchedule.getKey().getItems();
                if (!sortedSchedules.get(0).getKey().interferesWithMe(subItems))
                {
                    firstNonInterfering = subItems;
                    break;
                }
            }
        }

        BruteForceSchedules schedules = new BruteForceSchedules(sortedSchedules.stream().limit(limit).collect(Collectors.toList()), day, groove);
        schedules.setBestSubItems(firstNonInterfering);

        return schedules;
    }

    private static int getHoursUsed(List<Item> schedule)
    {
        int hours = 0;
        for(Item item : schedule)
        {
            hours += items[item.ordinal()].time;
        }
        return hours;
    }

    private static CycleSchedule getBestSchedule(int day, int groove)
    {
        populateReservedItems(day);
        return getBestSchedule(day, groove, null, day);
    }

    private static CycleSchedule getBestSchedule(int day, int groove,
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
        if (day >= CSVImporter.currentPeaks[11].length)
            return false;

        for (int i = 0; i < items.length; i++)
        {
            items[i].peak = CSVImporter.currentPeaks[i][day];
            /*if(day==1 && items[i].peak == Cycle3Weak)
                items[i].peak = Cycle67;*/
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
    public static <E> void iteratePermutations(List<E> original, Consumer<List<E>> consumer) {
        Objects.requireNonNull(original);
        consumer.accept(original);
        iteratePermutationsRecursively(original, 0, consumer);
    }

    public static <E> void iteratePermutationsRecursively(List<E> original, int start, Consumer<List<E>> consumer) {
        Objects.requireNonNull(original);
        for (int i = start; i < original.size() - 1; i++) {
            for (int j = i + 1; j < original.size(); j++) {
                List<E> temp = new ArrayList<>(original);
                E tempVal = temp.get(i);
                temp.set(i, temp.get(j));
                temp.set(j, tempVal);
                consumer.accept(temp);
                iteratePermutationsRecursively(temp, i + 1, consumer);
            }
        }
    }
}
