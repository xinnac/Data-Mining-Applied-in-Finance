
/**
 * this class is to define the sets of Data and values of the Attributes.
 * @author Shapan Dashore (sdashore).
 * @version 1.6.
 */
import java.util.ArrayList;
import java.util.List;

public class DataGroup {
    /**
     * list of values.
     */
    private List<String> values;

    /**
     * Default constructor
     */
    public DataGroup() {
        this.values = new ArrayList<String>();
    }

    /**
     * constructor with parameter as list of values.
     * 
     * @param values
     */
    public DataGroup(List<String> values) {
        this.values = values;
    }

    /**
     * setter method.
     * 
     * @param values
     *            set values for all attributes
     */
    public void setValueOfAttributes(String[] values) {
        for (int i = 0; i < values.length; i++) {
            this.values.add(values[i]);
        }
    }

    /**
     * getter method to get value at index position.
     * 
     * @param index
     *            is the position of the attribute value.
     * @return the value of attribute.
     */
    public String getValue(int index) {
        if (index < 0) {
            return "";
        } else if (index >= values.size()) {
            return "";
        } else {
            return values.get(index);
        }
    }

    /**
     * @return list of values of this Data Group.
     */
    public List<String> getListOfValues() {
        return values;
    }

}
