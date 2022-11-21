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



    public static PeakCycle fromString(String str)
    {
        PeakCycle peakEnum = Unknown;
        switch(str)
        {
            case "2S":
                peakEnum = PeakCycle.Cycle2Strong;
                break;
            case "2W":
            case "2U":
                peakEnum = PeakCycle.Cycle2Weak;
                break;
            case "3S":
                peakEnum = PeakCycle.Cycle3Strong;
                break;
            case "3W":
                peakEnum = PeakCycle.Cycle3Weak;
                break;
            case "4S":
                peakEnum = PeakCycle.Cycle4Strong;
                break;
            case "4W":
                peakEnum = PeakCycle.Cycle4Weak;
                break;
            case "5S":
                peakEnum = PeakCycle.Cycle5Strong;
                break;
            case "5W":
                peakEnum = PeakCycle.Cycle5Weak;
                break;
            case "6S":
                peakEnum = PeakCycle.Cycle6Strong;
                break;
            case "6W":
                peakEnum = PeakCycle.Cycle6Weak;
                break;
            case "7S":
                peakEnum = PeakCycle.Cycle7Strong;
                break;
            case "7W":
                peakEnum = PeakCycle.Cycle7Weak;
                break;
            case "45":
                peakEnum = PeakCycle.Cycle45;
                break;
            case "5U":
                peakEnum = PeakCycle.Cycle5;
                break;
            case "67":
                peakEnum = PeakCycle.Cycle67;
                break;
        }
        return peakEnum;
    }
    
    public String toDisplayName()
    {
        switch(this)
        {
        case Cycle2Strong:
            return "Cycle 2 Strong";
        case Cycle2Weak:
            return "Cycle 2 Weak";
        case Cycle3Strong:
            return "Cycle 3 Strong";
        case Cycle3Weak:
            return "Cycle 3 Weak";
        case Cycle4Strong:
            return "Cycle 4 Strong";
        case Cycle4Weak:
            return "Cycle 4 Weak";
        case Cycle5Strong:
            return "Cycle 5 Strong";
        case Cycle5Weak:
            return "Cycle 5 Weak";
        case Cycle6Strong:
            return "Cycle 6 Strong";
        case Cycle6Weak:
            return "Cycle 6 Weak";
        case Cycle7Strong:
            return "Cycle 7 Strong";
        case Cycle7Weak:
            return "Cycle 7 Weak";
        case Unknown:
        case Cycle67:
            return "Cycle 6/7";
        case Cycle45: 
            return "Cycle 4/5";
        case Cycle5:
            return "Cycle 5";
        default:
            return super.toString();
        
        }
    }
}
