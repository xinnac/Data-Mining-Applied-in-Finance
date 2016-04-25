/**
 * @author Shapan Dashore (sdashore).
 * @version 1.2.
 */
public class NumericAttributes {
	String name;
    /**
     * Default constructor
     */
    public NumericAttributes() {
    }

    /**
     * Constructor with parameter: name
     * 
     * @param name
     *            : name of this attribute
     */
    public NumericAttributes(String name) {
        this.name = name;
    }

    public String getName() {
		
		return this.name;
	}

	/**
     * Implements abstract: verify if this value is valid
     * 
     * @param value
     *            : value of this attribute
     * @return true if this value is a double, otherwise return false
     */
    public boolean check(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Given the value and splitValue, return the name of its child
     * 
     * @param recValue:
     *            record's value on this attribute
     * @param splitValue:
     *            split value on this attribute
     * @return when recValue is smaller than splitValue it will return "lower"
     *         otherwise it will return "greater"
     */
    public String getNodeChild(String recValue, double splitValue) {
        if (Double.parseDouble(recValue) <= splitValue) {
            return "lower";
        } else {
            return "higher";
        }
    }
}
