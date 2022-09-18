package islandworkshop;
import static islandworkshop.ItemCategory.*;
import static islandworkshop.RareMaterial.*;
import static islandworkshop.Supply.*;
import static islandworkshop.Popularity.*;
import static islandworkshop.DemandShift.*;
import static islandworkshop.PeakCycle.*;
import java.util.Map;
public class Solver
{
    final static int[][] SUPPLY_PATH = {{0, 0,0}, //Unknown
            {-4, -4, 10, 0, 0, 0, 0}, //Cycle2Weak 
            {-8, -7, 15, 0, 0, 0, 0}, //Cycle2Strong
            {0, -4, -4, 10, 0, 0, 0}, //Cycle3Weak
            {0, -8, -7, 15, 0, 0, 0}, //Cycle3Strong
            {0, 0, -4, -4, 10, 0, 0}, //Cycle4Weak
            {0, 0, -8, -7, 15, 0, 0}, //Cycle4Strong
            {0, 0, 0, -4, -4, 10, 0}, //5Weak
            {0, 0, 0, -8, -7, 15, 0}, //5Strong
            {0, -1, 5, -4, -4, -4, 10}, //6Weak
            {0, -1, 8, -7, -8, -7, 15}, //6Strong
            {0, -1, 8, -3, -4, -4, -4}, //7Weak
            {0, -1, 8, 0, -7, -8, -7}, //7Strong
            {0, 0, 0}, //4/5
            {0, 0, 0,-4,-4}, //5
            {0, -1, 8, 0} //6/7
            };
    
    final static ItemInfo[] ITEMS = {
            new ItemInfo("Potion",Concoctions,Invalid,28,4,null),
            new ItemInfo("Firesand",Concoctions,UnburiedTreasures,28,4,null),
            new ItemInfo("Wooden Chair",Furnishings,Woodworks,42,6,null),
            new ItemInfo("Grilled Clam",Foodstuffs,MarineMerchandise,28,4,null),
            new ItemInfo("Necklace",Accessories,Woodworks,28,4,null),
            new ItemInfo("Coral Ring",Accessories,MarineMerchandise,42,6,null),
            new ItemInfo("Barbut",Attire,Metalworks,42,6,null),
            new ItemInfo("Macuahuitl",Arms,Woodworks,42,6,null),
            new ItemInfo("Sauerkraut",PreservedFood,Invalid,40,4,Map.of(Cabbage,1)),
            new ItemInfo("Baked Pumpkin",Foodstuffs,Invalid,40,4,Map.of(Pumpkin,1)),
            new ItemInfo("Tunic",Attire,Textiles,72,6,Map.of(Fleece,2)),
            new ItemInfo("Culinary Knife",Sundries,CreatureCreations,44,4,Map.of(Claw,1)),
            new ItemInfo("Brush",Sundries,Woodworks,44,4,Map.of(Fur, 1)),
            new ItemInfo("Boiled Egg",Foodstuffs,CreatureCreations,44,4,Map.of(Egg, 1)),
            new ItemInfo("Hora",Arms,CreatureCreations,72,6,Map.of(Carapace, 2)),
            new ItemInfo("Earrings",Accessories,CreatureCreations,44,4,Map.of(Fang, 1)),
            new ItemInfo("Butter",Ingredients,CreatureCreations,44,4,Map.of(Milk, 1)),
            new ItemInfo("Brick Counter",Furnishings,UnburiedTreasures,48,6,null),
            new ItemInfo("Bronze Sheep",Furnishings,Metalworks,64,8,null),
            new ItemInfo("Growth Formula",Concoctions,Invalid,136,8,Map.of(Alyssum, 2)),
            new ItemInfo("Garnet Rapier",Arms,UnburiedTreasures,136,8,Map.of(Garnet,2)),
            new ItemInfo("Spruce Round Shield",Attire,Woodworks,136,8,Map.of(Spruce,2)),
            new ItemInfo("Shark Oil",Sundries,MarineMerchandise,136,8,Map.of(Shark,2)),
            new ItemInfo("Silver Ear Cuffs",Accessories,Metalworks,136,8,Map.of(Silver,2)),
            new ItemInfo("Sweet Popoto",Confections,Invalid,72,6,Map.of(Popoto, 2, Milk,1)),
            new ItemInfo("Parsnip Salad",Foodstuffs,Invalid,48,4,Map.of(Parsnip,2)),
            new ItemInfo("Caramels",Confections,Invalid,81,6,Map.of(Milk,2)),
            new ItemInfo("Ribbon",Accessories,Textiles,54,6,null),
            new ItemInfo("Rope",Sundries,Textiles,36,4,null),
            new ItemInfo("Cavalier's Hat",Attire,Textiles,81,6,Map.of(Feather,2)),
            new ItemInfo("Horn",Sundries,CreatureCreations,81,6,Map.of(Horn,2)),
            new ItemInfo("Salt Cod",PreservedFood,MarineMerchandise,54,6,null),
            new ItemInfo("Squid Ink",Ingredients,MarineMerchandise,36,4,null),
            new ItemInfo("Essential Draught",Concoctions,MarineMerchandise,54,6,null),
            new ItemInfo("Jam",Ingredients,Invalid,78,6,Map.of(Isleberry,3)),
            new ItemInfo("Tomato Relish",Ingredients,Invalid,52,4,Map.of(Tomato,2)),
            new ItemInfo("Onion Soup",Foodstuffs,Invalid,78,6,Map.of(Onion,3)),
            new ItemInfo("Pie",Confections,MarineMerchandise,78,6,Map.of(Wheat,3)),
            new ItemInfo("Corn Flakes",PreservedFood,Invalid,52,4,Map.of(Corn,2)),
            new ItemInfo("Pickled Radish",PreservedFood,Invalid,104,8,Map.of(Radish,4)),
            new ItemInfo("Iron Axe",Arms,Metalworks,72,8,null),
            new ItemInfo("Quartz Ring",Accessories,UnburiedTreasures,72,8,null),
            new ItemInfo("Porcelain Vase",Sundries,UnburiedTreasures,72,8,null),
            new ItemInfo("Vegetable Juice",Concoctions,Invalid,78,6,Map.of(Cabbage,3)),
            new ItemInfo("Pumpkin Pudding",Confections,Invalid,78,6,Map.of(Pumpkin, 3, Egg, 1, Milk,1)),
            new ItemInfo("Sheepfluff Rug",Furnishings,CreatureCreations,90,6,Map.of(Fleece,3)),
            new ItemInfo("Garden Scythe",Sundries,Metalworks,90,6,Map.of(Claw,3)),
            new ItemInfo("Bed",Furnishings,Textiles,120,8,Map.of(Fur,4)),
            new ItemInfo("Scale Fingers",Attire,CreatureCreations,120,8,Map.of(Carapace,4)),
            new ItemInfo("Crook",Arms,Woodworks,120,8,Map.of(Fang,4))};
    
