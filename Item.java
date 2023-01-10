package islandworkshop;

public enum Item
{
    Potion("Potion"),
    Firesand("Firesand"),
    WoodenChair("Wooden Chair"),
    GrilledClam("Grilled Clam"),
    Necklace("Necklace"),
    CoralRing("Coral Ring"),
    Barbut("Barbut"),
    Macuahuitl("Macuahuitl"),
    Sauerkraut("Sauerkraut"),
    BakedPumpkin("Baked Pumpkin"),
    Tunic("Tunic"),
    CulinaryKnife("Culinary Knife"),
    Brush("Brush"),
    BoiledEgg("Boiled Egg"),
    Hora("Hora"),
    Earrings("Earrings"),
    Butter("Butter"),
    BrickCounter("Brick Counter"),
    BronzeSheep("Bronze Sheep"),
    GrowthFormula("Growth Formula"),
    GarnetRapier("Garnet Rapier"),
    SpruceRoundShield("Spruce Round Shield"),
    SharkOil("Shark Oil"),
    SilverEarCuffs("Silver Ear Cuffs"),
    SweetPopoto("Sweet Popoto"),
    ParsnipSalad("Parsnip Salad"),
    Caramels("Caramels"),
    Ribbon("Ribbon"),
    Rope("Rope"),
    CavaliersHat("Cavalier's Hat"),
    Horn("Horn"),
    SaltCod("Salt Cod"),
    SquidInk("Squid Ink"),
    EssentialDraught("Essential Draught"),
    Jam("Jam"),
    TomatoRelish("Tomato Relish"),
    OnionSoup("Onion Soup"),
    Pie("Pie"),
    CornFlakes("Corn Flakes"),
    PickledRadish("Pickled Radish"),
    IronAxe("Iron Axe"),
    QuartzRing("Quartz Ring"),
    PorcelainVase("Porcelain Vase"),
    VegetableJuice("Vegetable Juice"),
    PumpkinPudding("Pumpkin Pudding"),
    SheepfluffRug("Sheepfluff Rug"),
    GardenScythe("Garden Scythe"),
    Bed("Bed"),
    ScaleFingers("Scale Fingers"),
    Crook("Crook"),
    CoralSword("Coral Sword"),
    CoconutJuice("Coconut Juice"),
    Honey("Honey"),
    SeashineOpal("Seashine Opal"),
    DriedFlowers("Dried Flowers"),
    PowderedPaprika("Powdered Paprika"),
    CawlCennin("Cawl Cennin"),
    Isloaf("Isloaf"),
    PopotoSalad("Popoto Salad"),
    Dressing("Dressing");
    
    private String displayName;
    private Item(String display)
    {
        displayName = display;
    }
    
    public String toString()
    {
        return displayName;
    }
}
