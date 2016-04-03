/**
 * Created by xinnacai on 4/3/16.
 */

import com.sun.javafx.collections.MappingChange;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a record vector of one instance in the train set or test set.
 */
public class Record {
    private String type;
    private int lifeStyle;
    private int vocation;
    private int eCredit;
    private double salary;
    private double property;
    private int label;

//    public static enum Type {
//        STUDENT("student"), ENGINEER("engineer"), LIBRARIAN("librarian"),
//        PROFESSOR("professor"), DOCTOR("doctor");
//
//        private final String typeName;
//        private static final HashMap<String,Type> elements= new HashMap<>();
//
//        static {
//            Map<String,Type> elements= new HashMap<>();
//            for(Type value : values()){
//                elements.put(value.toString().toLowerCase(),value);
//            }
//        }
//
//        Type (String typeName) {
//            this.typeName = typeName;
//        }
//        public String typeName() {return typeName;}
//
//        public static Type elementsOf(String typeName){
//            return elements.get(typeName);
//        }
//    }

    public Record(String type, byte lifeStyle, int vocation, int eCredit, double salary, double property, byte label) {
        this.type = type;
        this.lifeStyle = lifeStyle;
        this.vocation = vocation;
        this.eCredit = eCredit;
        this.salary = salary;
        this.property = property;
        this.label = label;

    }

    public Record() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(int lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public int getVocation() {
        return vocation;
    }

    public void setVocation(int vocation) {
        this.vocation = vocation;
    }

    public int geteCredit() {
        return eCredit;
    }

    public void seteCredit(int eCredit) {
        this.eCredit = eCredit;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getProperty() {
        return property;
    }

    public void setProperty(double property) {
        this.property = property;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

}