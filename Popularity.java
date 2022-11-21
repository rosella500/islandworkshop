package islandworkshop;

public enum Popularity
{
    VeryHigh(140), High(120), Average(100), Low(80);
    
    public final int multiplier;
    
    private Popularity(int mult)
    {
        this.multiplier = mult;
    }

    public static Popularity fromIndex(int index)
    {
        switch (index)
        {
            case 1:
                return VeryHigh;
            case 2:
                return High;
            case 3:
                return Average;
            case 4:
                return Low;
        }
        return Low;
    }
}