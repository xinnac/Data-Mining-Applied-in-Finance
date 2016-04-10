import sun.net.www.content.text.Generic;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by xinnacai on 4/3/16.
 */
public abstract class KNN {
    protected int k; // number of k nearest neighbour;
    protected ArrayList<ArrayList<String>> trainRawData;
    protected ArrayList<ArrayList<String>> testRawData;
    protected ArrayList<String> attribute;
    protected HashMap<String, SimilarityMatrix> matrixMap = new HashMap<>();
    protected double[] weight;
    protected double weightSum;
    protected double[] minValue;
    protected double[] maxValue;
    protected double accuracy;
    protected int groupNum = 10;
    protected double bottom = 1.0;
    protected double ceil = 100;
    protected double number = 10;
    protected Random r = new Random();

    protected ArrayList<ArrayList<Double>> trainData;
    protected ArrayList<ArrayList<Double>> testData;
    protected HashMap<Integer, ArrayList<ArrayList<String>>> groups;
    protected ArrayList<ArrayList<String>> lastGroup = new ArrayList<ArrayList<String>>();

    public KNN(int k, double accuracy) {
        this.k = k;
        this.accuracy = accuracy;
    }

    public KNN(int k) {
        this.k = k;
        this.accuracy = Double.MAX_VALUE;
    }

    public void setK(int k) {
        this.k = k;
    }

    /**
     * set train data before normalization
     * @param trainRawData
     */
    public void setTrainData(ArrayList<ArrayList<String>> trainRawData) {
        this.trainRawData = new ArrayList<ArrayList<String>>();
        this.trainRawData = trainRawData;
    }

