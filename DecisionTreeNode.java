
/**
 * @author Shapan Dashore (sdashore).
 * @version 3.2.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DecisionTreeNode {
    /**
     * list of indexes of the Nodes.
     */
    private List<Integer> indexOfNodes;
    /**
     * label attribute of the leaf Node.
     */
    private String label;

    /**
     * mapped child Node.
     */
    private HashMap<String, DecisionTreeNode> childNodes;
    /**
     * level of Node from the Root.
     */
    private int level;

    /**
     * Label index of Attribute.
     */
    private int labelIndex;
    /**
     * Node Entropy.
     */
    private double entropy;
    /**
     * Map with Label name and its frequency in Node.
     */
    private HashMap<String, Integer> labelCount = null;
    /**
     * list of attributes.
     */
    private List<Object> abstractAttributes;
    /**
     * index of nest split Attributed.
     */
    private int SplitIndex;
    /**
     * value of next Split Attribute.
     */
    private double nextSplitValue;
    /**
     * threshold to halt splitting.
     */
    private double threshhold = 0.0;
    /**
     * list of Data Group.
     */
    private List<DataGroup> dataGroup;

    /**
     * Constructor with parameters
     * 
     * @param dataGroup
     *            Set of Data.
     * @param labelIndex
     *            Label index of Attribute.
     * @param abstractAttributes
     *            abstract atrribute contains both Nom and Num Attributes.
     * @param level
     *            level of the Node.
     * @param threshhold
     *            Threshold value.
     */
    public DecisionTreeNode(List<DataGroup> dataGroup, int labelIndex, List<Object> abstractAttributes, int level,
            double threshhold) {

        this.indexOfNodes = new ArrayList<Integer>();
        this.dataGroup = dataGroup;
        this.labelIndex = labelIndex;
        this.entropy = 0.0;
        this.abstractAttributes = abstractAttributes;
        this.level = level;
        this.childNodes = null;
        this.threshhold = threshhold;
    }

    /**
     * add label index into this node
     * 
     * @param allIndex
     */
    public void addAllLabelIndex(List<Integer> allIndex) {
        indexOfNodes.addAll(allIndex);
    }

    /**
     * @return index of label attribute in the attribute list.
     */
    public int getLabelIndex() {
        return this.labelIndex;
    }

    /**
     * counting the labels in the Node and will update the label.
     */
    public void labelCountUpdate() {
        if (this.labelCount == null || this.labelCount.size() == 0) {
            labelCount = new HashMap<String, Integer>();
            for (int i : this.indexOfNodes) {
                String curClass = this.dataGroup.get(i).getValue(labelIndex);

                if (!labelCount.containsKey(curClass)) {

                    labelCount.put(curClass, 1);
                } else {

                    labelCount.put(curClass, labelCount.get(curClass) + 1);
                }
            }
        }
    }

    /**
     * Splitting is done here, when entropy is smaller than the threshold value.
     * All possible Split values are iterated over. Till lower value of entropy
     * is found.
     * 
     */
    public void split() {

        labelCountUpdate();
        double entropy = 0.0;
        int totCount = indexOfNodes.size();
        for (Integer i : labelCount.values()) {
            double ratio = i * 1.0 / totCount;
            entropy += -1.0 * ratio * Math.log(ratio);
        }
        this.entropy = entropy;

        if (this.entropy > this.threshhold && this.getAllDataIndex().size() > 1) {
            // going to split
            this.SplitIndex = -1;
            this.nextSplitValue = 0.0;
            double minEntropy = Double.MAX_VALUE;
            for (int attrIndex = 0; attrIndex < abstractAttributes.size(); attrIndex++) {
                if (attrIndex == this.getLabelIndex()) {
                    continue;
                }
                double[] toSplit = this.getSplittedEntropy(attrIndex);
                if (toSplit[0] < minEntropy) {
                    minEntropy = toSplit[0];
                    this.nextSplitValue = toSplit[1];
                    this.SplitIndex = attrIndex;
                }
            }
            splitAttribute(this.SplitIndex, this.nextSplitValue);
            for (DecisionTreeNode child : this.childNodes.values()) {
                child.split();
            }
        }

    }

    /**
     * Attributes Split based on the split value.
     * 
     * @param AttributesplitIndex
     *            value of next Split Attribute.
     * @param nextSplitValue
     *            Value of next Split.
     */
    private void splitAttribute(int AttributesplitIndex, double nextSplitValue) {
        Object spAttribute = abstractAttributes.get(AttributesplitIndex);
        this.childNodes = new HashMap<String, DecisionTreeNode>();
        if (spAttribute instanceof NumericAttributes) {
            // split on numeric attribute : binary split (only on 2 children)
            numericAttributeSplit(AttributesplitIndex, nextSplitValue);
        } else {
            // split on nominal abstractAttributes: multiple children
            splitOnNomAttribute(AttributesplitIndex);
        }
    }

    /**
     * split given numeric attribute based on given split value
     * 
     * @param AttributesplitIndex
     *            value of next Split Attribute.
     * @param nextSplitValue
     *            Value of next Split.
     */
    private void numericAttributeSplit(int AttributesplitIndex, double nextSplitValue) {
        for (int i : this.getAllDataIndex()) {
            DataGroup curRecord = this.getDataSet().get(i);

            double curValue = Double.valueOf(curRecord.getValue(AttributesplitIndex));

            if (curValue <= nextSplitValue) {

                if (!this.childNodes.containsKey("lower")) {

                    DecisionTreeNode leftChild = new DecisionTreeNode(this.getDataSet(), this.getLabelIndex(),
                            abstractAttributes, this.level + 1, this.threshhold);

                    leftChild.addDataGroup(i);

                    childNodes.put("lower", leftChild);
                } else {
                    childNodes.get("lower").addDataGroup(i);
                }
            } else {
                if (!this.childNodes.containsKey("higher")) {
                    DecisionTreeNode rightChild = new DecisionTreeNode(this.getDataSet(), this.getLabelIndex(),
                            abstractAttributes, this.level + 1, this.threshhold);
                    rightChild.addDataGroup(i);

                    childNodes.put("higher", rightChild);
                } else {
                    childNodes.get("higher").addDataGroup(i);
                }
            }
        }
    }

    /**
     * Split on nominal attribute.
     * 
     * @param splitIndex
     *            next split attribute index.
     */
    private void splitOnNomAttribute(int splitIndex) {
        for (int i : this.getAllDataIndex()) {
            DataGroup current = this.getDataSet().get(i);
            String value = current.getValue(splitIndex);
            if (childNodes.containsKey(value)) {
                childNodes.get(value).addDataGroup(i);
            } else {
                DecisionTreeNode insert = new DecisionTreeNode(this.getDataSet(), this.getLabelIndex(),
                        abstractAttributes, this.level + 1, threshhold);
                insert.addDataGroup(i);

                childNodes.put(value, insert);
            }
        }

    }

    /**
     * @return child nodes of the : Map<String, DecisionTreeNode>
     */
    public HashMap<String, DecisionTreeNode> getChildNodes() {
        return this.childNodes;
    }

    /**
     * get value of entropy depending on the attribute.
     * 
     * @param attrIndex
     *            index of the attribute to split
     * @return
     */
    private double[] getSplittedEntropy(int attrIndex) {

        if (abstractAttributes.get(attrIndex).getClass().equals(NominalAttributes.class)) {

            return nominalAttributeEntropyAfterSplit(attrIndex);
        } else {

            return numericAttributeEntropyAfterSplit(attrIndex);
        }
    }

    /**
     * @return hashmap contains the label name and its count.
     */
    private HashMap<String, Integer> getIndexOfLabels() {
        HashMap<String, Integer> indexOfLabels = new HashMap<String, Integer>();
        int index = 0;
        for (String className : this.getLabelCount().keySet()) {
            if (!indexOfLabels.containsKey(className)) {
                indexOfLabels.put(className, index++);
            }
        }
        return indexOfLabels;
    }

    /**
     * Split based on numeric attribute.
     * 
     * @param index
     *            index of the attribute.
     * @return result
     */
    private double[] numericAttributeEntropyAfterSplit(int index) {

        HashMap<Double, int[]> labelCountOfValue = new HashMap<Double, int[]>();

        double[] resultString = new double[2];

        List<Double> sortedValue = new ArrayList<Double>();

        updateSortedList(index, labelCountOfValue, sortedValue);

        HashMap<String, Integer> countLabel = this.getLabelCount();

        int[] left = new int[countLabel.keySet().size()];

        int[] right = new int[countLabel.keySet().size()];

        for (int i = 0; i < sortedValue.size(); i++) {

            double curValue = sortedValue.get(i);

            for (int j = 0; j < labelCountOfValue.get(curValue).length; j++) {
                right[j] += labelCountOfValue.get(curValue)[j];
            }
        }

        double mininumEntropy = Double.MAX_VALUE;

        double splitValue = Double.MAX_VALUE;

        for (int i = 0; i < sortedValue.size() - 1; i++) {
            double curValue = sortedValue.get(i);
            int[] labelDistribution = labelCountOfValue.get(curValue);

            for (int j = 0; j < labelDistribution.length; j++) {
                left[j] += labelDistribution[j];
                right[j] -= labelDistribution[j];
            }

            int leftSum = 0;
            int rightSum = 0;
            for (int j = 0; j < labelDistribution.length; j++) {
                leftSum += left[j];
                rightSum += right[j];
            }

            double lowerRatio = (i + 1) * 1.0 / sortedValue.size();
            double gainRatio = -1.0 * lowerRatio * Math.log(lowerRatio) - (1 - lowerRatio) * Math.log(1 - lowerRatio);

            double leftBranchEntropy = 0;

            double rightBranchEntropy = 0;

            for (int j = 0; j < labelDistribution.length; j++) {
                if (left[j] > 0) {
                    double leftProportion = left[j] * 1.0 / leftSum;
                    leftBranchEntropy -= leftProportion * Math.log(leftProportion);
                }
                if (right[j] > 0) {
                    double rightProportion = right[j] * 1.0 / rightSum;
                    rightBranchEntropy -= rightProportion * Math.log(rightProportion);
                }
            }
            double entropy = leftBranchEntropy * leftSum / (leftSum + rightSum)
                    + rightBranchEntropy * rightSum / (leftSum + rightSum);
            entropy /= gainRatio;

            if (entropy < mininumEntropy) {
                mininumEntropy = entropy;
                splitValue = curValue;
            }
        }

        resultString[0] = mininumEntropy;
        resultString[1] = splitValue;
        return resultString;
    }

    /**
     * @return index of all Data in Node.
     */
    public List<Integer> getAllDataIndex() {
        return this.indexOfNodes;
    }

    /**
     * @return get all data set in node.
     */
    public List<DataGroup> getDataSet() {
        return this.dataGroup;
    }

    /**
     * Split based on nominal attribute
     * 
     * @param i
     *            : index of the attribute
     * @return : an double array: double[2] [0] = minimal entropy, [1] =
     *         bestSplitValue
     * 
     */
    private double[] nominalAttributeEntropyAfterSplit(int i) {
        double[] res = new double[2];
        HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        // for gainRatio calculation
        HashMap<String, Integer> distribution = new HashMap<String, Integer>();

        List<DataGroup> dataSet = this.getDataSet();
        for (Integer dataIndex : this.getAllDataIndex()) {
            String curAttribute = dataSet.get(dataIndex).getValue(i);
            if (map.containsKey(curAttribute)) {
                map.get(curAttribute).add(dataIndex);
            } else {
                List<Integer> list = new ArrayList<Integer>();
                list.add(dataIndex);
                map.put(curAttribute, list);
            }

            if (distribution.containsKey(curAttribute)) {
                distribution.put(curAttribute, distribution.get(curAttribute) + 1);
            } else {
                distribution.put(curAttribute, 1);
            }
        }
        double entropy = 0.0;
        for (List<Integer> list : map.values()) {
            entropy += calculateEntropy(list) * list.size() / this.getAllDataIndex().size();
        }
        double gainRatio = 0;
        for (String a : distribution.keySet()) {
            double curRatio = distribution.get(a) * 1.0 / this.getAllDataIndex().size();
            gainRatio = -1.0 * curRatio * Math.log(curRatio);
        }

        if (gainRatio > 0) {
            entropy /= gainRatio;
        } else {
            entropy = Double.MAX_VALUE;
        }

        res[0] = entropy;
        res[1] = 0;
        return res;
    }

    /**
     * calculate entropy for the root node
     * 
     * @param list
     *            list of Data Group in this node
     * @return entropy of the root node in this Data group.
     */
    private double calculateEntropy(List<Integer> list) {
        double entropy = 0.0;
        // first get the distribution of classes for given list
        HashMap<String, Integer> map = getClassCount(list);
        int totalCounts = list.size();

        for (Integer i : map.values()) {
            double ratio = i * 1.0 / totalCounts;
            entropy += -1.0 * ratio * Math.log(ratio);
        }
        return entropy;
    }

    /**
     * @return a hashmap of label name and its counts.
     */
    public HashMap<String, Integer> getLabelCount() {
        if (this.labelCount == null || this.labelCount.size() == 0) {
            this.labelCountUpdate();
        }
        return labelCount;
    }

    /**
     * Label prediction of the Data Group on basis of decision tree.
     * 
     * @param dSet
     *            data set to be labeled.
     * @return label of the data set.
     */
    public String prediction(DataGroup dSet) {
        if (this.childNodes == null) {
            if (label == null) {
                int maximumCount = 0;
                labelCountUpdate();
                for (String name : this.labelCount.keySet()) {
                    if (this.labelCount.get(name) > maximumCount) {
                        maximumCount = this.labelCount.get(name);
                        this.label = name;
                    }
                }
            }
            return this.label;
        } else {
            Object tmp = abstractAttributes.get(this.SplitIndex);
            String childName;
            if (tmp instanceof NumericAttributes) {
                childName = ((NumericAttributes) tmp).getNodeChild(dSet.getValue(this.SplitIndex), this.nextSplitValue);
            } else {
                childName = dSet.getValue(this.SplitIndex);
            }

            if (this.childNodes.containsKey(childName)) {
                return this.childNodes.get(childName).prediction(dSet);
            } else {
                return ("can't find correct path to Attribute");
            }
        }
    }

    /**
     * calculate every count in every class.
     * 
     * @param list
     *            of the Dta Group.
     * @return HashMap= key: class name, value: Data Set counts of this class
     */
    private HashMap<String, Integer> getClassCount(List<Integer> list) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i : list) {
            String curClass = this.getDataSet().get(i).getValue(this.getLabelIndex());
            if (map.containsKey(curClass)) {
                map.put(curClass, map.get(curClass) + 1);
            } else {
                map.put(curClass, 1);
            }
        }
        return map;
    }

    /**
     * add a new Data Group index into this node.
     * 
     * @param index
     */
    public void addDataGroup(int index) {
        indexOfNodes.add(index);
    }

    /**
     * calculate entropy of value.
     * 
     * @param indexOfAttribute
     *            index of the attribute, used to calculate entropy.
     * @param classCountsOfEachValue:
     *            a pointer that will be updated in the method
     * @param sortedValue:
     *            a pointer that will be updated in the method
     */
    private void updateSortedList(int indexOfAttribute, HashMap<Double, int[]> labelCountOfEachValue,
            List<Double> sortedValue) {

        HashMap<String, Integer> labelsIndex = getIndexOfLabels();
        for (int i : this.getAllDataIndex()) {
            DataGroup current = this.getDataSet().get(i);
            double currentAttribute = Double.parseDouble(current.getValue(indexOfAttribute));
            int currentLabelIndex = labelsIndex.get(current.getValue(this.getLabelIndex()));

            if (!labelCountOfEachValue.containsKey(currentAttribute)) {
                int[] counts = new int[this.getLabelCount().keySet().size()];
                counts[currentLabelIndex]++;
                labelCountOfEachValue.put(currentAttribute, counts);
            } else {
                labelCountOfEachValue.get(currentAttribute)[currentLabelIndex]++;
            }
        }

        for (Double value : labelCountOfEachValue.keySet()) {
            sortedValue.add(value);
        }
        // Sorting Performed here.
        Collections.sort(sortedValue);
    }

}
