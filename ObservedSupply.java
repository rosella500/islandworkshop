package islandworkshop;

public class ObservedSupply
{
    public Supply supply;
    public DemandShift demandShift;
    
    public ObservedSupply(Supply supp, DemandShift demand)
    {
        supply = supp;
        demandShift = demand;
    }
    
    public boolean equals(ObservedSupply other)
    {
        return other.supply == this.supply && other.demandShift == this.demandShift;
    }
    
    public String toString()
    {
        return supply+" - "+demandShift;
    }
}
