package islandworkshop; 

import java.util.List;
import java.util.ArrayList;
import islandworkshop.Item;

public class WeekSchedule{
    int week;
    int grossCowries;
    int netCowries;
    int restDay;

    List<List<Item>> mainCrafts = new ArrayList<List<Item>>(6);
    List<List<Item>> subCrafts = new ArrayList<List<Item>>(6);

    public WeekSchedule()
    {
        for (int i=0;i<6;i++)
        {
            mainCrafts.add(new ArrayList<>());
            subCrafts.add(new ArrayList<>());
        }
    }


    public void calcRestDay()
    {
        for (int i=0;i<6;i++){
            if( mainCrafts.get(i).isEmpty())
                restDay = i+2;
        }
    }

    public void setMetaData(int wek, int gross, int net){
        week=wek;
        grossCowries=gross;
        netCowries=net;
    }

    public void setCycle(int cycle, List<Item> main,List<Item> sub){
        mainCrafts.get(cycle).addAll(main);
        subCrafts.get(cycle).addAll(sub);

    }

    public void clear()
    {
        for (int i=0;i<6;i++)
        {
            mainCrafts.get(i).clear();
            subCrafts.get(i).clear();
        }
    }

    public String genCSVString()
    {
        String toPrint = week+",";
        calcRestDay();
        toPrint+=restDay+",";
        toPrint+=grossCowries+",";
        toPrint+=netCowries+",";
        for(int i=0;i<6;i++)
        {
            for (int j=0;j<6;j++){
                if (mainCrafts.get(i).size() > j){
                    toPrint+=mainCrafts.get(i).get(j)+",";
                }
                else
                {
                    toPrint+=",";
                }
            }
        }
        for(int i=0;i<6;i++)
        {
            for (int j=0;j<6;j++){
                if (subCrafts.get(i).size() > j){
                    toPrint+=subCrafts.get(i).get(j)+",";
                }
                else
                {
                    toPrint+=",";
                }
            }
        }

        return(toPrint);

    }

}
