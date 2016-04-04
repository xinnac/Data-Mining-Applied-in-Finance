import sun.net.www.content.text.Generic;

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
    protected double number = 0;
    protected Random r = new Random();

    protected ArrayList<ArrayList<Double>> trainData;
    protected ArrayList<ArrayList<Double>> testData;
    protected HashMap<Integer,ArrayList<ArrayList<String>>> groups;
    protected ArrayList<ArrayList<String>> lastGroup = new ArrayList<ArrayList<String>>();

    public KNN(int k,double accuracy,double bottom, double ceil, int number){
        this.k = k;
        this.accuracy = accuracy;
        this.bottom = bottom;
        this.ceil = ceil;
        this.number = number;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setTrainData(ArrayList<ArrayList<String>> trainRawData) {
        this.trainRawData = new ArrayList<ArrayList<String>>();
        this.trainRawData = trainRawData;
    }

    public void setTestData(ArrayList<ArrayList<String>> testRawData) {
        this.testRawData = new ArrayList<ArrayList<String>>();
        this.testRawData = testRawData;
    }

    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    public void setAttribute(ArrayList<String> attribute){
        this.attribute = attribute;
    }

    public void addMatrix(String matrixName, SimilarityMatrix matrix){
        matrixMap.put(matrixName,matrix);
    }

    public void setMinValue() {
        int size = attribute.size()-1;
        minValue = new double[size];
        for(int i=0;i<size;i++){
           double min = Double.MAX_VALUE;
           String attributeName = attribute.get(i);
           if(matrixMap.containsKey(attributeName)) {
               continue;
           }
            for(int j = 0;j < trainRawData.size();j++){
                double d = Double.parseDouble(trainRawData.get(j).get(i));
                if(d < min) min = d;
            }
            minValue[i] = min;
        }
//        for(int i = 0;i< minValue.length;i++){
//            System.out.println(minValue[i]);
//        }
    }

    public double[] getMinValue(){
        return this.minValue;
    }

    /**
     * set the maxValue of each attribute.
     */
    public void setMaxValue() {
        int size = attribute.size()-1;
        maxValue = new double[size];
        for(int i=0;i < size;i++){
            double max = Double.MIN_VALUE;
            String attributeName = attribute.get(i);
            if(matrixMap.containsKey(attributeName)) {
                continue;
            }
            for(int j = 0;j < trainRawData.size();j++){
                double d = Double.parseDouble(trainRawData.get(j).get(i));
                if(d > max) max = d;
            }
            minValue[i] = max;
        }
//        for(int i = 0;i< minValue.length;i++){
//            System.out.println(minValue[i]);
//        }
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
            for(int i = 0;i < trainRawData.size();i++){
                ArrayList<Double> temp = new ArrayList<Double>();
                for(int j = 0; j < attribute.size()-1;j++) {
                    double d = 0.0;
                    if(matrixMap.containsKey(attribute.get(j))) {
                        SimilarityMatrix sm = matrixMap.get(attribute.get(j));
                        d = (double)sm.getIndex(trainRawData.get(i).get(j));
                    } else {
                        d = Double.valueOf(trainRawData.get(i).get(j));
                        d = (d - minValue[j]) / (maxValue[j] - minValue[j]);
                    }
                    temp.add(d);
                }
                trainData.add(temp);
//                System.out.println(temp);
//                trainData.add(temp);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        // normalize test data
        try {
            for(int i = 0;i < testRawData.size();i++){
                ArrayList<Double> temp = new ArrayList<Double>();
                for(int j = 0; j < attribute.size()-1;j++) {
                    double d = 0.0;
                    if(matrixMap.containsKey(attribute.get(j))) {
                        SimilarityMatrix sm = matrixMap.get(attribute.get(j));
                        d = (double)sm.getIndex(testRawData.get(i).get(j));
                    } else {
                        d = Double.valueOf(testRawData.get(i).get(j));
                        d = (d - minValue[j]) / (maxValue[j] - minValue[j]);
                    }
                    temp.add(d);
                }
                testData.add(temp);
            }
        } catch (Exception e){
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
        for(int i =0;i<attribute.size()-1;i++) {
            double d = 0.0;
            if(matrixMap.containsKey(attribute.get(i))) {
                SimilarityMatrix sm = matrixMap.get(attribute.get(i));
                int indexr = test.get(i).intValue();
                int indexy = train.get(i).intValue();
                d = 1 - sm.getSimilarity(indexr,indexy);
                d = d * (weight[i]/weightSum);
                d = Math.pow(d,2);
            } else {
                d = (double)Math.pow((test.get(i)-train.get(i))* (weight[i]/weightSum),2);
            }
            tempDistance = tempDistance + d;
        }
        distance = Math.sqrt(tempDistance);
        return distance;
    }

    public  LinkedList<Map.Entry<Integer,Double>> distanceResult(ArrayList<Double> test) {
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
//        ScoreComparator sc = new ScoreComparator(result);
//        TreeMap<Integer,Double> tm = new TreeMap<Integer,Double>(sc);
//        tm.putAll(result);
//        return tm;
//    }
//
//    private class ScoreComparator implements Comparator<Integer> {
//
//        HashMap<Integer,Double> map = new HashMap<>();
//
//        ScoreComparator(HashMap<Integer,Double> map) {
//            this.map.putAll(map);
//        }
//
//        @Override
//        public int compare(Integer o1, Integer o2) {
//            if(map.get(o1) >= map.get(o2)) return -1;
//            else return 1;
//        }
    }

    public void setWeightSum(double[] weight){
        for(int i = 0; i< weight.length;i++ ){
            weightSum+= weight[i];
        }
    }
    public abstract double computeAccuracy() ;

    public void partitionGroup(){
        groups = new HashMap<>();
        int gap = trainRawData.size() / groupNum;
        int end = 0;
        for(int i = 0;i <groupNum;i++ ){
            ArrayList<ArrayList<String>> sublist = new ArrayList<ArrayList<String>>();
            if(i < groupNum-1) {
                sublist.addAll(trainRawData.subList(end, end+gap));
                groups.put(i,sublist);
                end = end + gap;
            } else {
                lastGroup.addAll(trainRawData.subList(end, trainRawData.size()));
            }
        }
    }
    public abstract void executeKNN();

    public void trainModel (){
        partitionGroup();
        weight = new double[attribute.size()-1];
        for(int i = 0;i< weight.length;i++){
            weight[i] = 1.0;
        }
        double cur_avac = 0.0;
        for(int i = 0;i< groupNum;i++){
            setModelData(i);
            executeKNN();
            double ac = computeAccuracy();
            cur_avac+= ac;
        }
        cur_avac = cur_avac / groupNum;
        System.out.println(cur_avac);
        double pre_avac = cur_avac;
        boolean b = true;
        int index = 0;
        double[] pre_weight = weight;
        boolean up = true;
        boolean first = true;
        int num = 0;
        while(b) {
            if(index == weight.length) {
                num++;
                index = 0;
            }
            pre_weight[index] = weight[index];
            pre_avac = cur_avac;
            cur_avac = 0.0;
            if(up) {
                double temp = increase(weight[index]);
                if (temp == weight[index]) {
                    index++;
                    continue;
                }
                weight[index] = temp;
                System.out.print(index +" "+ weight[index]+",");
            } else {
                double temp = decrease(weight[index]);
                if (temp == weight[index]) {
                    index++;
                    continue;
                }
                weight[index] = temp;
                System.out.print(index +" "+ weight[index]+",");
            }
            for(int i = 0;i< groupNum;i++){
                setModelData(i);
                executeKNN();
                double ac = computeAccuracy();
                cur_avac+= ac;
            }
            cur_avac = cur_avac / groupNum;
            if(cur_avac >= accuracy || (num == number && index == weight.length-1) ){
               break;
            }
            if(cur_avac > pre_avac && up && first) {
                first = false;
                continue;
            }
            else if(cur_avac <= pre_avac && up && first) {
                weight[index] = pre_weight[index];
                up = false;
                first = false;
                continue;
            } else if(cur_avac > pre_avac && up && !first) continue;
            else if(cur_avac <= pre_avac && up && !first) {
                weight[index] = pre_weight[index];
                index ++;
                up = true;
                first =  true;
                System.out.println();
            }
            else if(cur_avac > pre_avac && !up) continue;
            else {
                weight[index] = pre_weight[index];
                index++;
                up = true;
                first = true;
                System.out.println();
            }
        }
        System.out.println();
        System.out.println(cur_avac);
    }

    public void setModelData(int i) {
        ArrayList<ArrayList<String>> train = new ArrayList<ArrayList<String>>();
        if(i < groupNum - 1){
            setTestData(groups.get(i));
            for(int j = 0;j < groupNum - 1;j++){
                if(i == j) continue;
                train.addAll(groups.get(j));
            }
            train.addAll(lastGroup);
        } else {
            setTestData(lastGroup);
            for(int j = 0;j<groupNum - 1;j++){
                if(i == j) continue;
                train.addAll(groups.get(j));
            }
        }
        setTrainData(train);
    }

    public double increase(double d){
        if(d >= ceil -0.1) return ceil -0.1;
        return d+0.1+ (ceil - d - 0.1) * r.nextDouble();
    }

    public double decrease(double d){
        if(d == bottom ) return bottom +0.1;
        return bottom+ (d - bottom) * r.nextDouble();
    }

}










