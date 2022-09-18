package islandworkshop;
import java.util.Map;
import static islandworkshop.ItemCategory.*;

public class ItemInfo
{
    private String name;
    private int baseValue;
    private ItemCategory category1;
    private ItemCategory category2;
    private int time;
    private Map<RareMaterial, Integer> materialsRequired;
    private int materialValue;
    
    public ItemInfo(String n, ItemCategory cat1, ItemCategory cat2, int value, int hours, Map<RareMaterial,Integer> mats)
    {
        name = n;
        baseValue = value;
        category1 = cat1;
        category2 = cat2;
        time = hours;
        materialsRequired = mats;
        materialValue = 0;
        
        if(mats != null)
            materialsRequired.forEach((k, v) -> {materialValue+=k.cowrieValue * v;});
        
    }

    public String getName()
    {
        return name;
    }

    public int getBaseValue()
    {
        return baseValue;
    }

    public ItemCategory getCategory1()
    {
        return category1;
    }

    public ItemCategory getCategory2()
    {
        return category2;
    }

    public int getTime()
    {
        return time;
    }

    public Map<RareMaterial, Integer> getMaterialsRequired()
    {
        return materialsRequired;
    }

    public int getMaterialValue()
    {
        return materialValue;
    } 
    
    public boolean doCategoriesMatch(ItemCategory other1, ItemCategory other2)
    {
        return (other1!=Invalid && (other1 == category1 || other1 == category2)) || (other2!=Invalid && (other2 == category1 || other2 == category2));
    }
    
}
