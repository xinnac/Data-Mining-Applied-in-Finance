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

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("@attribute")) {
                    String[] temp = line.split(" ");
                    attributes.add(temp[1].toLowerCase().trim());
                }else if(line.startsWith("@data")){
                    break;
                }

            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] temp = line.split(",");
                ArrayList<String>arr = new ArrayList<String>();
                for(int i=0;i<temp.length;i++)
                    arr.add(temp[i]);
                ret.add(arr);

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
