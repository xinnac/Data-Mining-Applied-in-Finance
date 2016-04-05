import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Paranjay on 4/3/16.
 */
public class ProcessData {
    private LinkedList<LinkedList<String>> dataList;
    private LinkedList<String> attributeList;

    ProcessData(String textFile) {
        Scanner scanner = null;
        attributeList = new LinkedList<String>();
        dataList = new LinkedList<LinkedList<String>>();
        File file = new File(textFile);

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
                String newLine = scanner.nextLine();
                String[] var = newLine.split(",");
                LinkedList<String> list = new LinkedList<String>();
                for (int i = 0; i < var.length; i++)
                    list.add(var[i]);
                dataList.add(list);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Not Found.");
        }
    }

    public LinkedList<String> getAttributes() {
        return attributeList;
    }

    public LinkedList<LinkedList<String>> getData() {
        return dataList;
    }
}
