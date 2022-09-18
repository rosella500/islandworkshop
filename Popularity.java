package islandworkshop;

public enum Popularity
{
    VeryHigh(1.4), High(1.2), Average(1), Low(.8);
    
    public final double multiplier;
    
    private Popularity(double mult)
    {
        this.multiplier = mult;
    }
}