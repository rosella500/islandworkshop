package islandworkshop;

public enum Supply
{
    Overflowing (60), Surplus(80), Sufficient(100), Insufficient(130), Nonexistent(160);
    
    public final int multiplier;
    
    private Supply(int mult)
    {
        this.multiplier = mult;
    }
}