    /**
     * set test data before normalization
     * @param testRawData
     */
    public void setTestData(ArrayList<ArrayList<String>> testRawData) {
        this.testRawData = new ArrayList<ArrayList<String>>();
        this.testRawData = testRawData;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    /**
     * add attribute of train and test data
     * @param attribute
     */
    public void setAttribute(ArrayList<String> attribute) {
        this.attribute = attribute;
    }

    /**
     * Add similarity matrix
     * @param matrixName
     * @param matrix
     */
    public void addMatrix(String matrixName, SimilarityMatrix matrix) {
        matrixMap.put(matrixName, matrix);
    }

    /**
     * set the minValue of each attribute.
     */
    public void setMinValue() {
        int size = attribute.size() - 1;
        minValue = new double[size];
        for (int i = 0; i < size; i++) {
            double min = Double.MAX_VALUE;
            String attributeName = attribute.get(i);
            if (matrixMap.containsKey(attributeName)) {
                continue;
            }
            for (int j = 0; j < trainRawData.size(); j++) {
                double d = Double.parseDouble(trainRawData.get(j).get(i));
                if (d < min) min = d;
            }
            minValue[i] = min;
        }

    }

    public double[] getMinValue() {
        return this.minValue;
    }

    /**
     * set the maxValue of each attribute.
     */
    public void setMaxValue() {
        int size = attribute.size() - 1;
        maxValue = new double[size];
        for (int i = 0; i < size; i++) {
            double max = Double.MIN_VALUE;
            String attributeName = attribute.get(i);
            if (matrixMap.containsKey(attributeName)) {
                continue;
            }
            for (int j = 0; j < trainRawData.size(); j++) {
                double d = Double.parseDouble(trainRawData.get(j).get(i));
                if (d > max) max = d;
            }
            maxValue[i] = max;
        }
    }

    public double[] getMaxValue() {
        return this.maxValue;
    }

    /**
     * normalize the train and test data
     */
    public void normalize() {
        trainData = new ArrayList<ArrayList<Double>>();
        testData = new ArrayList<ArrayList<Double>>();
        // normalize training data
        try {
            for (int i = 0; i < trainRawData.size(); i++) {
                ArrayList<Double> temp = new ArrayList<Double>();
                for (int j = 0; j < attribute.size() - 1; j++) {
                    double d = 0.0;
                    if (matrixMap.containsKey(attribute.get(j))) {
                        SimilarityMatrix sm = matrixMap.get(attribute.get(j));
                        d = (double) sm.getIndex(trainRawData.get(i).get(j));
                    } else {
                        d = Double.valueOf(trainRawData.get(i).get(j));
                        d = (d - minValue[j]) / (maxValue[j] - minValue[j]);
                    }
                    temp.add(d);
                }
                trainData.add(temp);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            for (int i = 0; i < testRawData.size(); i++) {
                ArrayList<Double> temp = new ArrayList<Double>();
                for (int j = 0; j < attribute.size() - 1; j++) {
                    double d = 0.0;
                    if (matrixMap.containsKey(attribute.get(j))) {
                        SimilarityMatrix sm = matrixMap.get(attribute.get(j));
                        d = (double) sm.getIndex(testRawData.get(i).get(j));
                    } else {
                        d = Double.valueOf(testRawData.get(i).get(j));
                        d = (d - minValue[j]) / (maxValue[j] - minValue[j]);
                    }
                    temp.add(d);
                }
                testData.add(temp);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Calculate the Euclidean distance of one test record with one training record.
     * @param test
     * @param train
     * @return
     */
    public double distance(ArrayList<Double> test, ArrayList<Double> train) {
        double distance = 0.0;
        double tempDistance = 0.0;
        for (int i = 0; i < attribute.size() - 1; i++) {
            double d = 0.0;
            if (matrixMap.containsKey(attribute.get(i))) {
                SimilarityMatrix sm = matrixMap.get(attribute.get(i));
                int indexr = test.get(i).intValue();
                int indexy = train.get(i).intValue();
                d = 1 - sm.getSimilarity(indexr, indexy);
                d = d * (weight[i]);
            } else {
                d = (double) Math.pow((test.get(i) - train.get(i)), 2) * weight[i];
            }
            tempDistance = tempDistance + d;
        }
        distance = Math.sqrt(tempDistance);
        return distance;
    }

    /**
     * store the distance with each instance of train data for one test instance in list
     * @param test
     * @return a sorted list storing the distance and the index of train data
     */
    public LinkedList<Map.Entry<Integer, Double>> distanceResult(ArrayList<Double> test) {
        HashMap<Integer, Double> result = new HashMap<Integer, Double>();
        for (int i = 0; i < trainData.size(); i++) {
            double score = distance(test, trainData.get(i));
            result.put(i, score);
        }
        LinkedList<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(result.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return list;
    }

    public void setWeightSum(double[] weight) {
        for (int i = 0; i < weight.length; i++) {
            weightSum += weight[i];
        }
    }

    /**
     * calculate the accuracy by comparing the real class with the predicted class
     * @return the accuracy for one set of training and testing data
     */
    public abstract double computeAccuracy();

    /**
     * partition 10 groups of train data.
     */
    public void partitionGroup() {
        groups = new HashMap<>();
        int gap = trainRawData.size() / groupNum;
        int remain = trainRawData.size() % groupNum;
        for (int i = 0; i < groupNum; i++) {
            ArrayList<ArrayList<String>> sublist = new ArrayList<ArrayList<String>>();
            for (int j = 0; j < gap; j++) {
                sublist.add(trainRawData.get(i + j * groupNum));
            }
            if (i < remain) sublist.add(trainRawData.get(gap * groupNum + i));
            groups.put(i, sublist);
        }
    }

    public abstract void executeKNN();

    /**
     * execute cross validation without training the weight and shows accuracy.
     */
    public void baseLine() {
        partitionGroup();
        weight = new double[attribute.size()-1];
        for(int i = 0;i< weight.length;i++){
            weight[i] = 1.0;
        }
        double cur_avac = 0.0;
        for (int i = 0; i < groupNum; i++) {
            setModelData(i);
            executeKNN();
            double ac = computeAccuracy();
            cur_avac += ac;
        }
        cur_avac = cur_avac / groupNum;
        System.out.println("baseline accuracy: " + cur_avac);
    }

    /**
     * execute cross validation with trained weight and shows accuracy.
     */
    public void crossValidation() {
        partitionGroup();
        double cur_avac = 0.0;
        for (int i = 0; i < groupNum; i++) {
            setModelData(i);
            executeKNN();
            double ac = computeAccuracy();
            cur_avac += ac;
        }
        cur_avac = cur_avac / groupNum;
        System.out.println("The model we select the accuracy: " + cur_avac);
    }

    /**
     * train the weight vector.
     */
    public void trainModel() {
        partitionGroup();
        weight = new double[attribute.size() - 1];
        double[] pre_weight = new double[attribute.size() - 1];
        for (int i = 0; i < weight.length; i++) {
            weight[i] = 1.0;
            pre_weight[i] = 1.0;
        }
        double cur_avac = 0.0;
        for (int i = 0; i < groupNum; i++) {
            setModelData(i);
            executeKNN();
            double ac = computeAccuracy();
            cur_avac += ac;
        }
        cur_avac = cur_avac / groupNum; // initialized baseline accuracy
        System.out.println(cur_avac);
        double pre_avac = cur_avac; // store previous accuracy
        int index = 0;
        boolean up = true; // if up is true, we increase the weight, if false we decrease the weight.
        boolean first = true; // whether it is first time we try to increase the weight
        int num = 0; // round time now, at least 10 round time to stop the training
        System.out.print(index + ":");
        while (true) {
            if (index == weight.length) { // go to next round
                num++;
                index = 0;
            }
            pre_weight[index] = weight[index];
            cur_avac = 0.0;
            if (up) { // first try to increase the weight
                double temp = increase(weight[index]);
                if (temp == weight[index]) {
                    up = false;
                    continue;
                }
                weight[index] = temp;
                System.out.print(weight[index] + ",");
            } else {// if after first try increasing, the accuracy declines, then decrease the weight
                double temp = decrease(weight[index]);
                if (temp == weight[index]) {
                    index++;
                    continue;
                }
                weight[index] = temp;
                System.out.print(weight[index] + ",");
            }
            for (int i = 0; i < groupNum; i++) {
                setModelData(i);
                executeKNN();
                double ac = computeAccuracy();
                cur_avac += ac;
            }
            cur_avac = cur_avac / groupNum;// after decrease or increase the weight, calculate the accuracy
            if (cur_avac >= accuracy || (Math.abs(cur_avac - pre_avac) < 1E-15 && num >= number && cur_avac >= 0.9)) {
                break;
            }// if the accuracy is larger than predefined accuracy, or the accuracy becomes stable and the round time is over 10 times.
            // break the loop and output the trained weight
            if (cur_avac > pre_avac && up && first) { // if the accuracy improves and it is first try and the weight has been increased
                pre_avac = cur_avac;                  // continue increasing the weight and set first try false
                first = false;
            } else if (cur_avac <= pre_avac && up && first) { // if the accuracy declines, and it is first try and the weight has been increased.
                weight[index] = pre_weight[index];          // recover the previous weight
                up = false;// try decrease the weight
                first = false;// set first try false
            } else if (cur_avac > pre_avac && up && !first) {// if the accuracy improves, and it is not first try and the weight has been increased
                pre_avac = cur_avac;// continue increasing the weight
            } else if (cur_avac <= pre_avac && up && !first) {// if the accuracy declines, and it is not first try and the weight has been increased
                weight[index] = pre_weight[index];// recover the previous weight
                index++;// move to next index
                System.out.println(pre_avac+"\n");
                System.out.print(index % (attribute.size() - 1) + ":");
                up = true;
                first = true;
            } else if (cur_avac > pre_avac && !up) {// if the accuracy improves and the weight has been decreased
                pre_avac = cur_avac;// continue decreasing the weight
            } else {// if the accuracy declines and the weight has been decreased
                weight[index] = pre_weight[index];// recover the previous weight
                index++;// move to next index
                System.out.println(pre_avac+"\n");
                System.out.print(index % (attribute.size() - 1) + ":");
                up = true;
                first = true;
            }
        }
        System.out.println();
        System.out.println("After training the accuracy: "+cur_avac); //finish the training, output the current accuracy.
        System.out.println();
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.print("After training the weigh vecotr: ");
        for(int i = 0;i< weight.length;i++){
            System.out.print(df.format(weight[i])+",");
        }
        System.out.println();
    }

    /**
     * set train data and test data in cross validation
     * @param i the i time of 10-fold cross validation
     */
    public void setModelData(int i) {
        ArrayList<ArrayList<String>> train = new ArrayList<ArrayList<String>>();
        setTestData(groups.get(i));
        for (int j = 0; j < groupNum; j++) {
            if (i == j) continue;
            train.addAll(groups.get(j));
        }
        setTrainData(train);
    }

    /**
     * increase the weight.
     * @param d the current weight
     * @return new weight
     */
    public double increase(double d) {
        double dd = r.nextDouble() * (10.0 - (d + 0.01)) + (d + 0.01);
        return dd;
    }


    /**
     * decrease the weight.
     * @param d the current weight
     * @return new weight
     */
    public double decrease(double d) {
        double dd = r.nextDouble() * (d - (0.0 + 0.01)) + (0.01);
        return dd;
    }

}










