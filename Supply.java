package islandworkshop;

public enum Supply
{
    Overflowing (.6), Surplus(.8), Sufficient(1), Insufficient(1.3), Nonexistent(1.6);
    
    public final double multiplier;
    
    private Supply(double mult)
    {
        this.multiplier = mult;
    }
}



