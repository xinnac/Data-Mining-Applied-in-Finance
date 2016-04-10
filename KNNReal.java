import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by xinnacai on 4/8/16.
 */
public class KNNReal extends KNN {
    private HashMap<Integer,Double> trainNumber;// map index of train data to its real number in class
    private ArrayList<Double> oringinalNumber;// save the original real number in the test data
    private ArrayList<Double> predictNumber;// save the predicted real number in the test data

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

    /**
     * set the original real number of test data
     */
    public void setOringinalNumber() {
        oringinalNumber = new ArrayList<Double>();
        for(int i =0;i  < testRawData.size();i++) {
            int n = (attribute.size())-1;
            double c = Double.valueOf(testRawData.get(i).get(n));
            oringinalNumber.add(c);
        }
    }

    /**
     * set the real number of train data
     */
    public void setTrainNumber() {
        trainNumber = new HashMap<Integer,Double>();
        for(int i =0;i < trainRawData.size();i++){
            int n = (attribute.size())-1;
            double c = Double.valueOf(trainRawData.get(i).get(n));
            trainNumber.put(i,c);
        }
    }

    /**
     * predict real number for one test instance
     * @return real nymber for one record of test data.
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

    /**
     * predict real number all test cases.
     */
    public void getResult() {
        predictNumber = new ArrayList<Double>();
        for(int i = 0; i < testData.size();i++){
            double r  = predictResult(testData.get(i));
            predictNumber.add(r);
        }
    }

    /**
     * execute the knn in training process
     */
    public void executeKNN() {
        setMinValue();
        setMaxValue();
        normalize();
        setWeightSum(weight);
        setTrainNumber();
        setOringinalNumber(); // here test has original real number in class
        getResult();
    }

    /**
     * execute the knn in final predicting real nymber process
     */
    public void executeKNNReal() {
        setMinValue();
        setMaxValue();
        normalize();
        setWeightSum(weight);
        setTrainNumber();
        getResult();
    }

    /**
     * print the predicted real number for test cases.
     */
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
