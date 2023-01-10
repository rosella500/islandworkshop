package islandworkshop;

public enum RareMaterial
{
    Fleece(12),
    Claw(12),
    Fur(12),
    Egg(12),
    Carapace(12),
    Fang(12),
    Milk(12),
    Feather(12),
    Horn(12),
    Garnet(25),
    Alyssum(25),
    Spruce(25),
    Shark(25),
    Silver(25),
    Cabbage(4),
    Pumpkin(4),
    Parsnip(5),
    Popoto(5),
    Isleberry(6),
    Tomato(6),
    Onion(6),
    Wheat(6),
    Corn(6),
    Radish(6),
    Paprika(6),
    Leek(6);
    public final int cowrieValue;
    
    private RareMaterial(int value)
    {
        cowrieValue = value;
    }
}
