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
import java.util.Map;
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
            
            
    
    public static void main(String[] args)
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
        
        
        /*
         * System.out.println("\nDay1: "); for(ItemInfo item : items) {
         * System.out.println(item); }
         */
        
        /*
         * items[3].addCrafted(7, 0); items[7].addCrafted(2, 0); items[9].addCrafted(7,
         * 0); items[25].addCrafted(8, 0); items[38].addCrafted(2, 0);
         * 
         * items[3].addObservedDay(Sufficient, None);
         * items[7].addObservedDay(Nonexistent, Skyrocketing);
         * items[9].addObservedDay(Sufficient, None);
         * items[25].addObservedDay(Sufficient, Skyrocketing);
         * items[38].addObservedDay(Nonexistent, Skyrocketing);
         * 
         * System.out.println(items[3]); System.out.println(items[7]);
         * System.out.println(items[9]); System.out.println(items[25]);
         * System.out.println(items[38]);
         */
        
        
        
        items[0].addObservedDay(Sufficient,None);
        items[1].addObservedDay(Insufficient,None);
        items[2].addObservedDay(Insufficient,None);
        items[3].addObservedDay(Insufficient,None);
        items[4].addObservedDay(Insufficient,None);
        items[5].addObservedDay(Insufficient,Increasing);
        items[6].addObservedDay(Insufficient,None);
        items[7].addObservedDay(Nonexistent,Skyrocketing);
        items[8].addObservedDay(Insufficient,None);
        items[9].addObservedDay(Sufficient,None);
        items[10].addObservedDay(Sufficient,None);
        items[11].addObservedDay(Insufficient,Skyrocketing);
        items[12].addObservedDay(Sufficient,None);
        items[13].addObservedDay(Insufficient,Increasing);
        items[14].addObservedDay(Sufficient,None);
        items[15].addObservedDay(Insufficient,None);
        items[16].addObservedDay(Insufficient,Increasing);
        items[17].addObservedDay(Insufficient,Increasing);
        items[18].addObservedDay(Insufficient,None);
        items[19].addObservedDay(Sufficient,None);
        items[20].addObservedDay(Insufficient,None);
        items[21].addObservedDay(Insufficient,None);
        items[22].addObservedDay(Insufficient,Increasing);
        items[23].addObservedDay(Insufficient,Increasing);
        items[24].addObservedDay(Insufficient,Increasing);
        items[25].addObservedDay(Insufficient,Skyrocketing);
        items[26].addObservedDay(Insufficient,None);
        items[27].addObservedDay(Insufficient,None);
        items[28].addObservedDay(Sufficient,None);
        items[29].addObservedDay(Sufficient,None);
        items[30].addObservedDay(Insufficient,None);
        items[31].addObservedDay(Insufficient,Increasing);
        items[32].addObservedDay(Insufficient,None);
        items[33].addObservedDay(Insufficient,None);
        items[34].addObservedDay(Nonexistent,Skyrocketing);
        items[35].addObservedDay(Nonexistent,Skyrocketing);
        items[36].addObservedDay(Sufficient,None);
        items[37].addObservedDay(Sufficient,None);
        items[38].addObservedDay(Nonexistent,Skyrocketing);
        items[39].addObservedDay(Sufficient,None);
        items[40].addObservedDay(Sufficient,None);
        items[41].addObservedDay(Insufficient,Skyrocketing);
        items[42].addObservedDay(Sufficient,None);
        items[43].addObservedDay(Sufficient,None);
        items[44].addObservedDay(Sufficient,None);
        items[45].addObservedDay(Insufficient,None);
        items[46].addObservedDay(Insufficient,Skyrocketing);
        items[47].addObservedDay(Sufficient,None);
        items[48].addObservedDay(Insufficient,None);
        items[49].addObservedDay(Insufficient,None);
        
        /*
         * System.out.println("\nDay2: "); for(ItemInfo item : items) {
         * System.out.println(item); }
         */
        
        items[0].addObservedDay(Insufficient,Increasing);
        items[1].addObservedDay(Sufficient,Decreasing);
        items[2].addObservedDay(Sufficient,Plummeting);
        items[3].addObservedDay(Sufficient,Decreasing);
        items[4].addObservedDay(Sufficient,Plummeting);
        items[5].addObservedDay(Sufficient,Plummeting);
        items[6].addObservedDay(Sufficient,Plummeting);
        items[7].addObservedDay(Sufficient,Plummeting);
        items[8].addObservedDay(Sufficient,Plummeting);
        items[9].addObservedDay(Sufficient,None);
        items[10].addObservedDay(Insufficient,Increasing);
        items[11].addObservedDay(Nonexistent,Skyrocketing);
        items[12].addObservedDay(Insufficient,Skyrocketing);
        items[13].addObservedDay(Insufficient,Increasing);
        items[14].addObservedDay(Sufficient,None);
        items[15].addObservedDay(Sufficient,Plummeting);
        items[16].addObservedDay(Sufficient,Plummeting);
        items[17].addObservedDay(Sufficient,Plummeting);
        items[18].addObservedDay(Sufficient,Plummeting);
        items[19].addObservedDay(Insufficient,Increasing);
        items[20].addObservedDay(Sufficient,Plummeting);
        items[21].addObservedDay(Sufficient,Plummeting);
        items[22].addObservedDay(Insufficient,Increasing);
        items[23].addObservedDay(Insufficient,Increasing);
        items[24].addObservedDay(Insufficient,Increasing);
        items[25].addObservedDay(Nonexistent,Skyrocketing);
        items[26].addObservedDay(Sufficient,Plummeting);
        items[27].addObservedDay(Sufficient,Decreasing);
        items[28].addObservedDay(Sufficient,None);
        items[29].addObservedDay(Insufficient,Increasing);
        items[30].addObservedDay(Sufficient,Plummeting);
        items[31].addObservedDay(Sufficient,Plummeting);
        items[32].addObservedDay(Sufficient,Plummeting);
        items[33].addObservedDay(Sufficient,Plummeting);
        items[34].addObservedDay(Surplus,Plummeting);
        items[35].addObservedDay(Surplus,Plummeting);
        items[36].addObservedDay(Insufficient,Skyrocketing);
        items[37].addObservedDay(Sufficient,None);
        items[38].addObservedDay(Sufficient,Plummeting);
        items[39].addObservedDay(Sufficient,None);
        items[40].addObservedDay(Insufficient,Skyrocketing);
        items[41].addObservedDay(Nonexistent,Skyrocketing);
        items[42].addObservedDay(Sufficient,None);
        items[43].addObservedDay(Sufficient,None);
        items[44].addObservedDay(Sufficient,None);
        items[45].addObservedDay(Sufficient,Decreasing);
        items[46].addObservedDay(Nonexistent,Skyrocketing);
        items[47].addObservedDay(Insufficient,Skyrocketing);
        items[48].addObservedDay(Sufficient,Plummeting);
        items[49].addObservedDay(Sufficient,Plummeting);
        
        /*
         * System.out.println("\nDay3: "); for(ItemInfo item : items) {
         * System.out.println(item); }
         */
        
        items[0].addObservedDay(Insufficient,Increasing);
        items[1].addObservedDay(Sufficient,Increasing);
        items[2].addObservedDay(Sufficient,Increasing);
        items[3].addObservedDay(Sufficient,Increasing);
        items[4].addObservedDay(Sufficient,None);
        items[5].addObservedDay(Sufficient,None);
        items[6].addObservedDay(Sufficient,None);
        items[7].addObservedDay(Sufficient,None);
        items[8].addObservedDay(Sufficient,Increasing);
        items[9].addObservedDay(Insufficient,Increasing);
        items[10].addObservedDay(Insufficient,Increasing);
        items[11].addObservedDay(Sufficient,Plummeting);
        items[12].addObservedDay(Nonexistent,Skyrocketing);
        items[13].addObservedDay(Sufficient,Plummeting);
        items[14].addObservedDay(Insufficient,Skyrocketing);
        items[15].addObservedDay(Sufficient,Skyrocketing);
        items[16].addObservedDay(Sufficient,None);
        items[17].addObservedDay(Sufficient,None);
        items[18].addObservedDay(Sufficient,Skyrocketing);
        items[19].addObservedDay(Insufficient,Increasing);
        items[20].addObservedDay(Sufficient,None);
        items[21].addObservedDay(Sufficient,Skyrocketing);
        items[22].addObservedDay(Sufficient,Plummeting);
        items[23].addObservedDay(Surplus,Plummeting);
        items[24].addObservedDay(Sufficient,Plummeting);
        items[25].addObservedDay(Sufficient,Plummeting);
        items[26].addObservedDay(Sufficient,Skyrocketing);
        items[27].addObservedDay(Sufficient,Increasing);
        items[28].addObservedDay(Insufficient,Skyrocketing);
        items[29].addObservedDay(Insufficient,Increasing);
        items[30].addObservedDay(Sufficient,Increasing);
        items[31].addObservedDay(Sufficient,None);
        items[32].addObservedDay(Sufficient,None);
        items[33].addObservedDay(Sufficient,Increasing);
        items[34].addObservedDay(Surplus,None);
        items[35].addObservedDay(Surplus,None);
        items[36].addObservedDay(Nonexistent,Skyrocketing);
        items[37].addObservedDay(Insufficient,Increasing);
        items[38].addObservedDay(Sufficient,None);
        items[39].addObservedDay(Insufficient,Increasing);
        items[40].addObservedDay(Nonexistent,Skyrocketing);
        items[41].addObservedDay(Sufficient,Plummeting);
        items[42].addObservedDay(Insufficient,Skyrocketing);
        items[43].addObservedDay(Insufficient,Skyrocketing);
        items[44].addObservedDay(Insufficient,Increasing);
        items[45].addObservedDay(Sufficient,Increasing);
        items[46].addObservedDay(Surplus,Plummeting);
        items[47].addObservedDay(Nonexistent,Skyrocketing);
        items[48].addObservedDay(Sufficient,None);
        items[49].addObservedDay(Sufficient,Increasing);
        
        
        /*
         * System.out.println("\nDay4: "); for(ItemInfo item : items) {
         * System.out.println(item); }
         */
        
        addDay(Arrays.asList(Butter,TomatoRelish,Jam,TomatoRelish,Jam), 1);
        addDay(Arrays.asList(CulinaryKnife,GardenScythe,SilverEarCuffs,GardenScythe),2);
        addDay(Arrays.asList(SheepfluffRug, Hora, SheepfluffRug, Hora),4);
        addDay(Arrays.asList(Crook, SpruceRoundShield, Crook),5);
        addDay(Arrays.asList(BoiledEgg, ScaleFingers, BoiledEgg, ScaleFingers),6);
        
    }
    
    public static void addDay(List<Item> crafts, int day)
    {
        CycleSchedule schedule = new CycleSchedule(day, groove);
        schedule.setForAllWorkshops(crafts);
        
        System.out.println("day "+(day+1)+" total: "+schedule.getValue()+" material cost: "+schedule.getMaterialCost());
        
        schedule.numCrafted.forEach((k,v)->{items[k.ordinal()].addCrafted(v, day);});
        groove = schedule.endingGroove;
    }
    
    public static Supply getSupplyBucket(int supply)
    {
        if(supply < -8)
            return Supply.Nonexistent;
        if(supply < 0)
            return Supply.Insufficient;
        if(supply < 8)
            return Supply.Sufficient;
        if(supply < 16)
            return Supply.Surplus;
        return Supply.Overflowing;
    }
    
    public static DemandShift getDemandShift(int prevSupply, int newSupply)
    {
        int diff = newSupply - prevSupply;
        if(diff < -5)
            return DemandShift.Skyrocketing;
        if(diff < -1)
            return DemandShift.Increasing;
        if(diff < 2)
            return DemandShift.None;
        if(diff < 6)
            return DemandShift.Decreasing;
        return DemandShift.Plummeting;
    }
}
