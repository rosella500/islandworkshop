package islandworkshop;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BruteForceChains
{
    
    public static List<List<Item>> allEfficientChains;
    
    public static void init()
    {
        allEfficientChains = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("AllEfficientChains.csv"))) 
        {
            String line;
            while ((line = br.readLine()) != null) 
            {
                String[] values = line.split(",");
                List<Item> items = new ArrayList<>();
                for(String itemStr : values)
                {
                    itemStr = itemStr.replace(" ", "");
                    itemStr = itemStr.replace("'", "");
                    itemStr = itemStr.replace("Islefish", "");
                    itemStr = itemStr.replace("Isleberry", "");
                    
                    if(!itemStr.isBlank())
                        items.add(Item.valueOf(itemStr));
                    
                }
                allEfficientChains.add(items);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error importing csv: "+e.getMessage());
        }
    }
}