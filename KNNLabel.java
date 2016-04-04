import java.util.*;

/**
 * Created by xinnacai on 4/3/16.
 */
public class KNNLabel extends KNN {
    private HashMap<Integer,Integer> trainLabel;// map index of train data to its class label
    private ArrayList<Integer> oringinalLabel;
    private ArrayList<Integer> predictLable;

    public KNNLabel(int k,double accuracy,double bottom, double ceil, int number){
        super(k,accuracy,bottom,ceil,number);
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

    public void setOringinalLabel() {
        oringinalLabel = new ArrayList<Integer>();
        for(int i =0;i  < testRawData.size();i++) {
            int n = (attribute.size())-1;
            String c = testRawData.get(i).get(n);
            int cc = getCC(c);
            oringinalLabel.add(cc);
        }
    }

    public void setTrainLabel() {
        trainLabel = new HashMap<Integer,Integer>();
        for(int i =0;i < trainRawData.size();i++){
            int n = (attribute.size())-1;
            String c = trainRawData.get(i).get(n);
            int cc = getCC(c);
            trainLabel.put(i,cc);
        }
    }

    public int getCC (String c){
        int cc = 0;
        if(c.equalsIgnoreCase("C1")) cc = Constants.C1;
        else if(c.equalsIgnoreCase("C2")) cc = Constants.C2;
        else if(c.equalsIgnoreCase("C3")) cc = Constants.C3;
        else if(c.equalsIgnoreCase("C4")) cc = Constants.C4;
        else if(c.equalsIgnoreCase("C5")) cc = Constants.C5;
        else {throw new IllegalArgumentException("invalid class");}
        return cc;
    }


    /**
     * predict class label.
     * @return
     */
    public int predictResult(ArrayList<Double> test) {
        HashMap<Integer,Double> labelScore = new HashMap<Integer,Double>();// map label to its score
//        TreeMap tm = distanceResult(test);

//        LinkedList<Map.Entry<Integer,Double>> list = new LinkedList<Map.Entry<Integer,Double>>(tm.entrySet());
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
                    if (o1.getKey() <= o2.getKey()) return 1;
                    else return -1;
//                return -(o1.getValue().compareTo(o2.getValue()));
                }
            }
        });
        return labelList.getFirst().getKey();
    }

    public void getResult() {
        predictLable = new ArrayList<Integer>();
        for(int i = 0; i < testData.size();i++){
            int r  = predictResult(testData.get(i));
            predictLable.add(r);
        }
    }

    public void executeKNN() {
        setMinValue();
        setMaxValue();
        normalize();
        setWeightSum(weight);
        setTrainLabel();
        setOringinalLabel();
        getResult();
//        System.out.println(predictLable.size());
//        for(int i = 0; i< predictLable.size();i++){
//            System.out.println(i+1+" "+predictLable.get(i)+" "+oringinalLabel.get(i));
//        }
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