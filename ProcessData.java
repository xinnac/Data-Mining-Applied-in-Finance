import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Paranjay on 4/3/16.
 */
public class ProcessData {
    private ArrayList<ArrayList<String>> dataList;
    private ArrayList<String> attributeList;

    ProcessData(String filename) {
        Scanner scanner = null;
        attributeList = new ArrayList<String>();
        dataList = new ArrayList<ArrayList<String>>();
        File file = new File(filename);

        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String newLine = scanner.nextLine();
                if (newLine.startsWith("@data")) {
                    break;
                }
                if (newLine.startsWith("@attribute")) {
                    String[] var = newLine.split(" ");
                    attributeList.add(var[1].trim().toLowerCase());
                }
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] var = line.split(",");
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < var.length; i++)
                    list.add(var[i]);
                dataList.add(list);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Not Found.");
        }
    }

    public ArrayList<String> getAttributes() {
        return attributeList;
    }

    public ArrayList<ArrayList<String>> getData() {
        return dataList;
    }
}
