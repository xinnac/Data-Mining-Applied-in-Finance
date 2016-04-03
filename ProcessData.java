import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by xinnacai on 4/3/16.
 */
public class ProcessData {
    private ArrayList<Record> dataSet = new ArrayList<>();

    public ProcessData(String fileName) {
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            String line = null;
            // read record data line by line
            while ((line = scanner.nextLine()) != null) {
                if(line.startsWith("@relation")) continue;
                if(line.equals("")) continue;
                if(line.startsWith("@attribute")) continue;
                if(line.startsWith("@data")) continue;
                String[] vector = line.split(",");
                if(vector.length != 7) continue;
                Record r = new Record();
//                if (isValidType(vector[0])) {
//                    Record.Type type = Record.Type.elementsOf(vector[0]);
//                    r.setType(type);
//                } else {
//                    continue;
//                }
                String type = vector[0];
                if(type.equalsIgnoreCase("student")) {
                    r.setType(Constants.TYPE_STUDENT);
                } else if(type.equalsIgnoreCase("engineer")) {
                    r.setType(Constants.TYPE_ENGINEER);
                } else if(type.equalsIgnoreCase("engineer")) {
                    r.setType(Constants.TYPE_ENGINEER);
                } else if(type.equalsIgnoreCase("librarian")) {
                    r.setType(Constants.TYPE_LIBRARIAN);
                } else if(type.equalsIgnoreCase("professor")) {
                    r.setType(Constants.TYPE_PROFESSOR);
                } else if(type.equalsIgnoreCase("doctor")) {
                    r.setType(Constants.TYPE_DOCTOR);
                } else {
                    continue;
                }
                String style = vector[1];
                if (style.equalsIgnoreCase("spend>saving")) {
                    r.setLifeStyle(Constants.SPEND_MORE);
                } else if (style.equalsIgnoreCase("spend<saving")) {
                    r.setLifeStyle(Constants.SPEND_LESS);
                } else if (style.equalsIgnoreCase("spend>>saving")) {
                    r.setLifeStyle(Constants.SPEND_MUCH_MORE);
                } else if (style.equalsIgnoreCase("spend<<saving")) {
                    r.setLifeStyle(Constants.SPEND_MUCH_LESS);
                } else {
                    continue;
                }
                if (isValidVocation(vector[2])) {
                    r.setVocation(Integer.valueOf(vector[2]));
                } else {
                    continue;
                }
                if (isValidEcredit(vector[3])) {
                    r.seteCredit(Integer.valueOf(vector[3]));
                } else {
                    continue;
                }
                if (isValidSalary(vector[4])) {
                    r.setSalary(Double.parseDouble(vector[4]));
                } else {
                    continue;
                }
                if (isValidProperty(vector[5])) {
                    r.setProperty(Double.parseDouble(vector[5]));
                } else {
                    continue;
                }
                String label = vector[6];
                if (label.equalsIgnoreCase("C1")) {
                    r.setLabel(Constants.C1);
                } else if (label.equalsIgnoreCase("C2")) {
                    r.setLabel(Constants.C2);
                } else if (label.equalsIgnoreCase("C3")) {
                    r.setLabel(Constants.C3);
                } else if (label.equalsIgnoreCase("C4")) {
                    r.setLabel(Constants.C4);
                } else if (label.equalsIgnoreCase("C5")) {
                    r.setLabel(Constants.C5);
                } else {
                    continue;
                }
                dataSet.add(r);
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

//    static public boolean isValidType(String typeName) {
//        if (typeName == null || typeName.equals("")) {
//            return false;
//        }
//        Record.Type[] types = Record.Type.values();
//        for (Record.Type type : types) {
//            if (type.typeName().equalsIgnoreCase(typeName.trim())) {
//                return true;
//            }
//        }
//        return false;
//    }

    static public boolean isValidVocation(String vocation) {
        if (vocation == null || vocation.equals("")) {
            return false;
        }
        try {
            int v = Integer.valueOf(vocation);
            if (v < 0 || v > 60) return false;
            else return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static public boolean isValidEcredit(String eCredit) {
        if (eCredit == null || eCredit.equals("")) {
            return false;
        }
        try {
            int c = Integer.valueOf(eCredit);
            if (c < 5 || c > 3000) return false;
            else return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static public boolean isValidSalary(String salary) {
        if (salary == null || salary.equals("")) {
            return false;
        }
        try {
            double s = Double.parseDouble(salary);
            if (s < 10.0 || s > 40.0) return false;
            else return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    static public boolean isValidProperty(String property) {
        if (property == null || property.equals("")) {
            return false;
        }
        try {
            double p = Double.parseDouble(property);
            if (p < 0.00 || p > 20.00) return false;
            else return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ArrayList<Record> getDataSet (){
        return this.dataSet;
    }

    public void printDataSet() {
        int size = dataSet.size();
        System.out.println(size);
        for(int i =0;i<size;i++){
            Record r = dataSet.get(i);
            System.out.println(i+1+" "+r.getType()+","+r.getLifeStyle()+","+r.getVocation()+","+r.geteCredit()+
            ","+r.getSalary()+","+r.getProperty()+","+r.getLabel());
        }
    }

    public static void main(String[] args) {
        ProcessData pd = new ProcessData(args[0]);
        pd.printDataSet();
    }


}
