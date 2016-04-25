/**
 * @author Shapan Dashore (sdashore).
 * @version 1.4.
 */
import java.util.ArrayList;
import java.util.List;

public class NominalAttributes {
	String name;
    /**
     * list to store every nominal value.
     */
    private List<String> values;

    /**
     * Default constructor
     */
    public NominalAttributes() {
        values = new ArrayList<String>();
    }

    /**
     * Constructor with parameter = name
     * 
     * @param name:
     *            of the Attribute
     */
    public NominalAttributes(String name) {
        this.name = name;
        values = new ArrayList<String>();
    }

    /**
     * @return all sets of values for the Attribute.
     */
    public List<String> getValueSet() {
        return this.values;
    }

    /**
     * Add new value to the attribute
     * 
     * @param value
     *            for this attribute
     */
    public void addValue(String value) {
        this.values.add(value);
    }

    /**
     * @return gets the name of the Attribute.
     */
    
    public String getName() {
        return this.name;
    }

    /**
     * Check if this value is valid for the attribute.
     * 
     * @param value
     *            Check if this value is valid.
     * @return true if this value is valid, otherwise return false.
     */
    public boolean check(String value) {
        if (values.contains(value)) {
            return true;
        } else {
            return false;
        }
    }


}
