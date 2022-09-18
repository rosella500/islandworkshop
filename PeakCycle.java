package islandworkshop;

public enum PeakCycle
{
    Unknown (false, false),
    Cycle2Weak (false, true),
    Cycle2Strong (true, true),
    Cycle3Weak (false, true),
    Cycle3Strong (true, true),
    Cycle4Weak (false, true),
    Cycle4Strong (true, true),
    Cycle5Weak(false, true),
    Cycle5Strong (true, true),
    Cycle6Weak (false, true),
    Cycle6Strong (true, true),
    Cycle7Weak (true, true),
    Cycle7Strong (false, true),
    Cycle45 (false, false),
    Cycle5 (false, false),
    Cycle67 (false, false);
    
    public final boolean isReliable;
    public final boolean isTerminal;
    
    private PeakCycle(boolean reliable, boolean terminal)
    {
        isReliable = reliable;
        isTerminal = terminal;
    }
}
