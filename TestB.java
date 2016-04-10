/**
 * Created by xinnacai on 4/8/16.
 */
public class TestB {
    public static void main(String[] args) {
        KNNLabel knn = new KNNLabel(3);
        ProcessData train = new ProcessData("trainProdIntro.binary.arff");
        ProcessData test = new ProcessData("testProdIntro.binary.arff");
        SimilarityMatrix service_type = new SimilarityMatrix("service_type.txt");
        SimilarityMatrix customer = new SimilarityMatrix("customer.txt");
        SimilarityMatrix size = new SimilarityMatrix("size.txt");
        SimilarityMatrix promotion = new SimilarityMatrix("promotion.txt");
        knn.addMatrix(promotion.getName(),promotion);
        knn.addMatrix(service_type.getName(),service_type);
        knn.addMatrix(customer.getName(),customer);
        knn.addMatrix(size.getName(),size);
        knn.setAttribute(train.getAttributes());
        knn.setTrainData(train.getRet());
        knn.trainModel();
        knn.setTrainData(train.getRet());
        knn.baseLine();
        knn.setTrainData(train.getRet());
        double[] weight = {1.00,9.45,7.39,5.65,0.14,0.69,1.00,9.82};
        System.out.print("the weight vector we select: ");
        for(double d : weight){
            System.out.print(d+",");
        }
        System.out.println();
        knn.setWeight(weight);
        knn.crossValidation();
        knn.setTrainData(train.getRet());
        knn.setTestData(test.getRet());
        knn.executeKNN();
        knn.printResult();

    }
}
