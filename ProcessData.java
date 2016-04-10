import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Paranjay on 4/3/16.
 */
public class ProcessData {
    private ArrayList<String> attributes = new ArrayList<>();
    private ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();

    /**
     * read the instance data from file.
     * @param filename the file name
     */
    ProcessData(String filename) {
        File file = new File(filename);

        try {
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("@attribute")) {
                    String[] array = line.split(" ");
                    attributes.add(array[1].toLowerCase().trim());
                    continue;
                } else if (line.startsWith("@data")) {
                    continue;
                } else if (line.equals("")) {
                    continue;
                }
                String[] value = line.split(",");
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < value.length; i++) {
                    list.add(value[i].toLowerCase().trim());
                }
                ret.add(list);
            }
            scanner.close();

        } catch (Exception e) {
            System.out.println("Can not find the file");
        }
    }

    /**
     * get the instance data.
     * @return instance data of train or test
     */
    public ArrayList<ArrayList<String>> getRet(){
        return ret;
    }

    /**
     * get the attribute name.
     * @return the list storing the attribute name
     */
    public ArrayList<String> getAttributes(){
        return attributes;
    }



}
