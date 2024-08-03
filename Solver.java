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
            new ItemInfo(Item.SweetPopoto,Confections,Invalid,72,6,5,Map.of(Popoto, 2, Milk,1)),
            new ItemInfo(ParsnipSalad,Foodstuffs,Invalid,48,4,5,Map.of(Parsnip,2)),
            new ItemInfo(Caramels,Confections,Invalid,81,6,6,Map.of(Milk,2)),
            new ItemInfo(Ribbon,Accessories,Textiles,54,6,6,null),
            new ItemInfo(Rope,Sundries,Textiles,36,4,6,null),
            new ItemInfo(CavaliersHat,Attire,Textiles,81,6,6,Map.of(Feather,2)),
            new ItemInfo(Item.Horn,Sundries,CreatureCreations,81,6,6,Map.of(RareMaterial.Horn,2)),
            new ItemInfo(SaltCod,PreservedFood,MarineMerchandise,54,6,7,null),
            new ItemInfo(SquidInk,Ingredients,MarineMerchandise,36,4,7,null),
            new ItemInfo(EssentialDraught,Concoctions,MarineMerchandise,54,6,7,null),
            new ItemInfo(IsleberryJam,Ingredients,Invalid,78,6,7,Map.of(Isleberry,3)),
            new ItemInfo(TomatoRelish,Ingredients,Invalid,52,4,7,Map.of(Tomato,2)),
            new ItemInfo(OnionSoup,Foodstuffs,Invalid,78,6,7,Map.of(Onion,3)),
            new ItemInfo(IslefishPie,Confections,MarineMerchandise,78,6,7,Map.of(Wheat,3)),
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
            new ItemInfo(BrassServingDish, Sundries, Metalworks, 36, 4, 16, null),
            new ItemInfo(GrindingWheel, Sundries, Invalid, 60, 6, 16, null),
            new ItemInfo(DuriumTathlums, Arms, Metalworks, 54, 6,17, null),
            new ItemInfo(GoldHairpin, Accessories, Metalworks, 72, 8,17, null),
            new ItemInfo(MammetAward, Furnishings, Invalid, 80, 8, 17, null),
            new ItemInfo(FruitPunch, Confections, Invalid, 52, 4, 18, Map.of(Watermelon, 1, Isleberry, 1)),
            new ItemInfo(SweetPopotoPie, Foodstuffs, Confections, 120, 8, 18, Map.of(RareMaterial.SweetPopoto, 3, Wheat, 1, Egg, 1)),
            new ItemInfo(Peperoncino, Foodstuffs, Invalid, 75, 6, 18, Map.of(Broccoli, 2, Wheat, 1)),
            new ItemInfo(BuffaloBeanSalad, Foodstuffs, CreatureCreations, 52, 4, 18, Map.of(BuffaloBeans, 2, Milk, 2)),
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
    public static Map<List<Item>, Integer> schedulesByPopularity = new HashMap<>();

    private static Map<Integer, TempSchedule> scheduledDays = new HashMap<>();

    private static Map<RareMaterial, Integer> matsUsed = new TreeMap<>();
    private static boolean logMats = false;

    private static boolean logCommonSchedules = true;
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
        int startWeek = 60;
        int endWeek = 100;
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
                else if(week <=58)
                {
                    WORKSHOP_BONUS = 130;
                    islandRank = 15;
                    NUM_WORKSHOPS = 4;
                }
                else
                {
                    WORKSHOP_BONUS = 140;
                    islandRank = 20;
                    NUM_WORKSHOPS = 4;
                }
                averageDayValue = 1123 * WORKSHOP_BONUS * NUM_WORKSHOPS / 100;

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
                //solveRecsForWeek();
                //solveFortuneTeller();
                solveRecsWithPerfectInfo();
                //solveCrimeTime(week);


                //verboseCalculatorLogging = true;
                //setObservedFromCSV(3);

                //setStaticSchedule(List.of(0,1,3,4,2), orderIndex);
                /*CycleSchedule schedule = new CycleSchedule(1,0);
                schedule.setForFirstThreeWorkshops(Arrays.asList(Earrings, Item.Horn, ScaleFingers, CavaliersHat));
                schedule.setFourthWorkshop(Arrays.asList(Earrings, Item.Horn, ScaleFingers, CavaliersHat));
                setDay(schedule, 1);

                CycleSchedule schedule2 = new CycleSchedule(3,groove);
                schedule2.setForFirstThreeWorkshops(Arrays.asList(Earrings, SilverEarCuffs, Earrings, SilverEarCuffs));
                schedule2.setFourthWorkshop(Arrays.asList(RunnerBeanSaute, OnionSoup, RunnerBeanSaute, OnionSoup, RunnerBeanSaute));
                setDay(schedule2, 3);

                CycleSchedule schedule3 = new CycleSchedule(4,groove);
                schedule3.setForFirstThreeWorkshops(Arrays.asList(Brush, SharkOil, Brush, SharkOil));
                schedule3.setFourthWorkshop(Arrays.asList(RunnerBeanSaute, Isloaf, RunnerBeanSaute, GrilledClam, SharkOil));
                setDay(schedule3, 4);

                CycleSchedule schedule4 = new CycleSchedule(5,groove);
                schedule4.setForFirstThreeWorkshops(Arrays.asList(BoiledEgg, SheepfluffRug, Bed, SheepfluffRug));
                schedule4.setFourthWorkshop(Arrays.asList(Rope, Tunic, Bed, SheepfluffRug));
                setDay(schedule4, 5);

                CycleSchedule schedule5 = new CycleSchedule(6,groove);
                schedule5.setForFirstThreeWorkshops(Arrays.asList(Firesand, GarnetRapier, Firesand, GarnetRapier));
                schedule5.setFourthWorkshop(Arrays.asList(SquidInk, Bouillabaisse, Firesand, GarnetRapier));
                setDay(schedule5, 6);*/


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

            if(logCommonSchedules)
            {
                var sortedSchedules = schedulesByPopularity
                        .entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toList());
                System.out.println("Top schedules: ");
                for(int i=0;i<15 && i<sortedSchedules.size();i++)
                {
                    var sched = sortedSchedules.get(i);
                    System.out.println(sched.getKey()+": "+sched.getValue());
                }
            }


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
                //bestOrder = order;
                bestRest = orderIndex;
            }
        }

        System.out.println("Best average: "+bestAverage+", so best rest: "+bestRest);*/


    }

    public static void solveFortuneTeller()
    {
        long time = System.currentTimeMillis();
        rested = true;
        setObservedFromCSV(1);
        setDay(getBestSchedule(2, 0), 2);
        solveRestOfWeek(1, true);
        System.out.println("Total: " + totalGross + " (" + totalNet + ")\n" + "Took " + (System.currentTimeMillis() - time) + "ms.\n");
    }

    public static void setStaticSchedule(List<Integer> order, int dayToRest)
    {

        CycleSchedule c1 = new CycleSchedule(0,0);
        c1.setWorkshop(0, List.of(Earrings, SheepfluffRug, Earrings, Hora, CawlCennin));
        c1.setWorkshop(1, List.of(Earrings, SheepfluffRug, Earrings, Hora, CawlCennin));
        c1.setWorkshop(2, List.of(PopotoSalad, ParsnipSalad, Isloaf, PopotoSalad, ParsnipSalad, PopotoSalad));
        c1.setWorkshop(3, List.of(PopotoSalad, ParsnipSalad, Isloaf, PopotoSalad, ParsnipSalad, PopotoSalad));

        CycleSchedule c2 = new CycleSchedule(0,0);
        c2.setWorkshop(0, List.of(FruitPunch, PumpkinPudding, FruitPunch, PumpkinPudding, FruitPunch));
        c2.setWorkshop(1, List.of(FruitPunch, PumpkinPudding, FruitPunch, PumpkinPudding, FruitPunch));
        c2.setWorkshop(2, List.of(Firesand, SeashineOpal, BrickCounter, CoolingGlass));
        c2.setWorkshop(3, List.of(Firesand, SeashineOpal, BrickCounter, CoolingGlass));

        CycleSchedule c3 = new CycleSchedule(0,0);
        c3.setWorkshop(0, List.of(Necklace, Macuahuitl, Crook, DuriumTathlums));
        c3.setWorkshop(1, List.of(Necklace, Macuahuitl, Crook, DuriumTathlums));
        c3.setWorkshop(2, List.of(Necklace, Macuahuitl, SpruceRoundShield, CavaliersHat));
        c3.setWorkshop(3, List.of(Necklace, Macuahuitl, SpruceRoundShield, CavaliersHat));

        CycleSchedule c4 = new CycleSchedule(0,0);
        c4.setWorkshop(0, List.of(Hora, BuffaloBeanSalad, SweetPopotoPie, OnionSoup));
        c4.setWorkshop(1, List.of(Hora, BuffaloBeanSalad, SweetPopotoPie, OnionSoup));
        c4.setWorkshop(2, List.of(Necklace, SilverEarCuffs, GardenScythe, DriedFlowers));
        c4.setWorkshop(3, List.of(Necklace, SilverEarCuffs, GardenScythe, DriedFlowers));

        CycleSchedule c5 = new CycleSchedule(0,0);
        c5.setWorkshop(0, List.of(BuffaloBeanSalad, BeetSoup, Bouillabaisse, ImamBayildi));
        c5.setWorkshop(1, List.of(BuffaloBeanSalad, BeetSoup, Bouillabaisse, ImamBayildi));
        c5.setWorkshop(2, List.of(BuffaloBeanSalad, BeetSoup, Bouillabaisse, ImamBayildi));
        c5.setWorkshop(3, List.of(BuffaloBeanSalad, BeetSoup, Bouillabaisse, ImamBayildi));

        List<CycleSchedule> crafts = new ArrayList<>();
        crafts.add(c1);
        crafts.add(c2);
        crafts.add(c3);
        crafts.add(c4);
        crafts.add(c5);


        int orderIndex = 0;
        for(int i=1;i<7;i++)
        {
            if(i==dayToRest)
                i++;
            if(i>=7)
                continue;

            int scheduleIndex = order.get(orderIndex);
            orderIndex++;
            crafts.get(scheduleIndex).startingGroove = groove;
            crafts.get(scheduleIndex).day = i;
            setDay(crafts.get(scheduleIndex), i, true);
        }



    }

    public static void checkFavorsAndSet(CycleSchedule currentSchedule, List<Item> favors)
    {
        int weightedValue = currentSchedule.getWeightedValue();
        setDay(currentSchedule, currentSchedule.day, false);
        var restOfWeek = solveRestOfWeek(currentSchedule.day-1, false);
        List<FavorModification> recsModifications = new ArrayList<>();
        for(Item favor : favors)
        {
            var bestCycle = getBestFavorCycle(currentSchedule, favor);
            System.out.println("Best schedule for favor "+favor+" is "+bestCycle.getSchedule().printWorkshops()+" with a value of "+bestCycle.getValue());
            int tomorrowDelta = weightedValue - bestCycle.getValue();
            System.out.println("Value is "+tomorrowDelta+" less than main recs");

            int futureDelta = 0;


            //get worst cycle value we're not resting on
            int bestDelta = 99999;
            FavorModification bestFutureReplacement = null;
            for(var sched : restOfWeek)
            {
                if(sched.day != 0)
                {
                    var bestFutureCycle = getBestFavorCycle(sched, favor);

                    setHypotheticalGroove(sched, currentSchedule.day, currentSchedule.startingGroove);
                    setHypotheticalGroove(bestFutureCycle.getSchedule(), currentSchedule.day, currentSchedule.startingGroove);
                    int delta = sched.getWeightedValue() - bestFutureCycle.getSchedule().getWeightedValue();
                    System.out.println("Best future schedule for favor "+favor+" on cycle "+(sched.day+1)+" is "
                            +bestFutureCycle.getSchedule().printWorkshops()+" with a value of "+bestFutureCycle.getSchedule().getWeightedValue()
                            +" and a delta of "+delta);

                    if(delta < bestDelta)
                    {
                        bestDelta = delta;
                        bestFutureReplacement = bestFutureCycle;
                    }
                }
            }
            System.out.println("Best future delta is "+bestDelta+" less than anticipated future recs");
            futureDelta = bestDelta;


            if(futureDelta < tomorrowDelta)
                System.out.println("Future delta seems smaller. Make "+favor+" later in the week!");
            else
            {
                System.out.println("This seems like the best it's going to get. Make "+favor+" tomorrow!");
                bestCycle.setDelta(futureDelta-tomorrowDelta);
                recsModifications.add(bestCycle);
            }

            System.out.println("\n\n\n\n");
        }

        recsModifications.sort(Comparator.comparingInt(FavorModification::getDelta));
        Collections.reverse(recsModifications);

        if(recsModifications.size() >= 2 && recsModifications.get(0).getNumWorkshops() == 2 && recsModifications.get(1).getNumWorkshops() == 2)
        {
            //Use both
            CycleSchedule newSched = new CycleSchedule(currentSchedule.day, currentSchedule.startingGroove);
            newSched.setWorkshop(0, recsModifications.get(0).getWorkshop().getItems());
            newSched.setWorkshop(1, recsModifications.get(0).getWorkshop().getItems());
            newSched.setWorkshop(2, recsModifications.get(1).getWorkshop().getItems());
            newSched.setWorkshop(3, recsModifications.get(1).getWorkshop().getItems());
            favors.remove(recsModifications.get(0).getFavor());
            favors.remove(recsModifications.get(1).getFavor());
            setDay(newSched, currentSchedule.day, true);
        }
        else if(recsModifications.size() == 3 && recsModifications.get(0).getNumWorkshops() == 3 && recsModifications.get(1).getDelta() + recsModifications.get(2).getDelta() > recsModifications.get(0).getDelta())
        {
            //Use both
            CycleSchedule newSched = new CycleSchedule(currentSchedule.day, currentSchedule.startingGroove);
            newSched.setWorkshop(0, recsModifications.get(2).getWorkshop().getItems());
            newSched.setWorkshop(1, recsModifications.get(2).getWorkshop().getItems());
            newSched.setWorkshop(2, recsModifications.get(1).getWorkshop().getItems());
            newSched.setWorkshop(3, recsModifications.get(1).getWorkshop().getItems());
            favors.remove(recsModifications.get(2).getFavor());
            favors.remove(recsModifications.get(1).getFavor());
            setDay(newSched, currentSchedule.day, true);
        }
        else if (recsModifications.size() > 0)
        {
            favors.remove(recsModifications.get(0).getFavor());
            setDay(recsModifications.get(0).getSchedule(), currentSchedule.day, true);
        }
        else
        {
            setDay(currentSchedule, currentSchedule.day, true);
        }
    }

    private static void setHypotheticalGroove(CycleSchedule sched, int currentDay, int currentStarting)
    {
        sched.startingGroove = Math.min((sched.day - currentDay) * 13 + currentStarting, 45);
    }

    public static FavorModification getBestFavorCycle(CycleSchedule currentSchedule, Item favor)
    {
        //See how it is if we sub the current schedule for it
        var twoWsSchedule = getBestFavorWorkshop(currentSchedule.day, favor, false, null, currentSchedule.startingGroove);
        //System.out.println("Best schedule with 2x favor "+favor+" is "+twoWsSchedule.getKey().getItems()+" ("+twoWsSchedule.getValue()+")");
        List<FavorModification> possibleCycles = new ArrayList<>();

        CycleSchedule replaceMain = new CycleSchedule(currentSchedule.day, currentSchedule.startingGroove);
        replaceMain.setWorkshop(0, twoWsSchedule.getKey().getItems());
        replaceMain.setWorkshop(1, twoWsSchedule.getKey().getItems());
        replaceMain.setWorkshop(2, currentSchedule.getItems());
        replaceMain.setWorkshop(3, currentSchedule.getSubItems());
        int value = replaceMain.getWeightedValue();
        //System.out.println("Value replacing 2 main schedules: "+value);
        possibleCycles.add(new FavorModification(replaceMain, favor, 2, twoWsSchedule.getKey(), value));

        CycleSchedule replaceSub = new CycleSchedule(currentSchedule.day, currentSchedule.startingGroove);
        replaceSub.setWorkshop(0, currentSchedule.getItems());
        replaceSub.setWorkshop(1, currentSchedule.getItems());
        replaceSub.setWorkshop(2, twoWsSchedule.getKey().getItems());
        replaceSub.setWorkshop(3, twoWsSchedule.getKey().getItems());
        value = replaceSub.getWeightedValue();
        //System.out.println("Value replacing 1 main and sub schedules: "+value);
        possibleCycles.add(new FavorModification(replaceSub, favor, 2, twoWsSchedule.getKey(), value));

        if(items[favor.ordinal()].time == 6)
        {
            var threeWsSchedule = getBestFavorWorkshop(currentSchedule.day, favor, true, null, currentSchedule.startingGroove);
            //System.out.println("Best schedule with 1x favor "+favor+" is "+threeWsSchedule.getKey().getItems()+" ("+threeWsSchedule.getValue()+")");

            CycleSchedule replaceMainThree = new CycleSchedule(currentSchedule.day, currentSchedule.startingGroove);
            replaceMainThree.setForFirstThreeWorkshops(threeWsSchedule.getKey().getItems());
            replaceMainThree.setFourthWorkshop(currentSchedule.getSubItems());
            value = replaceMainThree.getWeightedValue();
            //System.out.println("Value replacing main schedule: "+value);
            possibleCycles.add(new FavorModification(replaceMainThree, favor, 3, threeWsSchedule.getKey(), value));

            CycleSchedule replaceSubThree = new CycleSchedule(currentSchedule.day, currentSchedule.startingGroove);
            replaceSubThree.setForFirstThreeWorkshops(threeWsSchedule.getKey().getItems());
            replaceSubThree.setFourthWorkshop( currentSchedule.getItems());
            value = replaceSubThree.getWeightedValue();
            //System.out.println("Value replacing sub schedule and 2x main schedule: "+value);
            possibleCycles.add(new FavorModification(replaceSubThree, favor, 3, threeWsSchedule.getKey(), value));
        }

        Collections.sort(possibleCycles);
        Collections.reverse(possibleCycles);

        return possibleCycles.get(0);
    }

    public static Entry<WorkshopSchedule, Integer> getBestFavorWorkshop(int day, Item requiredFavor, boolean allow3, Map<Item,Integer> limitedUse, int groove)
    {
        //If it's an 8hr, we need it to be used in 2 workshops twice efficiently
        //If it's a 6hr, we can let it be used once efficiently in 3 workshops
        //If it's a 4hr, we need to be used in 2 workshops twice efficiently
        Collection<List<Item>> filteredItemLists = CSVImporter.allEfficientChains.stream()
                .filter(list -> list.stream().allMatch(item -> items[item.ordinal()].rankUnlocked <= islandRank))
                .filter(list -> list.stream().allMatch(item -> items[item.ordinal()].peaksOnOrBeforeDay(day) || item == requiredFavor)).collect(Collectors.toList());

        //I can't figure this out with streams, sorry
        Collection<List<Item>> newFilteredList = new HashSet<>();

        for(var list : filteredItemLists)
        {
            int efficientCount = 0;
            for(int i=1; i<list.size(); i++)
            {
                if(list.get(i) == requiredFavor)
                    efficientCount++;
            }
            if((!allow3 && efficientCount>=2) || (allow3 && efficientCount == 1))
                newFilteredList.add(list);
        }

        filteredItemLists = newFilteredList;

        Map<WorkshopSchedule, Integer> safeSchedules = new HashMap<>();
        for (List<Item> list : filteredItemLists)
        {
            addToScheduleMap(list, day, limitedUse, safeSchedules, null, groove);
        }
        if(safeSchedules.size()==0)
            return null;

        var sortedSchedules = safeSchedules
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return sortedSchedules.get(0);
    }

    public static List<CycleSchedule> solveRestOfWeek(int currentDay, boolean real)
    {
        int worstIndex = -1;
        int worstValue = 99999;
        int estGroove = (groove + GROOVE_MAX) / 2;
        setObservedFromCSV(Math.min(currentDay, 3));
        Map<Item,Integer> reservedSet = new HashMap<>();
        List<CycleSchedule> restOfWeek = new ArrayList<>();

        for (int d = currentDay + 2; d < 7; d++)
        {
            var solution = getBestSchedule(d, estGroove, reservedSet, d);
            if(solution.getValue() < worstValue)
            {
                worstValue = solution.getValue();
                worstIndex = restOfWeek.size();
            }

            restOfWeek.add(solution);
            reservedSet = solution.getLimitedUses(reservedSet);
            //System.out.println("day "+(d+1)+" schedule: "+solution+" ("+solution.getValue()+")");
        }

        if(!rested)
        {
            CycleSchedule empty = new CycleSchedule(0, 0);
            //If we haven't rested, rest the worst day
            restOfWeek.remove(worstIndex);
            restOfWeek.add(worstIndex, empty);
        }


        if(restOfWeek.size() == 5) //If we're at day 1, we have no real idea, so put our best guess at C6, the second-best day to craft
        {
            //Swap C3 and C6
            var best = restOfWeek.get(0);
            var c6 = restOfWeek.get(3);
            restOfWeek.set(0, c6);
            restOfWeek.set(3, best);
        }

        if(real)
        {
            setObservedFromCSV(3);

            for(int i=0; i<restOfWeek.size();i++)
            {
                if(restOfWeek.get(i).day == 0)
                    continue;
                restOfWeek.get(i).startingGroove = groove;
                setDay(restOfWeek.get(i), i+currentDay+2, true);
            }
        }
        return restOfWeek;
    }

    private static void solveRecsWithNoSupply()
    {
        for(ItemInfo item : items)
        {
            item.peak = Unknown;
        }
        verboseSolverLogging = true;

        long time = System.currentTimeMillis();
        Map<Item,Integer> reservedSet = new HashMap<>();
        List<CycleSchedule> scheduleList = new ArrayList<>();

        for (int d = 0; d < 5; d++)
        {
            var solution = getBestSchedule(2, 15, reservedSet, 2);
            scheduleList.add(solution);

            if (verboseSolverLogging)
                System.out.println("Schedule " + (d+1) + ", crafts: "
                        + solution
                        + " value: " + solution.getValue());

            reservedSet = solution.getLimitedUses(reservedSet);
        }
        if(verboseSolverLogging)
            System.out.println();

        setObservedFromCSV(3);

        //Committing to the bit
        scheduleList.get(1).startingGroove = 0;
        setDay(scheduleList.get(1), 1, verboseSolverLogging);
        scheduleList.get(4).startingGroove = groove;
        setDay(scheduleList.get(4), 2, verboseSolverLogging);

        scheduleList.get(2).startingGroove = groove;
        setDay(scheduleList.get(2), 3, verboseSolverLogging);

        scheduleList.get(3).startingGroove = groove;
        setDay(scheduleList.get(3), 4, verboseSolverLogging);

        scheduleList.get(5).startingGroove = groove;
        setDay(scheduleList.get(0), 5, verboseSolverLogging);

        System.out.println("Season total: " + totalGross + " (" + totalNet + ")\n" + "Took "+ (System.currentTimeMillis() - time) + "ms.\n");
    }

    private static void solveRecsWithPerfectInfo()
    {
        setObservedFromCSV(3);
        long time = System.currentTimeMillis();
        int bestTotal = 0;
        List<CycleSchedule> bestSchedules = new ArrayList<>();

        for(int rest=1; rest<7; rest++)
        {
            List<CycleSchedule> schedules = new ArrayList<>();
            for(int cycle=1; cycle<7; cycle++)
            {
                if(cycle == rest)
                    schedules.add(null);
                else
                {
                    schedules.add(getBestSchedule(cycle, groove));
                    setDay(schedules.get(schedules.size()-1), cycle, false);
                }
            }
            //System.out.println("Resting C"+(rest+1)+", total: "+totalGross);
            if(totalGross > bestTotal)
            {
                bestSchedules = schedules;
                bestTotal = totalGross;
            }
            groove = 0;
            totalGross = 0;
            totalNet = 0;
            scheduledDays.clear();
            reservedHelpers.clear();
            reservedItems.clear();
            for(int day=1;day<7;day++)
            {
                for (var item : items)
                    item.setCrafted(0, day);
            }

        }

        //verboseCalculatorLogging = true;

        for(int i=1; i<7; i++)
        {
            if(bestSchedules.get(i-1) == null)
                System.out.println("Rest C"+(i+1));
            else
                setDay(bestSchedules.get(i-1), i);
        }
        verboseCalculatorLogging = false;
        System.out.println("Total: " + totalGross + " (" + totalNet + ")\n" + "Took " + (System.currentTimeMillis() - time) + "ms.\n");

    }

    private static void solveRecsForWeek()
    {
        long time = System.currentTimeMillis();

        List<Item> favors = new ArrayList<>();
        favors.add(Stove);
        favors.add(GoldHairpin);
        favors.add(RunnerBeanSaute);

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
            //checkFavorsAndSet(d2, favors);
            setDay(d2, d2.day, true);
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

                setDay(d3, 2);
                //checkFavorsAndSet(d3, favors);
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
                        setDay(d4Again, 3);
                        //checkFavorsAndSet(d4Again, favors);
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
                    setDay(d4, 3);
                    //checkFavorsAndSet(d4, favors);
                }

                if(hasNextDay)
                {
                    setLateDays();
                }
            }
        }

        if(!hasNextDay)
            solveRestOfWeek(scheduledDays.size()-1 + (rested? 1 : 0), true);

        System.out.println("Total: " + totalGross + " (" + totalNet + ")\n" + "Took " + (System.currentTimeMillis() - time) + "ms.\n");
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
            if(day==1 && !next.getKey().peaksOnDay(1))
            {
                currFullWeek++;
                current = currFullWeek;
                cap = resFullWeek;
            }
            else if(day==2)
            {
                if(next.getKey().peaksOnDay(3) || next.getKey().peaksOnDay(4))
                {
                    curr45++;
                    current = curr45;
                    cap = res45;
                }
                else if(next.getKey().peaksOnDay(5) || next.getKey().peaksOnDay(6))
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
                else if(next.getKey().peaksOnDay(5) || next.getKey().peaksOnDay(6))
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
            else if(schedule.workshops[0]==null)
            {
                System.out.println("Cycle "+(day+1)+", REST");
            }
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
        schedule.startingGroove = 0;
        int groovelessValue = schedule.getValue();
        schedule.startingGroove = startingGroove;

        if (real)
        {
            boolean oldVerbose = verboseCalculatorLogging;
            verboseCalculatorLogging = false;

            if(logMats)
                schedule.addMaterials(matsUsed);
            if(logCommonSchedules)
            {
                schedulesByPopularity.put(schedule.getItems(), schedulesByPopularity.getOrDefault(schedule.getItems(), 0)+3);
                schedulesByPopularity.put(schedule.getSubItems(), schedulesByPopularity.getOrDefault(schedule.getSubItems(), 0)+1);
            }
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
        Map<WorkshopSchedule, Integer> semiSafeSchedules = new HashMap<>();
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
            addToScheduleMap(list, day, limitedUse, safeSchedules, semiSafeSchedules, groove);
        }

        var sortedSchedules = safeSchedules
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        var sortedSemiSafe = semiSafeSchedules.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        List<Item> firstNonInterfering = new ArrayList<>();
        if(islandRank >= 15)
        {
            for (Entry<WorkshopSchedule, Integer>  sortedSchedule : sortedSemiSafe)
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
    
    private static void addToScheduleMap(List<Item> list, int day,Map<Item,Integer> limitedUse,
            Map<WorkshopSchedule, Integer> safeSchedules, Map<WorkshopSchedule, Integer> semiSafeSchedules, int groove)
    {
        WorkshopSchedule workshop = new WorkshopSchedule(list);
        if(workshop.usesTooMany(limitedUse, true))
            return;


        if(!workshop.usesTooMany(limitedUse, false))
        {
            int value = workshop.getValueWithGrooveEstimate(day, groove, false);
            // Only add if we don't already have one with this schedule or ours is better
            int oldValue = safeSchedules.getOrDefault(workshop, -1);

            if (oldValue < value)
            {
                safeSchedules.remove(workshop);
                safeSchedules.put(workshop, value);
            }
        }

        if(semiSafeSchedules != null)
        {
            int subValue = workshop.getValueWithGrooveEstimate(day, groove, true);
            int oldSubValue = semiSafeSchedules.getOrDefault(workshop, -1);
            if(oldSubValue < subValue)
            {
                semiSafeSchedules.remove(workshop);
                semiSafeSchedules.put(workshop, subValue);
            }
        }
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
