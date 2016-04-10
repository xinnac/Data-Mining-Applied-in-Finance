import java.util.*;

/**
 * Created by xinnacai on 4/3/16.
 */
public class KNNLabel extends KNN {
    private HashMap<Integer,Integer> trainLabel;// map index of train data to its class label
    private ArrayList<Integer> oringinalLabel; // store the oringinal label of test data
    private ArrayList<Integer> predictLable;// store the predicted label of test data

    public KNNLabel(int k,double accuracy){
        super(k,accuracy);
    }
    public KNNLabel(int k){
        super(k);
    }

    public double getAccuracy(){
        return this.accuracy;
    }

    public HashMap<Integer, Integer> getTrainLabel() {
        return trainLabel;
    }

    public ArrayList<Integer> getOringinalLabel() {
        return oringinalLabel;
    }

    public ArrayList<Integer> getPredictLable() {
        return predictLable;
    }

    /**
     * set the original label of test data
     */
    public void setOringinalLabel() {
        oringinalLabel = new ArrayList<Integer>();
        for(int i =0;i  < testRawData.size();i++) {
            int n = (attribute.size())-1;
            String c = testRawData.get(i).get(n);
            int cc = getCC(c);
            oringinalLabel.add(cc);
        }
    }

    /**
     * set the label of train data
     */
    public void setTrainLabel() {
        trainLabel = new HashMap<Integer,Integer>();
        for(int i =0;i < trainRawData.size();i++){
            int n = (attribute.size())-1;
            String c = trainRawData.get(i).get(n);
            int cc = getCC(c);
            trainLabel.put(i,cc);
        }
    }

    /**
     * get the class label
     * @param c the String stored in class attribute
     * @return the integer label of the class
     */
    public int getCC (String c){
        int cc = 0;
        if(c.equalsIgnoreCase("C1")) cc = Constants.C1;
        else if(c.equalsIgnoreCase("C2")) cc = Constants.C2;
        else if(c.equalsIgnoreCase("C3")) cc = Constants.C3;
        else if(c.equalsIgnoreCase("C4")) cc = Constants.C4;
        else if(c.equalsIgnoreCase("C5")) cc = Constants.C5;
        else if(c.equalsIgnoreCase("1")) cc = 1;
        else if(c.equalsIgnoreCase("0")) cc = 0;
        else {
            throw new IllegalArgumentException("illegal label typ");
        }
        return cc;
    }


    /**
     * predict class label for one test instance.
     * @return the integer class label
     */
    public int predictResult(ArrayList<Double> test) {
        HashMap<Integer,Double> labelScore = new HashMap<Integer,Double>();// map label to its vote
        LinkedList<Map.Entry<Integer,Double>> list = distanceResult(test);
        for(int i = 0;i<k;i++){
            int c = trainLabel.get(list.get(i).getKey());
            double d = (double)1.0 / (list.get(i).getValue());
            if(!(labelScore.containsKey(c))) {
                labelScore.put(c,d);
            } else {
                double s = labelScore.get(c);
                s+= d;
                labelScore.put(c,s);
            }
        }
        LinkedList<Map.Entry<Integer,Double>> labelList = new LinkedList<Map.Entry<Integer,Double>>(labelScore.entrySet());
        Collections.sort(labelList, new Comparator<Map.Entry<Integer,Double>> (){
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                if (o1.getValue() < o2.getValue()) return 1;
                else if (o1.getValue() > o2.getValue()) return -1;
                else {
                    if (o1.getKey() <= o2.getKey()) return -1;
                    else return -1;
                }
            }
        });
        return labelList.getFirst().getKey();
    }

    /**
     * predict the result for all test instance and save the predicted result in the label list.
     */
    public void getResult() {
        predictLable = new ArrayList<Integer>();
        for(int i = 0; i < testData.size();i++){
            int r  = predictResult(testData.get(i));
            predictLable.add(r);
        }
    }

    /**
     * execute knn process
     */
    public void executeKNN() {
        setMinValue();
        setMaxValue();
        normalize();
        setWeightSum(weight);
        setTrainLabel();
        setOringinalLabel();
        getResult();
    }

    /**
     * print the predicted label for test instances
     */
    public void printResult() {
        System.out.println(predictLable.size());
        for(int i = 0; i< predictLable.size();i++){
            System.out.println(predictLable.get(i));
        }
    }

    public double computeAccuracy() {
        double ac = 0.0;
        double numOfRight = 0.0;
        for(int i = 0;i<predictLable.size();i++){
            if(oringinalLabel.get(i) == predictLable.get(i)) numOfRight++;
        }
        ac = (double) numOfRight/predictLable.size();
        return ac;
    }


}
