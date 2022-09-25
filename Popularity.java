package islandworkshop;

public enum Popularity
{
    VeryHigh(140), High(120), Average(100), Low(80);
    
    public final int multiplier;
    
    private Popularity(int mult)
    {
        this.multiplier = mult;
    }
}