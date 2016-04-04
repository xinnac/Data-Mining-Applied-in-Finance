import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by xinnacai on 4/3/16.
 */
public class ProcessData {
    private ArrayList<String> attributes;
    private ArrayList<ArrayList<String>> ret;
    ProcessData (String filename) {
        Scanner scanner = null;
        attributes = new ArrayList<String>();
        ret = new ArrayList<ArrayList<String>>();
        File file = new File(filename);

        try {
            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("@attribute")) {
                    String[] temp = line.split(" ");
                    attributes.add(temp[1].toLowerCase().trim());
                }else if(line.startsWith("@data")){
                    break;
                }

            }
            // handle @data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] temp = line.split(",");
                ArrayList<String>arr = new ArrayList<String>();
                for(int i=0;i<temp.length;i++)
                    arr.add(temp[i]);
                ret.add(arr);

            }



        } catch (Exception e) {
            System.out.println("Can not find the file");
        }
    }

    // return the data extracting from the file
    public ArrayList<ArrayList<String>> getData(){
        return ret;
    }

    public ArrayList<String> getAttributes(){
        return attributes;
    }


}
