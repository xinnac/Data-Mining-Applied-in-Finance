import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by xinnacai on 4/8/16.
 */
public class KNNReal extends KNN {
    private HashMap<Integer,Double> trainNumber;// map index of train data to its class label
    private ArrayList<Double> oringinalNumber;
    private ArrayList<Double> predictNumber;

    public KNNReal(int k,double accuracy){
        super(k,accuracy);
    }
    public KNNReal(int k){
        super(k);
    }


    public HashMap<Integer, Double> getTrainNumber() {
        return trainNumber;
    }

    public ArrayList<Double> getOringinalNumber() { return oringinalNumber;}

    public ArrayList<Double> getPredictNumber() {
        return predictNumber;
    }

    public void setOringinalNumber() {
        oringinalNumber = new ArrayList<Double>();
        for(int i =0;i  < testRawData.size();i++) {
            int n = (attribute.size())-1;
            double c = Double.valueOf(testRawData.get(i).get(n));
            oringinalNumber.add(c);
        }
    }

    public void setTrainNumber() {
        trainNumber = new HashMap<Integer,Double>();
        for(int i =0;i < trainRawData.size();i++){
            int n = (attribute.size())-1;
            double c = Double.valueOf(trainRawData.get(i).get(n));
            trainNumber.put(i,c);
        }
    }

    /**
     * predict class label.
     * @return score for one record of test data.
     */
    public double predictResult(ArrayList<Double> test) {
//        LinkedList<Map.Entry<Integer,Double>> list = new LinkedList<Map.Entry<Integer,Double>>(tm.entrySet());
        LinkedList<Map.Entry<Integer,Double>> list = distanceResult(test);
        double score = 0.0;
        for(int i = 0;i<k;i++){
            double d = trainNumber.get(list.get(i).getKey());
            score+= d;
        }
        score = score/k;
        return score;
    }

    public void getResult() {
        predictNumber = new ArrayList<Double>();
        for(int i = 0; i < testData.size();i++){
            double r  = predictResult(testData.get(i));
            predictNumber.add(r);
        }
    }

    public void executeKNN() {
        setMinValue();
        setMaxValue();
        normalize();
        setWeightSum(weight);
        setTrainNumber();
        setOringinalNumber();
        getResult();
//        System.out.println();
//        System.out.println(computeAccuracy());
//        System.out.println(predictLable.size());
//        for(int i = 0; i< predictLable.size();i++){
//            System.out.println(i+1+" "+predictLable.get(i)+" "+oringinalLabel.get(i));
//        }
//        System.out.println(predictLable.size());
//        for(int i = 0; i< predictLable.size();i++){
//            System.out.println(i+1+" "+predictLable.get(i)+" "+oringinalLabel.get(i));
//        }
    }

    public void executeKNNReal() {
        setMinValue();
        setMaxValue();
        normalize();
        setWeightSum(weight);
        setTrainNumber();
        getResult();
    }

    public void printResult() {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        System.out.println(predictNumber.size());
        for(int i = 0; i< predictNumber.size();i++){
            System.out.println(formatter.format(predictNumber.get(i)));
        }
    }


    public double computeAccuracy() {
        double ac = 0.0;
        double numOfRight = 0.0;
        for(int i = 0;i<predictNumber.size();i++){
            if(predictNumber.get(i) >= 20 && oringinalNumber.get(i) >= 20) ac++;
            if(predictNumber.get(i) < 20 && oringinalNumber.get(i) < 20) ac++;
        }
        return (double)ac/predictNumber.size();
    }
}
