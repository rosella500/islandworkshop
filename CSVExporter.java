package islandworkshop;
import java.util.List;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.IOException;

import islandworkshop.WeekSchedule;

public class CSVExporter
{

    private String filename;
    private FileWriter csvfile;
    private String header = "Week,Rest Day,Gross Cowries, Net Cowries, Main Cycle 2,,,,,,Main Cycle 3,,,,,,Main Cycle 4,,,,,,Main Cycle 5,,,,,,Main Cycle 6,,,,,,Main Cycle 7,,,,,, Sub Cycle 2,,,,,,Sub Cycle 3,,,,,,Sub Cycle 4,,,,,,Sub Cycle 5,,,,,,Sub Cycle 6,,,,,,Sub Cycle 7,,,,,,";


    public CSVExporter(String filen)
    {
        try {
            csvfile = new FileWriter(filen,false);
            csvfile.write(header);
            csvfile.write(System.lineSeparator());
        } catch (IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void printCSVweek(WeekSchedule week)
    {
        String toPrint = week.genCSVString();
        
        try {
            csvfile.write(toPrint.substring(0,toPrint.length()-1));
            csvfile.write(System.lineSeparator());
        }catch (IOException e)
            {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
    }
    public void close()
    {
        try {
            csvfile.close();
        }catch (IOException e)
            {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

    }


}