    public static void main(String[] args)
    {
        ItemInstance[] items = new ItemInstance[]{new ItemInstance(0,High,Cycle7Weak,Sufficient,None),
            new ItemInstance(1,VeryHigh,Cycle6Strong,Sufficient,None),
                    new ItemInstance(2,VeryHigh,Cycle5Strong,Sufficient,None),
                    new ItemInstance(3,Average,Cycle4Strong,Sufficient,None),
                    new ItemInstance(4,Low,Cycle7Strong,Sufficient,None),
                    new ItemInstance(5,Average,Cycle7Weak,Insufficient,Decreasing),
                    new ItemInstance(6,VeryHigh,Cycle4Strong,Sufficient,None),
                    new ItemInstance(7,VeryHigh,Cycle4Weak,Insufficient,Skyrocketing),
                    new ItemInstance(8,Average,Cycle3Strong,Sufficient,None),
                    new ItemInstance(9,Low,Cycle6Weak,Sufficient,None),
                    new ItemInstance(10,Average,Cycle6Strong,Sufficient,None),
                    new ItemInstance(11,Average,Cycle2Weak,Sufficient,None),
                    new ItemInstance(12,Low,Cycle6Strong,Sufficient,None),
                    new ItemInstance(13,High,Cycle5Weak,Sufficient,None),
                    new ItemInstance(14,High,Cycle5Weak,Sufficient,None),
                    new ItemInstance(15,High,Cycle3Weak,Sufficient,None),
                    new ItemInstance(16,High,Cycle7Weak,Insufficient,Decreasing),
                    new ItemInstance(17,High,Cycle7Weak,Insufficient,Decreasing),
                    new ItemInstance(18,VeryHigh,Cycle5Strong,Sufficient,None),
                    new ItemInstance(19,Average,Cycle3Strong,Sufficient,None),
                    new ItemInstance(20,Low,Cycle7Weak,Sufficient,None),
                    new ItemInstance(21,Low,Cycle6Weak,Sufficient,None),
                    new ItemInstance(22,Average,Cycle4Weak,Sufficient,None),
                    new ItemInstance(23,High,Cycle5Weak,Sufficient,None),
                    new ItemInstance(24,VeryHigh,Cycle6Strong,Sufficient,None),
                    new ItemInstance(25,VeryHigh,Cycle3Weak,Sufficient,None),
                    new ItemInstance(26,Average,Cycle3Weak,Sufficient,None),
                    new ItemInstance(27,High,Cycle5Strong,Sufficient,None),
                    new ItemInstance(28,Average,Cycle7Strong,Sufficient,None),
                    new ItemInstance(29,Average,Cycle7Strong,Sufficient,None),
                    new ItemInstance(30,Low,Cycle3Weak,Sufficient,None),
                    new ItemInstance(31,High,Cycle7Strong,Insufficient,Plummeting),
                    new ItemInstance(32,VeryHigh,Cycle2Strong,Sufficient,None),
                    new ItemInstance(33,VeryHigh,Cycle2Strong,Sufficient,None),
                    new ItemInstance(34,High,Cycle2Strong,Insufficient,Skyrocketing),
                    new ItemInstance(35,Average,Cycle5Strong,Insufficient,Skyrocketing),
                    new ItemInstance(36,Average,Cycle3Strong,Sufficient,None),
                    new ItemInstance(37,Average,Cycle4Weak,Sufficient,None),
                    new ItemInstance(38,High,Cycle6Weak,Insufficient,Skyrocketing),
                    new ItemInstance(39,High,Cycle5Weak,Sufficient,None),
                    new ItemInstance(40,VeryHigh,Cycle7Strong,Sufficient,None),
                    new ItemInstance(41,Average,Cycle6Weak,Sufficient,None),
                    new ItemInstance(42,Low,Cycle4Weak,Sufficient,None),
                    new ItemInstance(43,Average,Cycle2Weak,Sufficient,None),
                    new ItemInstance(44,High,Cycle4Strong,Sufficient,None),
                    new ItemInstance(45,High,Cycle2Strong,Sufficient,None),
                    new ItemInstance(46,High,Cycle4Strong,Sufficient,None),
                    new ItemInstance(47,Low,Cycle2Weak,Sufficient,None),
                    new ItemInstance(48,VeryHigh,Cycle2Weak,Sufficient,None),
                    new ItemInstance(49,VeryHigh,Cycle3Strong,Sufficient,None)};
        
        
        System.out.println("\nDay1: ");
        for(ItemInstance item : items)
        {
            System.out.println(item);
        }
        
        /*items[3].addCrafted(2, 0);
        items[7].addCrafted(2, 0);
        items[9].addCrafted(7, 0);
        items[25].addCrafted(8, 0);
        items[38].addCrafted(2, 0);
        
        items[3].addObservedDay(Sufficient, None);
        items[7].addObservedDay(Nonexistent, Skyrocketing);
        items[9].addObservedDay(Sufficient, None);
        items[25].addObservedDay(Sufficient, Skyrocketing);
        items[38].addObservedDay(Nonexistent, Skyrocketing);
        
        System.out.println(items[3]);
        System.out.println(items[7]);
        System.out.println(items[9]);
        System.out.println(items[25]);
        System.out.println(items[38]);*/
        
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
        
        System.out.println("\nDay2: ");
        for(ItemInstance item : items)
        {
            System.out.println(item);
        }

        items[16].addCrafted(3, 1);
        items[34].addCrafted(12, 1);
        items[35].addCrafted(12, 1);
        
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
        

        System.out.println("\nDay3: ");
        for(ItemInstance item : items)
        {
            System.out.println(item);
        }
        
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
        
        
        System.out.println("\nDay4: ");
        for(ItemInstance item : items)
        {
            System.out.println(item);
        }

        
    }
    
    
    public static int getSupplyAfterCraft(ItemInstance craft, int day, int newCrafts)
    {
        return getSupplyOnDay(craft, day) + newCrafts;
    }
    
    public static int getSupplyOnDay(ItemInstance craft, int day)
    {
        int supply = SUPPLY_PATH[craft.peak.ordinal()][0];
        for(int c=1;c < day; c++)
        {
            supply += craft.craftedPerDay[c-1];
            supply += SUPPLY_PATH[craft.peak.ordinal()][c];
        }
        
        return supply;
    }
    public static Supply GetSupplyBucket(int supply)
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
    
    public static int getSupplyOnDayByPeak(PeakCycle peak, int day)
    {
        int supply = SUPPLY_PATH[peak.ordinal()][0];
        for(int c = 1; c<=day; c++)
            supply += SUPPLY_PATH[peak.ordinal()][c];
        
        return supply;
    }
    
    public static DemandShift GetDemandShift(int prevSupply, int newSupply)
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
